package com.aystech.sandesh.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {

    String PREFER_NAME = "userdata_Sandesh";
    int PRIVATE_MODE = 0;

    private String USER_ID = "user_id";
    private String USER_NAME = "user_name";
    private String USER_MOBILE = "user_mobile";
    private String USER_EMAIL = "user_email";
    private String USER_TYPE = "user_type";
    private String KEY_JWT_TOKEN = "jwtToken";
    private String KEY_FCM_ID = "fcm_id";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public UserSession(Context context) {
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setUserId(String userId) {
        editor.putString(USER_ID, userId);
        editor.commit();
    }

    public String getUSER_ID() {
        return pref.getString(USER_ID, "");
    }

    public void setUserName(String userName) {
        editor.putString(USER_NAME, userName);
        editor.commit();
    }

    public String getUSER_NAME() {
        return pref.getString(USER_NAME, "");
    }

    public void setUserMobile(String mobile) {
        editor.putString(USER_MOBILE, mobile);
        editor.commit();
    }

    public String getUSER_MOBILE() {
        return pref.getString(USER_MOBILE, "");
    }

    public void setUserEmail(String email) {
        editor.putString(USER_EMAIL, email);
        editor.commit();
    }

    public String getUSER_EMAIL() {
        return pref.getString(USER_EMAIL, "");
    }

    public void setUserType(String userType) {
        editor.putString(USER_TYPE, userType);
        editor.commit();
    }

    public String getUSER_TYPE() {
        return pref.getString(USER_TYPE, "");
    }

    public String getJWTToken() {
        return pref.getString(KEY_JWT_TOKEN, "");
    }

    public void setJWTToken(String token) {
        editor.putString(KEY_JWT_TOKEN, token);
        editor.commit();
    }


    void clearUserSession() {
        setUserId(null);
        setUserType(null);
        setUserEmail(null);
        setUserMobile(null);
        setJWTToken(null);
    }
}
