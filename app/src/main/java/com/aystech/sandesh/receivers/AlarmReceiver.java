package com.aystech.sandesh.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import com.aystech.sandesh.services.MyService;
import com.aystech.sandesh.utils.GPSTracker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String address;
        GPSTracker gpsTracker = new GPSTracker(context);

        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            if (latitude != 0.0 || longitude != 0.0) {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            } else {
                address = "No latitude & longitude found.";
            }
            Intent intent1 = new Intent(context, MyService.class);
            intent1.putExtra(MyService.LAT, String.valueOf(latitude));
            intent1.putExtra(MyService.LONG, String.valueOf(longitude));
            intent1.putExtra(MyService.ADDRESS, address);
            context.startService(intent1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
