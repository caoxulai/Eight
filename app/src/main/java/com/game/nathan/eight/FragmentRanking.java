package com.game.nathan.eight;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by nathan on 7/9/2015.
 */
public class FragmentRanking extends Fragment {

    private static TextView[] name = new TextView[5];
    private static TextView[] step = new TextView[5];
    private static TextView[] time = new TextView[5];

    private static ImageView resetButton;

    // Declare SharedPreferences
    public static final String MyPREFERENCES = "rankings";
    SharedPreferences sharedpreferences;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.fragment_ranking, container, false);

        name[0] = (TextView) rl.findViewById(R.id.name1);
        step[0] = (TextView) rl.findViewById(R.id.step1);
        time[0] = (TextView) rl.findViewById(R.id.time1);

        name[1] = (TextView) rl.findViewById(R.id.name2);
        step[1] = (TextView) rl.findViewById(R.id.step2);
        time[1] = (TextView) rl.findViewById(R.id.time2);

        name[2] = (TextView) rl.findViewById(R.id.name3);
        step[2] = (TextView) rl.findViewById(R.id.step3);
        time[2] = (TextView) rl.findViewById(R.id.time3);

        name[3] = (TextView) rl.findViewById(R.id.name4);
        step[3] = (TextView) rl.findViewById(R.id.step4);
        time[3] = (TextView) rl.findViewById(R.id.time4);

        name[4] = (TextView) rl.findViewById(R.id.name5);
        step[4] = (TextView) rl.findViewById(R.id.step5);
        time[4] = (TextView) rl.findViewById(R.id.time5);

        resetButton = (ImageView) rl.findViewById(R.id.reset);

        for (int i = 1; i <= 5; i++) {
            initiate(i);
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

        return rl;
    }

    private void resetRanking() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear().commit();

        for (int i = 1; i <= 5; i++) {
            initiate(i);
        }

        Toast.makeText(getActivity().getApplicationContext(), "Ranking Reset", Toast.LENGTH_SHORT).show();

    }

    private void initiate(int i) {
        String ntag = "name" + Integer.toString(i);
        String stag = "step" + Integer.toString(i);
        String ttag = "time" + Integer.toString(i);
        String n = sharedpreferences.getString(ntag, "----");
        String s = sharedpreferences.getString(stag, "--");
        String t = sharedpreferences.getString(ttag, "--:--.--");
        name[i - 1].setText(n);
        step[i - 1].setText(s);
        time[i - 1].setText(t);
    }

}
