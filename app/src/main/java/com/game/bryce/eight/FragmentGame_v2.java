package com.game.bryce.eight;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import util.NumberImageView;
import util.OnSwipeTouchListener;
import util.Ranking;
import util.Record;
import util.Resizer;

import static java.lang.String.format;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentGame_v2 extends Fragment {
    private int boardSize = 3;

    // for saving method cuz alert dialog cannot pass value out
    private int steps = 0;

    // order info
    private int[] order;

    // declare all the components
    private ConstraintLayout canvas;
    private ConstraintLayout numberBoard;
    private NumberImageView[] numberImageViews;
    private int[] images = {R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six,
            R.drawable.seven, R.drawable.eight, R.drawable.transparent};

    //    private TextView textSteps;
    private ImageView resetButton;

    // declare a resizer
    private Resizer resizer;


    // for Timer
    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    private TextView localBest;

    // declare SharedPreferences as a database to store rankings
    public static final String MyPREFERENCES = "rankings";
    SharedPreferences sharedpreferences;
    Ranking ranking;

    // a Runnable instance to be timer
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            long timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            int seconds = (int) (timeInMilliseconds / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (timeInMilliseconds % 1000) / 10;
            timerValue.setText(format("%s:%s.%s", format("%02d", minutes), format("%02d", seconds), format("%02d", milliseconds)));
            customHandler.postDelayed(this, 0);
        }
    };

    public FragmentGame_v2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        ranking = new Ranking(sharedpreferences);

        ConstraintLayout fragmentGame = (ConstraintLayout) inflater.inflate(R.layout.fragment_game_v2, container, false);

        canvas = (ConstraintLayout) fragmentGame.findViewById(R.id.canvas);

        resetButton = (ImageView) fragmentGame.findViewById(R.id.reset);
