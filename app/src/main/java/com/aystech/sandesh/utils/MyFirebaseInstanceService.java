package com.aystech.sandesh.utils;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by dell on 12/28/2019.
 */

public class MyFirebaseInstanceService extends FirebaseInstanceIdService {
    private static final String TAG="MyFirebaseInstanceServi";
    UserSession userSession;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //FirebaseMessaging.getInstance().subscribeToTopic("all");
        Log.d(TAG, "Refreshed token:--> " + refreshedToken);

        /* If you want to send messages to this application instance or manage this apps subscriptions
        on the server side, send the Instance ID token to your app server.*/

        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        Log.d("TOKEN --->", refreshedToken);

        if(refreshedToken != null & !refreshedToken.isEmpty()){
            userSession = new UserSession(this);
            userSession.setFCMId(refreshedToken);
        }
    }
}
