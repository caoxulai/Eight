package com.game.nathan.eight;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentGame_v2 extends Fragment {

    private MediaPlayer successSound;

    // For saving method cuz alert dialog cannot pass value out
    private int rank = 0;
    private int steps = 0;
    private String stepsStr = "";
    private String time = "";
    private long milliseconds = 999999;

    // order info
    private int order[] = {5, 0, 7, 3, 1, 4, 6, 2, 8};

    private ImageView[] imageView;
    private int[] images = {R.drawable.one, R.drawable.two, R.drawable.three,
            R.drawable.four, R.drawable.five, R.drawable.six,
            R.drawable.seven, R.drawable.eight, R.drawable.transparent};


    private static TextView text_steps;

    private static ImageView image1;
    private static ImageView image2;
    private static ImageView image3;
    private static ImageView image4;
    private static ImageView image5;
    private static ImageView image6;
    private static ImageView image7;
    private static ImageView image8;
    private static ImageView resetButton;

    // For Timer
    private TextView timerValue;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    private long timeInMilliseconds = 0L;

    // Declare SharedPreferences
    public static final String MyPREFERENCES = "rankings";
    SharedPreferences sharedpreferences;


    // A Runnable instance to be timer
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;

            int secs = (int) (timeInMilliseconds / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (timeInMilliseconds % 1000) / 10;
            timerValue.setText("" + mins + ":" + String.format("%02d", secs)
                    + "." + String.format("%02d", milliseconds));
            customHandler.postDelayed(this, 0);
        }
    };

    public FragmentGame_v2() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        ConstraintLayout rl = (ConstraintLayout) inflater.inflate(R.layout.fragment_game_v2, container, false);

        text_steps = (TextView) rl.findViewById(R.id.textSteps);
        timerValue = (TextView) rl.findViewById(R.id.timerValue);

        imageView = new ImageView[9];
        imageView[0] = (ImageView) rl.findViewById(R.id.image1);
        imageView[1] = (ImageView) rl.findViewById(R.id.image2);
        imageView[2] = (ImageView) rl.findViewById(R.id.image3);
        imageView[3] = (ImageView) rl.findViewById(R.id.image4);
        imageView[4] = (ImageView) rl.findViewById(R.id.image5);
        imageView[5] = (ImageView) rl.findViewById(R.id.image6);
        imageView[6] = (ImageView) rl.findViewById(R.id.image7);
        imageView[7] = (ImageView) rl.findViewById(R.id.image8);
        imageView[8] = (ImageView) rl.findViewById(R.id.vacancy);

//        image1 = (ImageView) rl.findViewById(R.id.image1);
//        image2 = (ImageView) rl.findViewById(R.id.image2);
//        image3 = (ImageView) rl.findViewById(R.id.image3);
//        image4 = (ImageView) rl.findViewById(R.id.image4);
//        image5 = (ImageView) rl.findViewById(R.id.image5);
//        image6 = (ImageView) rl.findViewById(R.id.image6);
//        image7 = (ImageView) rl.findViewById(R.id.image7);
//        image8 = (ImageView) rl.findViewById(R.id.image8);
        resetButton = (ImageView) rl.findViewById(R.id.reset);
        restart();

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);

        for (int i = 0; i < order.length; i++) {
            final int finalI = i;
            imageView[finalI].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (changeable(finalI)) {
                        int vacancy = getVacancy();
                        imageView[vacancy].setImageResource(images[order[finalI]]);
                        imageView[finalI].setImageResource(images[order.length-1]);
                        stepAndUpdate(finalI);
                    }
                }
            });


        }


