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

import static java.lang.String.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentGame_v2 extends Fragment {
    // for saving method cuz alert dialog cannot pass value out
    private int steps = 0;

    // order info
    private int[] order;

    // declare all the components
    private ImageView[] imageView;
    private int[] images = {R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six,
            R.drawable.seven, R.drawable.eight, R.drawable.transparent};

    private TextView textSteps;

    // for Timer
    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();

    // declare SharedPreferences as a database to store rankings
    public static final String MyPREFERENCES = "rankings";
    SharedPreferences sharedpreferences;

    // a Runnable instance to be timer
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            long timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            int seconds = (int) (timeInMilliseconds / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (timeInMilliseconds % 1000) / 10;
            timerValue.setText(format("%d:%s.%s", minutes, format("%02d", seconds), format("%02d", milliseconds)));
            customHandler.postDelayed(this, 0);
        }
    };

    public FragmentGame_v2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        ConstraintLayout fragmentGame = (ConstraintLayout) inflater.inflate(R.layout.fragment_game_v2, container, false);

        textSteps = (TextView) fragmentGame.findViewById(R.id.textSteps);
        timerValue = (TextView) fragmentGame.findViewById(R.id.timerValue);

        order = new int[9];
        imageView = new ImageView[9];
        imageView[0] = (ImageView) fragmentGame.findViewById(R.id.image1);
        imageView[1] = (ImageView) fragmentGame.findViewById(R.id.image2);
        imageView[2] = (ImageView) fragmentGame.findViewById(R.id.image3);
        imageView[3] = (ImageView) fragmentGame.findViewById(R.id.image4);
        imageView[4] = (ImageView) fragmentGame.findViewById(R.id.image5);
        imageView[5] = (ImageView) fragmentGame.findViewById(R.id.image6);
        imageView[6] = (ImageView) fragmentGame.findViewById(R.id.image7);
        imageView[7] = (ImageView) fragmentGame.findViewById(R.id.image8);
        imageView[8] = (ImageView) fragmentGame.findViewById(R.id.vacancy);

        for (int i = 0; i < order.length; i++) {
            final int finalI = i;
            imageView[finalI].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (changeable(finalI)) {
                        int vacancy = getVacancy();
                        imageView[vacancy].setImageResource(images[order[finalI]]);
                        imageView[finalI].setImageResource(images[order.length - 1]);
                        stepAndUpdate(finalI);
                    }
                }
            });
        }

        ImageView resetButton = (ImageView) fragmentGame.findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                restart();
            }
        });

        restart();

        return fragmentGame;
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
        textSteps.setText(format("Steps: %d", steps));

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
                imageView[k].setClickable(false);
            }

            Vibrator v = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            if (v != null) {
                v.vibrate(500);
            }

            // get the total time
            customHandler.removeCallbacks(updateTimerThread);
            final long timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            final int rank = getRank(timeInMilliseconds);

            int seconds = (int) (timeInMilliseconds / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (timeInMilliseconds % 1000) / 10;

            // Current record
            String stepsStr = Integer.toString(steps);
            String time = Integer.toString(minutes) + ":" + format("%02d", seconds) + "." + format("%02d", milliseconds);


            // A new record
            if (rank < 6) {


                ////////Pop out an alert dialog///////
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                LayoutInflater li = LayoutInflater.from(getActivity());
                View alertTop5 = li.inflate(R.layout.alert_top5, null);
                builder.setView(alertTop5);

                final TextView rankValue = (TextView) alertTop5.findViewById(R.id.rank);
                final TextView stepsValue = (TextView) alertTop5.findViewById(R.id.steps);
                final TextView timeValue = (TextView) alertTop5.findViewById(R.id.time);
                rankValue.setText(String.valueOf(rank));
                stepsValue.setText(stepsStr);
                timeValue.setText(time);


                final EditText nameValue = (EditText) alertTop5.findViewById(R.id.name);
                String name = sharedpreferences.getString("name", "");
                nameValue.setText(name);

                builder.setCancelable(false).setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Code for the button of alert dialog

                                String name = nameValue.getText().toString();
                                // trim beginning and tailing whitespaces
                                name = name.trim();
                                // trim duplicated whitespaces
                                name = name.replaceAll("\\s+", " ");
                                // Write record in SharedPreferences
                                saving(name, rank, timeInMilliseconds);

                                FragmentManager fragmentManager = getFragmentManager();
                                // Go to Menu fragment, but it will be instantly replaced by Ranking fragment, so it won't show up. and then it will go back to Menu when you finished in ranking
                                fragmentManager.popBackStack();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                // The third input is alias of this new fragment
                                FragmentRanking f_ranking = new FragmentRanking();
                                Bundle bundle = new Bundle();
                                bundle.putInt("rank", rank);
                                f_ranking.setArguments(bundle);

                                transaction.replace(R.id.framelayout, f_ranking, "ranking");
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        }).create();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            } else {
                ////////Pop out an alert dialog///////
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater li = LayoutInflater.from(getActivity());
                View alertFinish = li.inflate(R.layout.alert_finish, null);
                builder.setView(alertFinish);

                final TextView stepsValue = (TextView) alertFinish.findViewById(R.id.steps);
                final TextView timeValue = (TextView) alertFinish.findViewById(R.id.time);
                stepsValue.setText(stepsStr);
                timeValue.setText(time);

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

    private int getRank(long timeInMilliseconds) {
        // Rank Check
        int rank = 6;
        String sTag = "step" + Integer.toString(rank - 1);
        String msTag = "ms" + Integer.toString(rank - 1);
        String s = sharedpreferences.getString(sTag, null);
        long ms = sharedpreferences.getLong(msTag, 9999999);
        while (s == null || steps < Integer.parseInt(s) || (steps == Integer.parseInt(s) && timeInMilliseconds < ms)) {
            rank--;
            if (rank == 1)
                break;
            sTag = "step" + Integer.toString(rank - 1);
            msTag = "ms" + Integer.toString(rank - 1);
            s = sharedpreferences.getString(sTag, null);
            ms = sharedpreferences.getLong(msTag, 999999);
        }
        return rank;
    }

    private void saving(String name, int rank, long timeInMilliseconds) {
        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("name", name);
        editor.commit();

        for (int k = 4; k >= rank; k--) {
            overwrite(k);
        }

        String nameTag = "name" + Integer.toString(rank);
        String stepTag = "step" + Integer.toString(rank);
        String timeTag = "time" + Integer.toString(rank);
        String millisecondsTag = "ms" + Integer.toString(rank);

        int seconds = (int) (timeInMilliseconds / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int milliseconds = (int) (timeInMilliseconds % 1000) / 10;

        // Current record
        String stepsStr = Integer.toString(steps);
        String time = Integer.toString(minutes) + ":" + format("%02d", seconds) + "." + format("%02d", milliseconds);

        editor.putString(nameTag, name);
        editor.putString(stepTag, stepsStr);
        editor.putString(timeTag, time);
        editor.putLong(millisecondsTag, timeInMilliseconds);
        editor.apply();
    }

    private void overwrite(int rank) {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        String nameTag = "name" + Integer.toString(rank);
        String stepTag = "step" + Integer.toString(rank);
        String timeTag = "time" + Integer.toString(rank);
        String millisecondsTag = "ms" + Integer.toString(rank);
        String n = sharedpreferences.getString(nameTag, null);
        String s = sharedpreferences.getString(stepTag, null);
        String t = sharedpreferences.getString(timeTag, null);
        long ms = sharedpreferences.getLong(millisecondsTag, 9999);


        nameTag = "name" + Integer.toString(rank + 1);
        stepTag = "step" + Integer.toString(rank + 1);
        timeTag = "time" + Integer.toString(rank + 1);
        millisecondsTag = "ms" + Integer.toString(rank + 1);

        editor.putString(nameTag, n);
        editor.putString(stepTag, s);
        editor.putString(timeTag, t);
        editor.putLong(millisecondsTag, ms);
        editor.apply();
    }


    private void restart() {
        // reset steps
        steps = 0;
        textSteps.setText(format("Steps: %d", steps));

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
            imageView[i].setImageResource(images[order[i]]);
            imageView[i].setClickable(true);
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
