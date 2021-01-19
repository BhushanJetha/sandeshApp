package com.aystech.sandesh.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.aystech.sandesh.activity.NoInternetActivity;

@SuppressWarnings("ALL")
public class Connectivity {

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        boolean isConnected = info != null &&
                info.isConnectedOrConnecting();
        if (isConnected != true) {
            Intent intent = new Intent(context, NoInternetActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);

            return false;
        }
        return true;
    }

}
