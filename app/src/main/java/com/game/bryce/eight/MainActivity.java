package com.game.bryce.eight;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends ActionBarActivity {

    final String adModAppID = "ca-app-pub-4003093615668727~6369461608";

    public static MediaPlayer bgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // add MobileAds
        MobileAds.initialize(this, adModAppID);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);




        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);


        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

//        getActionBar().hide();



        bgm = MediaPlayer.create(this, R.raw.bgm_main);
        bgm.setLooping(true);

        bgm.start();

        FragmentMenu fmenu = new FragmentMenu();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.framelayout, fmenu, "menu");
        transaction.commit();


    }

    @Override
    protected void onPause() {
        super.onPause();
        bgm.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bgm.start();
    }

    @Override
    public void onBackPressed() {

        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
