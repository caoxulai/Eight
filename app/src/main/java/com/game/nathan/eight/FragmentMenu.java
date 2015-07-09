package com.game.nathan.eight;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Xulai on 2015/6/21.
 */
public class FragmentMenu extends Fragment {

    private static TextView start;
    private static TextView ranking;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.fragment_menu, container, false);

        start = (TextView) rl.findViewById(R.id.start);
        ranking = (TextView) rl.findViewById(R.id.ranking);

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.framelayout, new FragmentGame(), "game");
                transaction.addToBackStack(null);
                transaction.commit();


//
//                Fragment details = (Fragment)getFragmentManager().findFragmentById(R.id.fragment);
//                details = new FragmentGame();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.fragment, details);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.commit();

            }
        });

        ranking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // The third input is alias of this new fragment
                transaction.replace(R.id.framelayout, new FragmentRanking(), "ranking");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return rl;
    }
}