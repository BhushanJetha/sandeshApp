package com.aystech.sandesh.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aystech.sandesh.services.MyService;
import com.aystech.sandesh.utils.GPSTracker;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            GPSTracker gpsTracker = new GPSTracker(context);

            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            Intent intent1 = new Intent(context, MyService.class);
            intent1.putExtra(MyService.LAT, String.valueOf(latitude));
            intent1.putExtra(MyService.LONG, String.valueOf(longitude));
            context.startService(intent1);
        }
    }
}
