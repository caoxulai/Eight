package com.game.nathan.eight;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by Xulai on 2015/7/9.
 */
public class SavingIntentService extends IntentService {

    private static final String MyPREFERENCES = "rankings";
    private SharedPreferences sharedpreferences;

    private static final String TAG = SavingIntentService.class.getSimpleName();


    public SavingIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            int rank = bundle.getInt("rank");
            String name = bundle.getString("name");
            String steps = bundle.getString("steps");
            String time = bundle.getString("time");
            long ms = bundle.getLong("ms", 999999);


//            Context ctx = getApplicationContext();
//            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);

            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("name", name);
            editor.commit();

            for (int k = 4; k >= rank; k++) {
                overwrite(k);
            }

            String ntag = "name" + Integer.toString(rank);
            String stag = "step" + Integer.toString(rank);
            String ttag = "time" + Integer.toString(rank);
            String mstag = "ms" + Integer.toString(rank);

            editor.putString(ntag, name);
            editor.putString(stag, steps);
            editor.putString(ttag, time);
            editor.putLong(mstag, ms);
            editor.apply();
        }
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


}
