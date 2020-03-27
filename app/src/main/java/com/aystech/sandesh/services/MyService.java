package com.aystech.sandesh.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;

public class MyService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String LAT = "lat";
    public static final String LONG = "long";
    public static final String ADDRESS = "address";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.aystech.sandesh.services";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        // Gets data from the incoming Intent
        //server call here
    }

    private void publishResults(int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}
