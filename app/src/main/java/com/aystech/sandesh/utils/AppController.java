package com.aystech.sandesh.utils;

import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.aystech.sandesh.activity.LoginActivity;

public class AppController extends MultiDexApplication {

    private static AppController mInstance;

    private UserSession userSession;

    public static AppController getmInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        mInstance = this;

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