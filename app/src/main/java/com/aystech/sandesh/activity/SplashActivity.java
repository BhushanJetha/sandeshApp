package com.aystech.sandesh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.aystech.sandesh.R;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.PermissionManager;
import com.aystech.sandesh.utils.Permissions;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.UserSession;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity implements PermissionManager.PermissionListener {

    UserSession userSession;
    long dayDifference = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        userSession = new UserSession(this);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            Date currentDate = Calendar.getInstance().getTime();
            String previousOnlineDate = "";

            if(!userSession.getPreviousOnlineDateTime().isEmpty() && userSession.getPreviousOnlineDateTime() != null){
                previousOnlineDate = userSession.getPreviousOnlineDateTime();
                Log.d("Prev Login Date -->", previousOnlineDate);
            }
            Date previousDate = sdf.parse(previousOnlineDate);
            dayDifference = Uitility.calculateDateDifference(previousDate,currentDate);
            Log.d("Date Difference -->", String.valueOf(dayDifference));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (!PermissionManager.hasPermissions(this, Permissions.STORAGE)) {
            PermissionManager.requestPermissions(this, this,
                    "", Permissions.STORAGE);
        } else {
            gotoNextActivity();
        }
    }

    private void gotoNextActivity() {
        // Splash screen timer
        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {

                Log.d("User Idd--------->", userSession.getUSER_ID());
                if (userSession.getUSER_ID() != null && !userSession.getUSER_ID().equals("") && dayDifference <= 3) {
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

    @Override
    public void onPermissionsGranted(List<String> perms) {
        gotoNextActivity();
    }

    @Override
    public void onPermissionsDenied(List<String> perms) {
        PermissionManager.requestPermissions(this, this,
                "", Permissions.STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionManager.onRequestPermissionsResult(this, this, requestCode,
                Permissions.STORAGE, grantResults);
    }
}
