package com.game.bryce.eight;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import util.Resizer;

/**
 * Created by Xulai on 2015/6/21.
 */
public class FragmentMenu extends Fragment {

    private static ConstraintLayout fragmentMenu;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        fragmentMenu = (ConstraintLayout) inflater.inflate(R.layout.fragment_menu_v2, container, false);

        TextView start = (TextView) fragmentMenu.findViewById(R.id.start);
        TextView ranking = (TextView) fragmentMenu.findViewById(R.id.ranking);

        resizeComponents();

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // The third input is alias of this new fragment
                transaction.replace(R.id.fragmentPlaceholder, new FragmentGame_v2(), "game");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        ranking.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                // The third input is alias of this new fragment
                transaction.replace(R.id.fragmentPlaceholder, new FragmentRanking_v2(), "ranking");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return fragmentMenu;
    }

    private void resizeComponents() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int deviceHeight = displayMetrics.heightPixels;
        int deviceWidth = displayMetrics.widthPixels;

        float scale = (float) Math.sqrt(Math.sqrt(deviceHeight / 592 * deviceWidth / 360) / displayMetrics.density);

        Resizer resizer = new Resizer(scale);
        resizer.adjustAll(fragmentMenu);
    }
}