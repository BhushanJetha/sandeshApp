package com.aystech.sandesh.utils;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.os.Build.VERSION.SDK_INT;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    NotificationManager notificationManager;
    String ADMIN_CHANNEL_ID = "Default";
    String defaultChannelName = "General";
    String getDefaultChannelDesc = "General Grease Monkey Notification";
    UserSession userSession;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        userSession = new UserSession(this);
        userSession.setFCMId(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        userSession = new UserSession(this);

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");

        Log.e("MyFirebaseMessaging", "onMessageReceived remoteMessage value--->" +
                remoteMessage.getData().toString());
        if (remoteMessage != null) {
            if (remoteMessage.getNotification() != null &&
                    remoteMessage.getNotification().getBody() != null) {
                message = remoteMessage.getNotification().getBody();
            } else {
                if (remoteMessage.getData() != null &&
                        remoteMessage.getData().get("title") != null) {
                    title = remoteMessage.getData().get("title");
                }
            }

            if (remoteMessage.getNotification() != null &&
                    remoteMessage.getNotification().getTitle() != null) {
                title = remoteMessage.getNotification().getTitle();
            } else {
                if (remoteMessage.getData() != null &&
                        remoteMessage.getData().get("message") != null) {
                    title = remoteMessage.getData().get("message");
                }
            }

            Log.e("MyFirebaseMessaging", "--->onMessageReceived title value--->" +
                    remoteMessage.getData().get("title"));
            Log.e("MyFirebaseMessaging", "--->onMessageReceived body value--->" +
                    remoteMessage.getData().get("body"));

            if (remoteMessage.getData().get("title") != null &&
                    remoteMessage.getData().get("body") != null)
                showNotification(
                        remoteMessage.getData().get("title"),
                        remoteMessage.getData().get("body")
                );
        }

        Log.e("MyFirebaseMessaging", "onMessageReceived title value--->" + title);
        Log.e("MyFirebaseMessaging", "onMessageReceived message value--->" + message);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // create a pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //when user app is open in that case below code will be run
        String channelId = "Default";
        long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                channelId)
                .setSmallIcon(R.drawable.ic_logo_sandesh)
                .setContentTitle(title)
                .setContentText(message)
                .setVibrate(pattern)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(uri);

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    "Default channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0, builder.build());
    }

    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // create a pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_logo_sandesh);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setAutoCancel(true);
        builder.setSound(uri);
        builder.setContentIntent(pendingIntent);
        builder.setVibrate(pattern);

        if (SDK_INT >= Build.VERSION_CODES.O) {
            setUpNotificationChannels();
        }
        notificationManager.notify(0, builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setUpNotificationChannels() {
        @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(ADMIN_CHANNEL_ID,
                defaultChannelName,
                NotificationManager.IMPORTANCE_MAX);

        notificationChannel.setDescription(getDefaultChannelDesc);
        notificationChannel.enableLights(true);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.enableVibration(true);
        notificationManager.createNotificationChannel(notificationChannel);
    }
}
