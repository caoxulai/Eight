package com.game.bryce.eight;

/**
 * Created by Xulai on 2015/6/21.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import util.Resizer;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_v2);
        TextView version = (TextView) findViewById(R.id.version);
        version.setText("version " + getResources().getString(R.string.app_version));
        resizeComponents();
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void resizeComponents() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int deviceHeight = displayMetrics.heightPixels;
        int deviceWidth = displayMetrics.widthPixels;

        float scale = (float) (Math.sqrt(deviceHeight / 592 * deviceWidth / 360) / displayMetrics.density);

        Resizer resizer = new Resizer(scale);
        resizer.adjustAll((ViewGroup) findViewById(R.id.splashScreen));
    }
}