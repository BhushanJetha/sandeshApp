package com.aystech.sandesh.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.UserSession;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String LAT = "lat";
    public static final String LONG = "long";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.aystech.sandesh.services";
    private UserSession userSession;

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        // Gets data from the incoming Intent

        userSession = new UserSession(this);

        if (Double.parseDouble(intent.getStringExtra(LAT)) != 0.0 &&
                Double.parseDouble(intent.getStringExtra(LONG)) != 0.0) {

            //TODO API Call
            sendCurrentLocation(userSession.getTRAVEL_ID(), intent.getStringExtra(LAT), intent.getStringExtra(LONG));
        }
    }

    private void sendCurrentLocation(String travel_id, String latitude, String longitude) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("travel_id", travel_id);
        jsonObject.addProperty("lat", latitude);
        jsonObject.addProperty("long", longitude);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.sendCurrentLocation(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        // successfully finished
                        result = Activity.RESULT_OK;

                        publishResults(result);
                    } else {
                        Toast.makeText(MyService.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
            }
        });
    }

    private void publishResults(int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}