//        textSteps = (TextView) fragmentGame.findViewById(R.id.textSteps);
        timerValue = (TextView) fragmentGame.findViewById(R.id.timerValue);
        localBest = (TextView) fragmentGame.findViewById(R.id.localBest);

        numberBoard = (ConstraintLayout) fragmentGame.findViewById(R.id.numberBoard);
        order = new int[9];
        numberImageViews = new NumberImageView[9];
        numberImageViews[0] = (NumberImageView) fragmentGame.findViewById(R.id.image1);
        numberImageViews[1] = (NumberImageView) fragmentGame.findViewById(R.id.image2);
        numberImageViews[2] = (NumberImageView) fragmentGame.findViewById(R.id.image3);
        numberImageViews[3] = (NumberImageView) fragmentGame.findViewById(R.id.image4);
        numberImageViews[4] = (NumberImageView) fragmentGame.findViewById(R.id.image5);
        numberImageViews[5] = (NumberImageView) fragmentGame.findViewById(R.id.image6);
        numberImageViews[6] = (NumberImageView) fragmentGame.findViewById(R.id.image7);
        numberImageViews[7] = (NumberImageView) fragmentGame.findViewById(R.id.image8);
        numberImageViews[8] = (NumberImageView) fragmentGame.findViewById(R.id.vacancy);

        resizer = initializeResizer();
        resizer.adjustAll(canvas);

        for (int i = 0; i < order.length; i++) {
            final int finalI = i;
            numberImageViews[finalI].setOnTouchListener(new OnSwipeTouchListener(getActivity().getApplicationContext()) {
                public void onTapUp() {
                    if (changeable(finalI)) {
                        int vacancy = getVacancy();
                        numberImageViews[vacancy].setImageResource(images[order[finalI]]);
                        numberImageViews[finalI].setImageResource(images[order.length - 1]);
                        stepAndUpdate(finalI);
                    }
                }

                public void onSwipeTop() {
                    int next = finalI - boardSize;
                    if (next == getVacancy()) {
                        int vacancy = getVacancy();
                        numberImageViews[vacancy].setImageResource(images[order[finalI]]);
                        numberImageViews[finalI].setImageResource(images[order.length - 1]);
                        stepAndUpdate(finalI);
                    }
                }

                public void onSwipeRight() {
                    int next = finalI + 1;
                    if (next % boardSize != 0 && next == getVacancy()) {
                        int vacancy = getVacancy();
                        numberImageViews[vacancy].setImageResource(images[order[finalI]]);
                        numberImageViews[finalI].setImageResource(images[order.length - 1]);
                        stepAndUpdate(finalI);
                    }
                }

                public void onSwipeLeft() {
                    int next = finalI - 1;
                    if (next % boardSize != boardSize - 1 && next == getVacancy()) {
                        int vacancy = getVacancy();
                        numberImageViews[vacancy].setImageResource(images[order[finalI]]);
                        numberImageViews[finalI].setImageResource(images[order.length - 1]);
                        stepAndUpdate(finalI);
                    }
                }

                public void onSwipeBottom() {
                    int next = finalI + boardSize;
                    if (next == getVacancy()) {
                        int vacancy = getVacancy();
                        numberImageViews[vacancy].setImageResource(images[order[finalI]]);
                        numberImageViews[finalI].setImageResource(images[order.length - 1]);
                        stepAndUpdate(finalI);
                    }
                }

            });
        }


        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                restart();
            }
        });

        restart();

        return fragmentGame;
    }

    private Resizer initializeResizer() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int deviceHeight = displayMetrics.heightPixels;
        int deviceWidth = displayMetrics.widthPixels;
        int canvasHeight = deviceHeight * 4 / 5;
        int canvasWidth = deviceWidth * 5 / 6;
        Resizer.set(canvas, canvasHeight, canvasWidth);

        float scale = Math.min(canvasHeight * 7 / 10, canvasWidth) / (300 * displayMetrics.density);

        return new Resizer(scale);
    }


    private int getVacancy() {
        for (int i = 0; i < order.length; i++) {
            if (order[i] == order.length - 1)
                return i;
        }
        return 0;
    }

    private boolean changeable(int curr) {
        int vacancy = getVacancy();
        if (curr == vacancy) return false;

        if (curr % 3 == 0) {
            if ((vacancy == curr + 1) || (vacancy == curr - 3) || (vacancy == curr + 3))
                return true;
        } else if (curr % 3 == 1) {
            if ((vacancy == curr + 1) || (vacancy == curr - 1) || (vacancy == curr - 3) || (vacancy == curr + 3))
                return true;
        } else if (curr % 3 == 2) {
            if ((vacancy == curr - 1) || (vacancy == curr - 3) || (vacancy == curr + 3))
                return true;
        }

        return false;

    }

    private void stepAndUpdate(int i) {
        steps++;
//        textSteps.setText(format("Steps: %d", steps));

        // update current order information
        int vacancy = getVacancy();
        order[vacancy] = order[i];
        order[i] = order.length - 1;


        int[] sorted = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        if (Arrays.equals(order, sorted)) {
            //successfully sort them in the right order
            MediaPlayer successSound = MediaPlayer.create(getActivity(), R.raw.success);
            successSound.start();

            for (int k = 0; k < order.length; k++) {
                numberImageViews[k].setClickable(false);
            }

            Vibrator v = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (v != null) {
                v.vibrate(500);
            }

            // get the total time
            customHandler.removeCallbacks(updateTimerThread);
            final long timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            final Record currRecord = new Record(steps, timeInMilliseconds);
            final int rank = ranking.getRank(currRecord);

            // A new record
            if (rank < 6) {

                ////////Pop out an alert dialog///////
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                LayoutInflater li = LayoutInflater.from(getActivity());
                View alertTop5 = li.inflate(R.layout.alert_top5_v2, null);
                resizer.adjustAll((ViewGroup) alertTop5);
                builder.setView(alertTop5);

                final TextView rankValue = (TextView) alertTop5.findViewById(R.id.rank);
                final TextView stepsValue = (TextView) alertTop5.findViewById(R.id.steps);
                final TextView timeValue = (TextView) alertTop5.findViewById(R.id.time);
                rankValue.setText(String.valueOf(rank));
                stepsValue.setText(currRecord.getStepsString());
                timeValue.setText(currRecord.getTimeString());


                final EditText nameValue = (EditText) alertTop5.findViewById(R.id.name);
                nameValue.setText(ranking.getLastName());

                builder.setCancelable(false).setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Do nothing here because we override this button later to change the close behaviour.
                                //However, we still need this because on older versions of Android unless we
                                //pass a handler the button doesn't get instantiated
                            }
                        }).create();
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Code for the button of alert dialog

                        // trim beginning and tailing whitespaces
                        String name = nameValue.getText().toString().trim();
                        // trim duplicated whitespaces
                        name = name.replaceAll("\\s+", " ");

                        if (name.matches("")) {
                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "You did not enter a username", Toast.LENGTH_SHORT);
                            int y = nameValue.getBottom() + nameValue.getHeight()/2;
                            toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.TOP, 0, y);
                            toast.show();

                        } else {
                            // add record in ranking
                            currRecord.setName(name);
                            ranking.addRecord(currRecord);

                            FragmentManager fragmentManager = getFragmentManager();
                            // Go to Menu fragment, but it will be instantly replaced by Ranking fragment, so it won't show up. and then it will go back to Menu when you finished in ranking
                            fragmentManager.popBackStack();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            // The third input is alias of this new fragment
                            FragmentRanking_v2 fragmentRanking = new FragmentRanking_v2();
                            Bundle bundle = new Bundle();
                            bundle.putInt("rank", rank);
                            fragmentRanking.setArguments(bundle);

                            transaction.replace(R.id.fragmentPlaceholder, fragmentRanking, "ranking");
                            transaction.addToBackStack(null);
                            transaction.commit();
                            alertDialog.dismiss();
                        }
                    }
                });

            } else {
                ////////Pop out an alert dialog///////
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater li = LayoutInflater.from(getActivity());
                View alertFinish = li.inflate(R.layout.alert_finish_v2, null);
                resizer.adjustAll((ViewGroup) alertFinish);
                builder.setView(alertFinish);

                final TextView stepsValue = (TextView) alertFinish.findViewById(R.id.steps);
                final TextView timeValue = (TextView) alertFinish.findViewById(R.id.time);
                stepsValue.setText(currRecord.getStepsString());
                timeValue.setText(currRecord.getTimeString());

                builder.setPositiveButton("New Game",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Code for the button of alert dialog
                                restart();
                            }
                        }).create();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    }

    private void restart() {
        // reset steps
        steps = 0;
//        textSteps.setText(format("Steps: %d", steps));
        localBest.setText("Best: " + ranking.getLocalBest());

        List<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < order.length - 1; i++) {
            dataList.add(i);
        }
        // generate random orders
        Collections.shuffle(dataList);
        while (inversions(dataList) % 2 == 1) {
            Collections.shuffle(dataList);
        }

        for (int i = 0; i < dataList.size(); i++) {
            order[i] = dataList.get(i);
        }
        order[order.length - 1] = order.length - 1;

        for (int i = 0; i < order.length; i++) {
            numberImageViews[i].setImageResource(images[order[i]]);
            numberImageViews[i].setClickable(true);
        }

        // start timer
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
        Toast.makeText(getActivity().getApplicationContext(), "New Game", Toast.LENGTH_SHORT).show();

    }

    private int inversions(List<Integer> dataList) {
        int inv = 0;
        for (int i = 0; i < dataList.size() - 1; i++) {
            for (int j = i + 1; j < dataList.size(); j++) {
                if (dataList.get(i) > dataList.get(j)) {
                    inv++;
                }
            }
        }
        return inv;
    }

}
