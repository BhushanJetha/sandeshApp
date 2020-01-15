package com.aystech.sandesh.remote;

import com.aystech.sandesh.model.CommonResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("api name")
    Call<CommonResponse> doLogin(@Body JsonObject jsonObject);
}
