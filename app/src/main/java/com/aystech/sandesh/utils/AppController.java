package com.aystech.sandesh.utils;

import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.aystech.sandesh.activity.LoginActivity;

public class AppController extends MultiDexApplication {

    public static String imageURL = "http://13.127.119.95:4444/api/assets/";
    public static String testingURL = "http://13.127.119.95:4444/";
    public static String devURL = "http://13.127.119.95:5555/";
    public static String BASEURL;

    private static AppController mInstance;

    private static boolean isBaseUrl = true;

    private UserSession userSession;

    public static AppController getmInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mInstance = this;

        if (isBaseUrl) {
            BASEURL = devURL;
        } else {
            BASEURL = testingURL;
        }

        userSession = new UserSession(this);
    }

    public void accessUnAuthorization() {
        userSession.clearUserSession();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AppController.getmInstance().startActivity(intent);

    }

    public String getJwtToken() {
        return userSession.getJWTToken();
    }

}