//        image1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (changeable(order[0], vacancy)) {
//                    changePosition(image1, vacancy);
//                    stepAndUpdate(1);
//                }
//            }
//        });
//
//        image2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (changeable(order[1], vacancy)) {
//                    changePosition(image2, vacancy);
//                    stepAndUpdate(2);
//                }
//            }
//        });
//
//        image3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (changeable(order[2], vacancy)) {
//                    changePosition(image3, vacancy);
//                    stepAndUpdate(3);
//                }
//            }
//        });
//
//        image4.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (changeable(order[3], vacancy)) {
//                    changePosition(image4, vacancy);
//                    stepAndUpdate(4);
//                }
//            }
//        });
//
//        image5.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (changeable(order[4], vacancy)) {
//                    changePosition(image5, vacancy);
//                    stepAndUpdate(5);
//                }
//            }
//        });
//
//        image6.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (changeable(order[5], vacancy)) {
//                    changePosition(image6, vacancy);
//                    stepAndUpdate(6);
//                }
//            }
//        });
//
//        image7.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (changeable(order[6], vacancy)) {
//                    changePosition(image7, vacancy);
//                    stepAndUpdate(7);
//                }
//            }
//        });
//
//        image8.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if (changeable(order[7], vacancy)) {
//                    changePosition(image8, vacancy);
//                    stepAndUpdate(8);
//                }
//            }
//        });
//
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                restart();
            }
        });

        return rl;
    }

    private int getVacancy(){
        for(int i=0; i<order.length; i++){
            if(order[i] == order.length-1)
                return i;
        }
        return 0;
    }

    private boolean changeable(int curr) {
        int vacancy = getVacancy();
        if(curr == vacancy)  return false;

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

    private void changePosition(ImageView img, int pos) {
        // locate 'img' to order 'pos'
        RelativeLayout.LayoutParams par = (RelativeLayout.LayoutParams) img.getLayoutParams();
        // image1.setImageResource(R.drawable.image2);
        float d = getResources().getDisplayMetrics().density;
        //get parent density and transform dip to pix
        par.topMargin = (int) (TMar(pos) * d);
        par.leftMargin = (int) (LMar(pos) * d);

        img.setLayoutParams(par);
    }

    private int TMar(int pos) {
        // calculate the margin from top
        return (pos - 1) / 3 * 120 + 99;
    }

    private int LMar(int pos) {
        // calculate the margin from left
        return (pos - 1) % 3 * 100 + 19;
    }

    private void stepAndUpdate(int i) {
        steps++;
        text_steps.setText("Steps: " + steps);

        // update current order information
        int vacancy = getVacancy();
        order[vacancy] = order[i];
        order[i] = order.length-1;


        int[] target = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        if (Arrays.equals(order, target)) {
            //successfully sort them in the right order
            successSound = MediaPlayer.create(getActivity(), R.raw.success);
            successSound.start();

            for (int k = 0; k < order.length; k++) {
                imageView[k].setClickable(false);
            }

            Vibrator v = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);


            customHandler.removeCallbacks(updateTimerThread);
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            int seconds = (int) (timeInMilliseconds / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (timeInMilliseconds % 1000) / 10;


            // Rank Check
            rank = 6;
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


            // Current record
            stepsStr = Integer.toString(steps);
            time = Integer.toString(minutes) + ":" + String.format("%02d", seconds) + "." + String.format("%02d", milliseconds);
            this.milliseconds = timeInMilliseconds;

            // A new record
            if (rank < 6) {

                ////////Pop out an alert dialog///////
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                LayoutInflater li = LayoutInflater.from(getActivity());
                View alert_top5 = li.inflate(R.layout.alert_top5, null);
                builder.setView(alert_top5);

                final TextView rankValue = (TextView) alert_top5.findViewById(R.id.rank);
                final TextView stepsValue = (TextView) alert_top5.findViewById(R.id.steps);
                final TextView timeValue = (TextView) alert_top5.findViewById(R.id.time);
                rankValue.setText(Integer.toString(rank));
                stepsValue.setText(stepsStr);
                timeValue.setText(time);


                final EditText nameValue = (EditText) alert_top5.findViewById(R.id.name);
                String name = sharedpreferences.getString("name", "");
                nameValue.setText(name);

                builder.setCancelable(false).setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Code for the button of alertdialog

                                String name = nameValue.getText().toString();
                                // trim beginning and tailing whitespaces
                                name = name.trim();
                                // trim duplicated whitespaces
                                name = name.replaceAll("\\s+", " ");
                                // Write record in SharedPreferences
                                saving(name);

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

    private void saving(String name) {


//            Context ctx = getApplicationContext();
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("name", name);
        editor.commit();

        for (int k = 4; k >= rank; k--) {
            overwrite(k);
        }

        String ntag = "name" + Integer.toString(rank);
        String stag = "step" + Integer.toString(rank);
        String ttag = "time" + Integer.toString(rank);
        String mstag = "ms" + Integer.toString(rank);

        editor.putString(ntag, name);
        editor.putString(stag, stepsStr);
        editor.putString(ttag, time);
        editor.putLong(mstag, milliseconds);
        editor.apply();


    }

    private void overwrite(int rank) {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        String ntag = "name" + Integer.toString(rank);
        String stag = "step" + Integer.toString(rank);
        String ttag = "time" + Integer.toString(rank);
        String mstag = "ms" + Integer.toString(rank);
        String n = sharedpreferences.getString(ntag, null);
        String s = sharedpreferences.getString(stag, null);
        String t = sharedpreferences.getString(ttag, null);
        long ms = sharedpreferences.getLong(mstag, 9999);


        ntag = "name" + Integer.toString(rank + 1);
        stag = "step" + Integer.toString(rank + 1);
        ttag = "time" + Integer.toString(rank + 1);
        mstag = "ms" + Integer.toString(rank + 1);

        editor.putString(ntag, n);
        editor.putString(stag, s);
        editor.putString(ttag, t);
        editor.putLong(mstag, ms);
        editor.apply();
    }


    private void restart() {
        steps = 0;
        text_steps.setText("Steps: " + steps);

        List<Integer> dataList = new ArrayList<>();
        for (int i = 0; i < order.length-1; i++) {
            dataList.add(i);
        }
        // generate random positions
        Collections.shuffle(dataList);
        while (inversions(dataList) % 2 == 1) {
            Collections.shuffle(dataList);
        }

        for (int i = 0; i < dataList.size(); i++) {
            order[i] = dataList.get(i);
        }
        order[order.length-1] = order.length-1;

        for (int i = 0; i < order.length; i++) {
            imageView[i].setImageResource(images[order[i]]);
            imageView[i].setClickable(true);
        }

//        setImage(image1, order[0]);
//        setImage(image2, order[1]);
//        setImage(image3, order[2]);
//        setImage(image4, order[3]);
//        setImage(image5, order[4]);
//        setImage(image6, order[5]);
//        setImage(image7, order[6]);
//        setImage(image8, order[7]);

//        changePosition(image1, order[0]);
//        changePosition(image2, order[1]);
//        changePosition(image3, order[2]);
//        changePosition(image4, order[3]);
//        changePosition(image5, order[4]);
//        changePosition(image6, order[5]);
//        changePosition(image7, order[6]);
//        changePosition(image8, order[7]);

//        image1.setClickable(true);
//        image2.setClickable(true);
//        image3.setClickable(true);
//        image4.setClickable(true);
//        image5.setClickable(true);
//        image6.setClickable(true);
//        image7.setClickable(true);
//        image8.setClickable(true);

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
