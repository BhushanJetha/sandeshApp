package com.aystech.sandesh.services;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

import com.aystech.sandesh.receivers.AlarmReceiver;

import java.util.Calendar;

public class OnTimeService extends IntentService {

    PendingIntent pendingIntent;
    AlarmManager alarmManager;

    public OnTimeService() {
        super("OnTimeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        onExecuteTime();
    }

    @SuppressLint("ShortAlarm")
    private void onExecuteTime() {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long time;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 0);
        Intent intent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
        if (System.currentTimeMillis() > time) {
            if (Calendar.AM_PM == 0)
                time = time + (1000 * 60 * 60 * 12);
            else
                time = time + (1000 * 60 * 60 * 24);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
    }
}
