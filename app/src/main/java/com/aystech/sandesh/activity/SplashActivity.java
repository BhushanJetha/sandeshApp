package com.aystech.sandesh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.aystech.sandesh.R;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.UserSession;

public class SplashActivity extends AppCompatActivity {

    UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        userSession = new UserSession(this);

        // Splash screen timer
        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                if (userSession.getUSER_ID() != null && !userSession.getUSER_ID().equals("")) {
                    Constants.fragmentType = "Dashboard";
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
