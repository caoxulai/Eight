package com.game.nathan.eight;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by nathan on 7/9/2015.
 */
public class FragmentRanking extends Fragment {

    private static TextView[] name = new TextView[5];
    private static TextView[] step = new TextView[5];
    private static TextView[] time = new TextView[5];

    private static ImageView resetButton;
//    private static TextView name1;
//    private static TextView step1;
//    private static TextView time1;
//
//    private static TextView name2;
//    private static TextView step2;
//    private static TextView time2;
//
//    private static TextView name3;
//    private static TextView step3;
//    private static TextView time3;
//
//    private static TextView name4;
//    private static TextView step4;
//    private static TextView time4;
//
//    private static TextView name5;
//    private static TextView step5;
//    private static TextView time5;

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

//        name1 = (TextView) rl.findViewById(R.id.name1);
//        step1 = (TextView) rl.findViewById(R.id.step1);
//        time1 = (TextView) rl.findViewById(R.id.time1);
//
//        name2 = (TextView) rl.findViewById(R.id.name2);
//        step2 = (TextView) rl.findViewById(R.id.step2);
//        time2 = (TextView) rl.findViewById(R.id.time2);
//
//        name3 = (TextView) rl.findViewById(R.id.name3);
//        step3 = (TextView) rl.findViewById(R.id.step3);
//        time3 = (TextView) rl.findViewById(R.id.time3);
//
//        name4 = (TextView) rl.findViewById(R.id.name4);
//        step4 = (TextView) rl.findViewById(R.id.step4);
//        time4 = (TextView) rl.findViewById(R.id.time4);
//
//        name5 = (TextView) rl.findViewById(R.id.name5);
//        step5 = (TextView) rl.findViewById(R.id.step5);
//        time5 = (TextView) rl.findViewById(R.id.time5);


//        String n1 = sharedpreferences.getString("name1", "");
//        String s1 = sharedpreferences.getString("step1", "");
//        String t1 = sharedpreferences.getString("time1", "");
//        name1.setText(n1);
//        step1.setText(s1);
//        time1.setText(t1);

        for (int i = 1; i <= 5; i++) {
            initiate(i);
        }

        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear().commit();

                for (int i = 1; i <= 5; i++) {
                    initiate(i);
                }
            }
        });

        return rl;
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
