package com.game.bryce.eight;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import util.Ranking;
import util.Record;
import util.Resizer;

/**
 * Created by nathan on 7/9/2015.
 */
public class FragmentRanking_v2 extends Fragment {

    private ConstraintLayout canvas;
    private ConstraintLayout rankingBoard;
    private static TextView[] rank = new TextView[5];
    private static TextView[] name = new TextView[5];
    private static TextView[] step = new TextView[5];
    private static TextView[] time = new TextView[5];
    private static TextView rankingTitle;
    private static TextView newGame;

    private static ImageView resetButton;

    // Declare SharedPreferences
    public static final String MyPREFERENCES = "rankings";
    SharedPreferences sharedpreferences;
    Ranking ranking;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        ranking = new Ranking(sharedpreferences);

        ConstraintLayout fragmentRanking = (ConstraintLayout) inflater.inflate(R.layout.fragment_ranking_v2, container, false);
        canvas = (ConstraintLayout) fragmentRanking.findViewById(R.id.canvas);
        rankingTitle = (TextView) fragmentRanking.findViewById(R.id.rankingTitle);
        resetButton = (ImageView) fragmentRanking.findViewById(R.id.reset);

        rankingBoard = (ConstraintLayout) fragmentRanking.findViewById(R.id.rankingBoard);

        rank[0] = (TextView) fragmentRanking.findViewById(R.id.rank1);
        name[0] = (TextView) fragmentRanking.findViewById(R.id.name1);
        step[0] = (TextView) fragmentRanking.findViewById(R.id.step1);
        time[0] = (TextView) fragmentRanking.findViewById(R.id.time1);

        rank[1] = (TextView) fragmentRanking.findViewById(R.id.rank2);
        name[1] = (TextView) fragmentRanking.findViewById(R.id.name2);
        step[1] = (TextView) fragmentRanking.findViewById(R.id.step2);
        time[1] = (TextView) fragmentRanking.findViewById(R.id.time2);

        rank[2] = (TextView) fragmentRanking.findViewById(R.id.rank3);
        name[2] = (TextView) fragmentRanking.findViewById(R.id.name3);
        step[2] = (TextView) fragmentRanking.findViewById(R.id.step3);
        time[2] = (TextView) fragmentRanking.findViewById(R.id.time3);

        rank[3] = (TextView) fragmentRanking.findViewById(R.id.rank4);
        name[3] = (TextView) fragmentRanking.findViewById(R.id.name4);
        step[3] = (TextView) fragmentRanking.findViewById(R.id.step4);
        time[3] = (TextView) fragmentRanking.findViewById(R.id.time4);

        rank[4] = (TextView) fragmentRanking.findViewById(R.id.rank5);
        name[4] = (TextView) fragmentRanking.findViewById(R.id.name5);
        step[4] = (TextView) fragmentRanking.findViewById(R.id.step5);
        time[4] = (TextView) fragmentRanking.findViewById(R.id.time5);

        newGame = (TextView) fragmentRanking.findViewById(R.id.newGame);


        resizeComponents();
        publishRanking();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int rank = bundle.getInt("rank", 0);
            if (rank > 0) {
                name[rank - 1].setTypeface(null, Typeface.BOLD_ITALIC);
                step[rank - 1].setTypeface(null, Typeface.BOLD_ITALIC);
                time[rank - 1].setTypeface(null, Typeface.BOLD_ITALIC);
            }
        }

        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ////////Pop out an alert dialog///////
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                LayoutInflater li = LayoutInflater.from(getActivity());
                View alert_reset_ranking = li.inflate(R.layout.alert_reset_ranking, null);

                resizeAlert((ViewGroup) alert_reset_ranking);
                builder.setView(alert_reset_ranking);

                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Code for the button of alertdialog
                                resetRanking();
                            }
                        }).create();
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Code for the button of alertdialog

                            }
                        }).create();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        newGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                // Go to Menu fragment, but it will be instantly replaced by Ranking fragment, so it won't show up. and then it will go back to Menu when you finished in ranking
                fragmentManager.popBackStack();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // The third input is alias of this new fragment
                transaction.replace(R.id.framelayout, new FragmentGame_v2(), "game");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return fragmentRanking;
    }

//    private void resizeAlert(ViewGroup alert) {
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        int deviceHeight = displayMetrics.heightPixels;
//        int deviceWidth = displayMetrics.widthPixels;
//
//        float scale1 = (float) Math.sqrt(Math.sqrt(deviceHeight / 592 * deviceWidth / 360) / displayMetrics.density);
//        float scale2 = Math.min(deviceHeight / 592, deviceWidth / 360) / displayMetrics.density;
//        float scale = (2*scale1 + scale2) / 3;
//
//        Resizer resizer = new Resizer(scale);
//        resizer.adjustAll(alert);
//    }
    private void resizeAlert(ViewGroup alert) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int deviceHeight = displayMetrics.heightPixels;
        int deviceWidth = displayMetrics.widthPixels;
        int canvasHeight = deviceHeight * 4 / 5;
        int canvasWidth = deviceWidth * 5 / 6;

        float scale = Math.min(canvasHeight * 6 / 10, canvasWidth) / (300 * displayMetrics.density);

        Resizer resizer = new Resizer(scale);
        resizer.adjustAll(alert);
    }

    private void resizeComponents() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int deviceHeight = displayMetrics.heightPixels;
        int deviceWidth = displayMetrics.widthPixels;
        int canvasHeight = deviceHeight * 4 / 5;
        int canvasWidth = deviceWidth * 5 / 6;
        Resizer.set(canvas, canvasHeight, canvasWidth);

        float scale = Math.min(canvasHeight * 6 / 10, canvasWidth) / (300 * displayMetrics.density);

        Resizer resizer = new Resizer(scale);
        resizer.adjustAll(canvas);
//        resizer.adjust(rankingTitle);
//        resizer.adjust(resetButton);
//        resizer.adjust(rankingBoard);
//        for (int i = 0; i < 5; i++) {
//            resizer.adjust(rank[i]);
//            resizer.adjust(name[i]);
//            resizer.adjust(step[i]);
//            resizer.adjust(time[i]);
//        }
//        resizer.adjust(newGame);
    }

    private void resetRanking() {
        ranking.clear();
        publishRanking();

        Toast.makeText(getActivity().getApplicationContext(), "Ranking Reset", Toast.LENGTH_SHORT).show();

    }

    private void publishRanking() {
        Record[] tops = ranking.getTops();
        for (int i = 0; i < tops.length; i++) {
            if (tops[i] != null) {
                name[i].setText(tops[i].getName());
                time[i].setText(tops[i].getTimeString());
                step[i].setText(tops[i].getStepsString());
            } else {
                name[i].setText("----");
                time[i].setText("--:--.--");
                step[i].setText("--");
            }
        }
    }

}
