package com.aystech.sandesh.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {

    String PREFER_NAME = "userdata_Sandesh";
    int PRIVATE_MODE = 0;

    private final String USER_ID = "user_id";
    private final String USER_NAME = "user_name";
    private final String USER_MOBILE = "user_mobile";
    private final String USER_EMAIL = "user_email";
    private final String USER_TYPE = "user_type";
    private final String KEY_JWT_TOKEN = "jwtToken";
    private final String KEY_FCM_ID = "fcm_id";
    private final String IS_FIRST_USER = "first_time_user";
    private final String USER_PREVIOUS_ONLINE_DATE_TIME = "user_previous_login_date_time";
    private final String RESET_PASSWORD = "reset_password";

    private final String TRAVEL_ID = "travel_id";

    private final String HOURS = "hours";
    private final String MINUTE = "minute";

    private final String LOGIN_COUNT = "login_count";

    private final String FROM_STATE = "from_state";
    private final String WEIGHT = "weight";
    private final String DELIVERY_OPTION = "delivery_option";
    private final String VEHICLE_TYPE = "vehicle_type";
    private final String NATURE_OF_GOODS = "nature_of_goods";
    private final String QUALITY = "quality";
    private final String PACKAGING = "packaging";

    private final String REFERRAL_CODE = "referral_code";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

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

    public void setTravelId(int travel_id) {
        editor.putString(TRAVEL_ID, String.valueOf(travel_id));
        editor.commit();
    }

    public String getTRAVEL_ID() {
        return pref.getString(TRAVEL_ID, "");
    }

    public void setHours(int hours) {
        editor.putString(HOURS, String.valueOf(hours));
        editor.commit();
    }

    public String getHOURS() {
        return pref.getString(HOURS, "");
    }

    public void setMinute(int minute) {
        editor.putString(MINUTE, String.valueOf(minute));
        editor.commit();
    }

    public String getMINUTE() {
        return pref.getString(MINUTE, "");
    }

    public void setFCMId(String s) {
        editor.putString(KEY_FCM_ID, s);
        editor.commit();
    }

    public String getFCMId() {
        return pref.getString(KEY_FCM_ID, "");
    }

    public void setLoginCount(int login_count) {
        editor.putString(LOGIN_COUNT, String.valueOf(login_count));
        editor.commit();
    }

    public String getLoginCount() {
        return pref.getString(LOGIN_COUNT, "");
    }

    public void setFromState(String state_list) {
        editor.putString(FROM_STATE, state_list);
        editor.commit();
    }

    public String getFromState() {
        return pref.getString(FROM_STATE, "");
    }

    public void setWeight(String weight_list) {
        editor.putString(WEIGHT, weight_list);
        editor.commit();
    }

    public String getWeight() {
        return pref.getString(WEIGHT, "");
    }

    public void setDeliveryOption(String delivery_option) {
        editor.putString(DELIVERY_OPTION, delivery_option);
        editor.commit();
    }

    public String getDeliveryOption() {
        return pref.getString(DELIVERY_OPTION, "");
    }

    public void setVehicleType(String vehicle_type) {
        editor.putString(VEHICLE_TYPE, vehicle_type);
        editor.commit();
    }

    public String getVehicleType() {
        return pref.getString(VEHICLE_TYPE, "");
    }

    public void setNatureOfGoods(String nature_of_goods) {
        editor.putString(NATURE_OF_GOODS, nature_of_goods);
        editor.commit();
    }

    public String getNatureOfGoods() {
        return pref.getString(NATURE_OF_GOODS, "");
    }

    public void setQuality(String quality) {
        editor.putString(QUALITY, quality);
        editor.commit();
    }

    public String getQuality(){
        return pref.getString(QUALITY, "");
    }

    public void setPackaging(String quality) {
        editor.putString(PACKAGING, quality);
        editor.commit();
    }

    public String getPackaging(){
        return pref.getString(PACKAGING, "");
    }

    public String getFirstTimeUserStatus() {
        return pref.getString(IS_FIRST_USER, "");
    }

    public void setFirstTimeUserStatus(String loginStatus) {
        editor.putString(IS_FIRST_USER, loginStatus);
        editor.commit();
    }

    public String getPreviousOnlineDateTime() {
        return pref.getString(USER_PREVIOUS_ONLINE_DATE_TIME, "");
    }

    public void setPreviousOnlineDateTime(String dateTime) {
        editor.putString(USER_PREVIOUS_ONLINE_DATE_TIME, dateTime);
        editor.commit();
    }

    public String getResetPasswordStatus() {
        return pref.getString(RESET_PASSWORD, "");
    }

    public void setResetPasswordStatus(String resetPasswordStatus) {
        editor.putString(RESET_PASSWORD, resetPasswordStatus);
        editor.commit();
    }

    public void setReferralCode(String referral_code) {
        editor.putString(REFERRAL_CODE, referral_code);
        editor.commit();
    }

    public String getReferralCode(){
        return pref.getString(REFERRAL_CODE, "");
    }

    void clearUserSession() {
        setUserId(null);
        setUserType(null);
        setUserEmail(null);
        setUserMobile(null);
        setJWTToken(null);
    }

    public void logout() {
        setUserId(null);
    }
}
