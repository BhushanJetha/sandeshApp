package com.aystech.sandesh.remote;

import com.aystech.sandesh.model.CityResponseModel;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.LoginResponseModel;
import com.aystech.sandesh.model.SearchOrderResponseModel;
import com.aystech.sandesh.model.StateResponseModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("api/login")
    Call<LoginResponseModel> doLogin(@Body JsonObject jsonObject);

    @POST("api/registerIndividualUser")
    Call<CommonResponse> doIndividualUserRegistration(@Body JsonObject jsonObject);

    @POST("api/registerCoporateUser")
    Call<CommonResponse> doCorporateUserRegistration(@Body JsonObject jsonObject);

    @POST("api/registerUserAddress")
    Call<CommonResponse> doAddressRegistration(@Body JsonObject jsonObject);

    @GET("api/getState")
    Call<StateResponseModel> getState();

    @POST("api/getCity")
    Call<CityResponseModel> getCity(@Body JsonObject jsonObject);

    @POST("api/getOTP")
    Call<CommonResponse> getOTP(@Body JsonObject jsonObject);

    @POST("api/verifyOTP")
    Call<CommonResponse> doOTPVerification(@Body JsonObject jsonObject);

    @POST("api/forgotPassword")
    Call<CommonResponse> forgotPassword(@Body JsonObject jsonObject);

    @POST("api/planTravel")
    Call<CommonResponse> planTravel(@Body JsonObject jsonObject);

    @POST("api/sendParcel")
    Call<CommonResponse> sendParcel(@Body JsonObject jsonObject);

    @POST("api/searchOrder")
    Call<SearchOrderResponseModel> searchOrder(@Body JsonObject jsonObject);

    @POST("api/searchTraveller")
    Call<SearchOrderResponseModel> searchTraveller(@Body JsonObject jsonObject);

    @GET("api/showHistory")
    Call<CommonResponse> showHistory(@Body JsonObject jsonObject);
}
