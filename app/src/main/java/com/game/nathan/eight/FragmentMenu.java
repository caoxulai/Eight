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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout rl = (RelativeLayout) inflater.inflate(R.layout.fragment_menu, container, false);

        start = (TextView) rl.findViewById(R.id.start);

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment, new FragmentGame(), "game");
                // transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();

                transaction.addToBackStack(null);

                transaction.commit();


            }
        });


        return rl;
    }
}