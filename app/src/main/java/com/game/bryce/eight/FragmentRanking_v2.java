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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import util.Ranking;
import util.Record;

/**
 * Created by nathan on 7/9/2015.
 */
public class FragmentRanking_v2 extends Fragment {

    private static TextView[] name = new TextView[5];
    private static TextView[] step = new TextView[5];
    private static TextView[] time = new TextView[5];
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

        name[0] = (TextView) fragmentRanking.findViewById(R.id.name1);
        step[0] = (TextView) fragmentRanking.findViewById(R.id.step1);
        time[0] = (TextView) fragmentRanking.findViewById(R.id.time1);

        name[1] = (TextView) fragmentRanking.findViewById(R.id.name2);
        step[1] = (TextView) fragmentRanking.findViewById(R.id.step2);
        time[1] = (TextView) fragmentRanking.findViewById(R.id.time2);

        name[2] = (TextView) fragmentRanking.findViewById(R.id.name3);
        step[2] = (TextView) fragmentRanking.findViewById(R.id.step3);
        time[2] = (TextView) fragmentRanking.findViewById(R.id.time3);

        name[3] = (TextView) fragmentRanking.findViewById(R.id.name4);
        step[3] = (TextView) fragmentRanking.findViewById(R.id.step4);
        time[3] = (TextView) fragmentRanking.findViewById(R.id.time4);

        name[4] = (TextView) fragmentRanking.findViewById(R.id.name5);
        step[4] = (TextView) fragmentRanking.findViewById(R.id.step5);
        time[4] = (TextView) fragmentRanking.findViewById(R.id.time5);

        newGame = (TextView) fragmentRanking.findViewById(R.id.new_game);

        resetButton = (ImageView) fragmentRanking.findViewById(R.id.reset);

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

    private void resetRanking() {
        ranking.clear();
        publishRanking();

        Toast.makeText(getActivity().getApplicationContext(), "Ranking Reset", Toast.LENGTH_SHORT).show();

    }

    private void publishRanking() {
        Record[] tops = ranking.getTops();
        for(int i=0; i<tops.length; i++){
            if(tops[i] != null) {
                name[i].setText(tops[i].getName());
                time[i].setText(tops[i].getTimeString());
                step[i].setText(tops[i].getStepsString());
            }
            else{
                name[i].setText("----");
                time[i].setText("--");
                step[i].setText("--:--.--");
            }
        }
    }

}
