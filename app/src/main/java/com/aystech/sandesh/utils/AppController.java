package com.aystech.sandesh.utils;

import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.aystech.sandesh.activity.LoginActivity;

public class AppController extends MultiDexApplication {

    public static String prodURL = "http://www.app.avantikasandesh.com/";
    public static String devURL = "http://www.app.avantikasandesh.com:4444/";
    public static String invoiceURL = "api/invoice";
    public static String statementURL = "api/statement";
    public static String imageURL = "api/assets/";
    public static String pricing_policy = "api/document/pricing_policy.html";
    public static String terms_and_conditions = "api/document/terms_and_conditions.html";
    public static String frequently_ask_question = "api/document/frequently_ask_question.html";
    public static String kyc = "https://kyc-api.aadhaarkyc.io/api/v1/aadhaar-v2/";
    public static String BASEURL;

    private static AppController mInstance;

    public static boolean isBaseUrl = true;

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
            BASEURL = prodURL;
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

    public String getKYCToken() {
        return "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0eXBlIjoiYWNjZXNzIiwiaWRlbnRpdHkiOiJkZXYuYXZhbnRpa2FAYWFkaGFhcmFwaS5pbyIsIm5iZiI6MTU5NDkwMzk0MywiZXhwIjoxOTEwMjYzOTQzLCJ1c2VyX2NsYWltcyI6eyJzY29wZXMiOlsicmVhZCJdfSwiZnJlc2giOmZhbHNlLCJqdGkiOiI2NjRiMTMxMS0wNmE3LTRkMjAtOGFiNS1hYzRmMWMwMWU2YmUiLCJpYXQiOjE1OTQ5MDM5NDN9.D98H5RwiaLvsjkOy7OuG1OGbZL0zBg8hZVd6mzSX_0A";
    }
}