package com.game.nathan.eight;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import android.content.DialogInterface;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Vibrator;

/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentGame extends Fragment {

    private MediaPlayer succees_sound;

    // For saving method cuz alert dialog cannot pass value out
    private int rank = 0;
    private int steps = 0;
    private String steps_str = "";
    private String time = "";
    private long miliseconds = 999999;

    // position info
    private int vacancy = 9;
    private int position[] = {5, 8, 7, 3, 1, 4, 6, 2};


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

    public FragmentGame() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.fragment_game, container, false);

        text_steps = (TextView) rl.findViewById(R.id.text_steps);
        timerValue = (TextView) rl.findViewById(R.id.timerValue);
        image1 = (ImageView) rl.findViewById(R.id.image1);
        image2 = (ImageView) rl.findViewById(R.id.image2);
        image3 = (ImageView) rl.findViewById(R.id.image3);
        image4 = (ImageView) rl.findViewById(R.id.image4);
        image5 = (ImageView) rl.findViewById(R.id.image5);
        image6 = (ImageView) rl.findViewById(R.id.image6);
        image7 = (ImageView) rl.findViewById(R.id.image7);
        image8 = (ImageView) rl.findViewById(R.id.image8);
        resetButton = (ImageView) rl.findViewById(R.id.reset);
        restart();

        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);

        image1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (changable(position[0], vacancy)) {
                    changePosition(image1, vacancy);
                    stepAndUpdate(1);
                }
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (changable(position[1], vacancy)) {
                    changePosition(image2, vacancy);
                    stepAndUpdate(2);
                }
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (changable(position[2], vacancy)) {
                    changePosition(image3, vacancy);
                    stepAndUpdate(3);
                }
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (changable(position[3], vacancy)) {
                    changePosition(image4, vacancy);
                    stepAndUpdate(4);
                }
            }
        });

        image5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (changable(position[4], vacancy)) {
                    changePosition(image5, vacancy);
                    stepAndUpdate(5);
                }
            }
        });

        image6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (changable(position[5], vacancy)) {
                    changePosition(image6, vacancy);
                    stepAndUpdate(6);
                }
            }
        });

        image7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (changable(position[6], vacancy)) {
                    changePosition(image7, vacancy);
                    stepAndUpdate(7);
                }
            }
        });

        image8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (changable(position[7], vacancy)) {
                    changePosition(image8, vacancy);
                    stepAndUpdate(8);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                restart();
            }
        });

        return rl;
    }


    private boolean changable(int origin, int target) {
        if (origin % 3 == 1) {
            if ((target == origin + 1) || (target == origin - 3) || (target == origin + 3))
                return true;
        } else if (origin % 3 == 2) {
            if ((target == origin + 1) || (target == origin - 1) || (target == origin - 3) || (target == origin + 3))
                return true;
        } else if (origin % 3 == 0) {
            if ((target == origin - 1) || (target == origin - 3) || (target == origin + 3))
                return true;
        }

        return false;

    }


    private void changePosition(ImageView img, int pos) {
        // locate 'img' to position 'pos'
        RelativeLayout.LayoutParams par = (RelativeLayout.LayoutParams) img.getLayoutParams();
        // image1.setImageResource(R.drawable.image2);
        float d = getResources().getDisplayMetrics().density;
        //get parent density and tranform dip to pix
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

        // update current postion infomation
        int p = vacancy;
        vacancy = position[i - 1];
        position[i - 1] = p;

        int[] target = {1, 2, 3, 4, 5, 6, 7, 8};
        if (Arrays.equals(position, target)) {
            succees_sound = MediaPlayer.create(getActivity(), R.raw.success);
            succees_sound.start();


            //successfully sort them in the right order
            image1.setClickable(false);
            image2.setClickable(false);
            image3.setClickable(false);
            image4.setClickable(false);
            image5.setClickable(false);
            image6.setClickable(false);
            image7.setClickable(false);
            image8.setClickable(false);

            Vibrator v = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);


            customHandler.removeCallbacks(updateTimerThread);
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            int secs = (int) (timeInMilliseconds / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (timeInMilliseconds % 1000) / 10;


            // Rank Check
            rank = 6;
            String stag = "step" + Integer.toString(rank - 1);
            String mstag = "ms" + Integer.toString(rank - 1);
            String s = sharedpreferences.getString(stag, null);
            long ms = sharedpreferences.getLong(mstag, 9999999);
            while (s == null || steps < Integer.parseInt(s) || (steps == Integer.parseInt(s) && timeInMilliseconds < ms)) {
                rank--;
                if (rank == 1)
                    break;
                stag = "step" + Integer.toString(rank - 1);
                mstag = "ms" + Integer.toString(rank - 1);
                s = sharedpreferences.getString(stag, null);
                ms = sharedpreferences.getLong(mstag, 999999);
            }


            // Current record
            steps_str = Integer.toString(steps);
            time = Integer.toString(mins) + ":" + String.format("%02d", secs) + "." + String.format("%02d", milliseconds);
            miliseconds = timeInMilliseconds;

            // A new record
            if (rank < 6) {

                ////////Pop out an alert dialog///////
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                LayoutInflater li = LayoutInflater.from(getActivity());
                View alert_top5 = li.inflate(R.layout.alert_top5, null);
                builder.setView(alert_top5);

                final TextView rank_value = (TextView) alert_top5.findViewById(R.id.rank);
                final TextView steps_value = (TextView) alert_top5.findViewById(R.id.steps);
                final TextView time_value = (TextView) alert_top5.findViewById(R.id.time);
                rank_value.setText(Integer.toString(rank));
                steps_value.setText(steps_str);
                time_value.setText(time);


                final EditText name_value = (EditText) alert_top5.findViewById(R.id.name);
                String name = sharedpreferences.getString("name", "");
                name_value.setText(name);

                builder.setCancelable(false).setPositiveButton("Submit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Code for the button of alertdialog

                                String name = name_value.getText().toString();
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
                View alert_finish = li.inflate(R.layout.alert_finish, null);
                builder.setView(alert_finish);

                final TextView steps_value = (TextView) alert_finish.findViewById(R.id.steps);
                final TextView time_value = (TextView) alert_finish.findViewById(R.id.time);
                steps_value.setText(steps_str);
                time_value.setText(time);

                builder.setPositiveButton("New Game",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Code for the button of alertdialog
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
        editor.putString(stag, steps_str);
        editor.putString(ttag, time);
        editor.putLong(mstag, miliseconds);
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

        vacancy = 9;

        List<Integer> dataList = new ArrayList<Integer>();
        for (int i = 1; i < 9; i++) {
            dataList.add(i);
        }
        // generate random positions
        Collections.shuffle(dataList);
        while (inversions(dataList) % 2 == 1) {
            Collections.shuffle(dataList);
        }

        for (int i = 0; i < dataList.size(); i++) {
            position[i] = dataList.get(i);
        }

        changePosition(image1, position[0]);
        changePosition(image2, position[1]);
        changePosition(image3, position[2]);
        changePosition(image4, position[3]);
        changePosition(image5, position[4]);
        changePosition(image6, position[5]);
        changePosition(image7, position[6]);
        changePosition(image8, position[7]);

        image1.setClickable(true);
        image2.setClickable(true);
        image3.setClickable(true);
        image4.setClickable(true);
        image5.setClickable(true);
        image6.setClickable(true);
        image7.setClickable(true);
        image8.setClickable(true);

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
