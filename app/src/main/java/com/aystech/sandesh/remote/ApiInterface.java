package com.aystech.sandesh.remote;

import com.aystech.sandesh.model.CommonResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("api/login")
    Call<CommonResponse> doLogin(@Body JsonObject jsonObject);

    @POST("api/registerIndividualUser")
    Call<CommonResponse> doIndividualUserRegistration(@Body JsonObject jsonObject);

    @POST("api/registerCoporateUser")
    Call<CommonResponse> doCorporateUserRegistration(@Body JsonObject jsonObject);

    @POST("api/registerUserAddress")
    Call<CommonResponse> doAddressRegistration(@Body JsonObject jsonObject);

    @GET("api/getState")
    Call<CommonResponse> getState();

    @POST("api/getCity")
    Call<CommonResponse> getCity(@Body JsonObject jsonObject);

    @POST("api/getOTP")
    Call<CommonResponse> getOTP(@Body JsonObject jsonObject);

    @POST("api/verifyOTP")
    Call<CommonResponse> doOTPVerification(@Body JsonObject jsonObject);

    @POST("api/forgotPassword")
    Call<CommonResponse> forgotPassword(@Body JsonObject jsonObject);


}
