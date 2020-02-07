package com.aystech.sandesh.remote;

import com.aystech.sandesh.model.CityResponseModel;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.LoginResponseModel;
import com.aystech.sandesh.model.ProfileResponseModel;
import com.aystech.sandesh.model.SearchOrderResponseModel;
import com.aystech.sandesh.model.SearchTravellerResponseModel;
import com.aystech.sandesh.model.ShowHistoryResponseModel;
import com.aystech.sandesh.model.StateResponseModel;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("api/login")
    Call<LoginResponseModel> doLogin(@Body JsonObject jsonObject);

    @FormUrlEncoded
    @POST("api/registerIndividualUser")
    Call<CommonResponse> doIndividualUserRegistration(
            @Field("email_id") String emailId,
            @Field("mobile_no") String mobileNo,
            @Field("first_name") String fristName,
            @Field("middle_name") String middleName,
            @Field("last_name") String lastName,
            @Field("password") String password,
            @Field("gender") String gender,
            @Field("birth_date") String birthDate,
            @Field("refferal_code") String refferalCode,
            @Field("file") String file,
            @Field("fcm_id") String fcmId
    );

    @FormUrlEncoded
    @POST("api/registerCoporateUser")
    Call<CommonResponse> doCorporateUserRegistration(
            @Field("email_id") String emailId,
            @Field("mobile_no") String mobileNo,
            @Field("password") String password,
            @Field("gender") String gender,
            @Field("birth_date") String birthDate,
            @Field("refferal_code") String refferalCode,
            @Field("file") String file,
            @Field("fcm_id") String fcmId,
            @Field("company_name") String companyName,
            @Field("branch") String branch,
            @Field("auth_person_name") String authPersonName,
            @Field("designation") String designation
    );

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
    Call<SearchTravellerResponseModel> searchTraveller(@Body JsonObject jsonObject);

    @GET("api/showHistory")
    Call<ShowHistoryResponseModel> showHistory();

    @GET("api/getProfile")
    Call<ProfileResponseModel> getProfile();

    @POST("api/registerUserAddress")
    Call<CommonResponse> updateAddress(@Body JsonObject jsonObject);

    @FormUrlEncoded
    @POST("api/updateProfile")
    Call<CommonResponse> updateCompanyProfile(
            @Field("email_id") String email_id,
            @Field("company_name") String company_name,
            @Field("branch") String branch,
            @Field("auth_person_name") String auth_person_name,
            @Field("designation") String designation,
            @Field("file") String file);

    @FormUrlEncoded
    @POST("api/updateProfile")
    Call<CommonResponse> updateUserProfile(
            @Field("email_id") String email_id,
            @Field("first_name") String first_name,
            @Field("middle_name") String middle_name,
            @Field("last_name") String last_name,
            @Field("gender") String gender,
            @Field("birth_date") String birth_date,
            @Field("file") String file);
}
