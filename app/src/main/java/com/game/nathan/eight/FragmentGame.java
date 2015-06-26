package com.game.nathan.eight;

import android.app.AlertDialog;
import android.app.Fragment;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class FragmentGame extends Fragment {

    private int steps = 0;
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
        int t = vacancy;
        vacancy = position[i - 1];
        position[i - 1] = t;

        int[] target = {1, 2, 3, 4, 5, 6, 7, 8};
        if (Arrays.equals(position, target)) {
            //successfully sort them in the right order
            image1.setClickable(false);
            image2.setClickable(false);
            image3.setClickable(false);
            image4.setClickable(false);
            image5.setClickable(false);
            image6.setClickable(false);
            image7.setClickable(false);
            image8.setClickable(false);

            ////////Pop out an alert dialog///////
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            customHandler.removeCallbacks(updateTimerThread);
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            int secs = (int) (timeInMilliseconds / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (timeInMilliseconds % 1000) / 10;

            builder.setMessage("Aha! You made it!\n\nYour steps: "
                    + steps
                    + "\nYour time: "
                    + mins
                    + ":"
                    + String.format("%02d", secs)
                    + "."
                    + String.format("%02d", milliseconds)
                    + "\n\nThanks for playing.\n                               -- Xulai");

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
