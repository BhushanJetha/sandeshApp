package com.aystech.sandesh.remote;

import com.aystech.sandesh.model.AcceptedOrdersResponseModel;
import com.aystech.sandesh.model.CityResponseModel;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.DeliveryOptionResponseModel;
import com.aystech.sandesh.model.LoginResponseModel;
import com.aystech.sandesh.model.MyOrdersResponseModel;
import com.aystech.sandesh.model.MyRidesResponseModel;
import com.aystech.sandesh.model.NatureOfGoodsResponseModel;
import com.aystech.sandesh.model.OrderDetailResponseModel;
import com.aystech.sandesh.model.PackagingResponseModel;
import com.aystech.sandesh.model.ProfileResponseModel;
import com.aystech.sandesh.model.QualityResponseModel;
import com.aystech.sandesh.model.SearchOrderResponseModel;
import com.aystech.sandesh.model.SearchTravellerResponseModel;
import com.aystech.sandesh.model.ShowHistoryResponseModel;
import com.aystech.sandesh.model.StateResponseModel;
import com.aystech.sandesh.model.TravelDetailResponseModel;
import com.aystech.sandesh.model.VehicleResponseModel;
import com.aystech.sandesh.model.WalletTransactionResponseModel;
import com.aystech.sandesh.model.WeightResponseModel;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @POST("api/login")
    Call<LoginResponseModel> doLogin(@Body JsonObject jsonObject);

    @Multipart
    @POST("api/registerIndividualUser")
    Call<CommonResponse> doIndividualUserRegistration(
            @Part("email_id") RequestBody emailId,
            @Part("mobile_no") RequestBody mobileNo,
            @Part("first_name") RequestBody fristName,
            @Part("middle_name") RequestBody middleName,
            @Part("last_name") RequestBody lastName,
            @Part("password") RequestBody password,
            @Part("gender") RequestBody gender,
            @Part("birth_date") RequestBody birthDate,
            @Part("refferal_code") RequestBody refferalCode,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("api/registerCoporateUser")
    Call<CommonResponse> doCorporateUserRegistration(
            @Part("email_id") RequestBody emailId,
            @Part("mobile_no") RequestBody mobileNo,
            @Part("auth_mobile_no") RequestBody authMobileNo,
            @Part("password") RequestBody password,
            @Part("refferal_code") RequestBody refferalCode,
            @Part("company_name") RequestBody companyName,
            @Part("branch") RequestBody branch,
            @Part("auth_person_name") RequestBody authPersonName,
            @Part("designation") RequestBody designation,
            @Part MultipartBody.Part image
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

    @Multipart
    @POST("api/sendParcel")
    Call<CommonResponse> sendParcel(
            @Part("from_city_id") RequestBody from_city_id,
            @Part("from_pincode") RequestBody from_pincode,
            @Part("to_city_id") RequestBody to_city_id,
            @Part("to_pincode") RequestBody to_pincode,
            @Part("start_date") RequestBody start_date,
            @Part("start_time") RequestBody start_time,
            @Part("end_date") RequestBody end_date,
            @Part("end_time") RequestBody end_time,
            @Part("delivery_option") RequestBody delivery_option,
            @Part("nature_of_goods") RequestBody nature_of_goods,
            @Part("good_description") RequestBody good_description,
            @Part("quality") RequestBody quality,
            @Part("weight_id") RequestBody weight_id,
            @Part("packaging") RequestBody packaging,
            @Part("isHazardous") RequestBody isHazardous,
            @Part("isProhibited") RequestBody isProhibited,
            @Part("isFragile") RequestBody isFragile,
            @Part("isFlamable") RequestBody isFlamable,
            @Part("value_of_goods") RequestBody value_of_goods,
            @Part("ownership") RequestBody ownership,
            @Part MultipartBody.Part invoice_pic,
            @Part MultipartBody.Part parcel_pic,
            @Part("receiver_name") RequestBody receiver_name,
            @Part("receiver_mobile_no") RequestBody receiver_mobile_no,
            @Part("receiver_address_detail") RequestBody receiver_address_detail
    );

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

    @Multipart
    @POST("api/updateProfile")
    Call<CommonResponse> updateCompanyProfile(
            @Part("email_id") RequestBody email_id,
            @Part("company_name") RequestBody company_name,
            @Part("branch") RequestBody branch,
            @Part("auth_person_name") RequestBody auth_person_name,
            @Part("designation") RequestBody designation,
            @Part MultipartBody.Part image
    );

    @Multipart
    @POST("api/updateProfile")
    Call<CommonResponse> updateUserProfile(
            @Part("email_id") RequestBody email_id,
            @Part("first_name") RequestBody first_name,
            @Part("middle_name") RequestBody middle_name,
            @Part("last_name") RequestBody last_name,
            @Part("gender") RequestBody gender,
            @Part("birth_date") RequestBody birth_date,
            @Part MultipartBody.Part image
    );

    @POST("api/getOrderDetail")
    Call<OrderDetailResponseModel> orderDetail(@Body JsonObject jsonObject);

    @GET("api/getWeight")
    Call<WeightResponseModel> getWeights();

    @GET("api/getDeliveryOption")
    Call<DeliveryOptionResponseModel> getDeliveryOption();

    @GET("api/getNatureOfGoods")
    Call<NatureOfGoodsResponseModel> getNatureOfGoods();

    @GET("api/getPackaging")
    Call<PackagingResponseModel> getPackaging();

    @GET("api/getQuality")
    Call<QualityResponseModel> getQuality();

    @GET("api/getVehicleType")
    Call<VehicleResponseModel> getVehicleType();

    @POST("api/getTraveDetail")
    Call<TravelDetailResponseModel> travelDetail(@Body JsonObject jsonObject);

    @GET("api/getMyTravelList")
    Call<SearchTravellerResponseModel> getMyTravellerList();

    @GET("api/getMyOrderList")
    Call<SearchOrderResponseModel> getMyOrderList();

    @POST("api/sendDeliveryRequest")
    Call<CommonResponse> sendDeliveryRequest(@Body JsonObject jsonObject);

    @POST("api/getMyAcceptedOrders")
    Call<AcceptedOrdersResponseModel> getMyAcceptedOrders(@Body JsonObject jsonObject);

    @POST("api/sendOTP")
    Call<CommonResponse> sendOTP(@Body JsonObject jsonObject);

    @POST("api/verifyOrderOTP")
    Call<CommonResponse> verifyOTP(@Body JsonObject jsonObject);

    @POST("api/sendVerificationStatus")
    Call<CommonResponse> sendVerificationStatus(@Body JsonObject jsonObject);

    @GET("api/getMyStartedJourney")
    Call<SearchTravellerResponseModel> getMyStartedJourney();

    @Multipart
    @POST("api/endJourney")
    Call<CommonResponse> endJourney(
            @Part("travel_id") RequestBody travel_id,
            @Part("parcel_id") RequestBody parcel_id,
            @Part("delivery_id") RequestBody delivery_id,
            @Part MultipartBody.Part parcel_picture,
            @Part MultipartBody.Part selfie_picture
    );

    @POST("api/addBalance")
    Call<CommonResponse> verifyPaymentDetail(@Body JsonObject jsonObject);

    @GET("api/getMyTransactionList")
    Call<WalletTransactionResponseModel> getMyTransactionList();

    @POST("api/getMyOrderRequestList")
    Call<SearchOrderResponseModel> myRequestedOrders(@Body JsonObject jsonObject);

    @POST("api/sendOrderRequestStatus")
    Call<CommonResponse> sendOrderRequestStatus(@Body JsonObject jsonObject);

    @POST("api/sendComplaint")
    Call<CommonResponse> sendComplaint(@Body JsonObject jsonObject);

    @POST("api/sendCurrentLocation")
    Call<CommonResponse> sendCurrentLocation(@Body JsonObject jsonObject);

    @POST("api/trackParcel")
    Call<OrderDetailResponseModel> trackParcel(@Body JsonObject jsonObject);

    @POST("api/getMyPreviousOrderHistory")
    Call<MyRidesResponseModel> getMyRidesHistory(@Body JsonObject jsonObject);

    @POST("api/getMyPreviousOrderHistory")
    Call<MyOrdersResponseModel> getMyOrdersHistory(@Body JsonObject jsonObject);

    @Multipart
    @POST("api/updateMyParcel")
    Call<CommonResponse> updateMyParcel(
            @Part("parcel_id") RequestBody parcel_id,
            @Part("from_city_id") RequestBody from_city_id,
            @Part("from_pincode") RequestBody from_pincode,
            @Part("to_city_id") RequestBody to_city_id,
            @Part("to_pincode") RequestBody to_pincode,
            @Part("start_date") RequestBody start_date,
            @Part("start_time") RequestBody start_time,
            @Part("end_date") RequestBody end_date,
            @Part("end_time") RequestBody end_time,
            @Part("delivery_option") RequestBody delivery_option,
            @Part("nature_of_goods") RequestBody nature_of_goods,
            @Part("good_description") RequestBody good_description,
            @Part("quality") RequestBody quality,
            @Part("weight_id") RequestBody weight_id,
            @Part("packaging") RequestBody packaging,
            @Part("isHazardous") RequestBody isHazardous,
            @Part("isProhibited") RequestBody isProhibited,
            @Part("isFragile") RequestBody isFragile,
            @Part("isFlamable") RequestBody isFlamable,
            @Part("value_of_goods") RequestBody value_of_goods,
            @Part("ownership") RequestBody ownership,
            @Part MultipartBody.Part invoice_pic,
            @Part MultipartBody.Part parcel_pic,
            @Part("receiver_name") RequestBody receiver_name,
            @Part("receiver_mobile_no") RequestBody receiver_mobile_no,
            @Part("receiver_address_detail") RequestBody receiver_address_detail
    );

    @POST("api/updateMyTravel")
    Call<CommonResponse> updateMyTravel(@Body JsonObject jsonObject);

    @Multipart
    @POST("api/nominateAlternatePerson")
    Call<CommonResponse> nominateAlternatePerson(
            @Part("travel_id") RequestBody travel_id,
            @Part("user_name") RequestBody user_name,
            @Part("mobile_no") RequestBody mobile_no,
            @Part("pancard_no") RequestBody pancard_no,
            @Part MultipartBody.Part pancard_pic
    );
}
