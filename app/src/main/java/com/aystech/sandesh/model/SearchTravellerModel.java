package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchTravellerModel {
    @SerializedName("travel_id")
    @Expose
    private Integer travelId;
    @SerializedName("estimate_amount")
    @Expose
    private Integer estimate_amount;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("from_city_id")
    @Expose
    private Integer fromCityId;
    @SerializedName("from_pincode")
    @Expose
    private String fromPincode;
    @SerializedName("to_city_id")
    @Expose
    private Integer toCityId;
    @SerializedName("to_pincode")
    @Expose
    private String toPincode;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("delivery_option")
    @Expose
    private String deliveryOption;
    @SerializedName("preferred_weight")
    @Expose
    private String preferredWeight;
    @SerializedName(value = "acceptable_volume_length", alternate = "length")
    @Expose
    private String acceptableVolumeLength;
    @SerializedName(value = "acceptable_volume_breadth", alternate = "breadth")
    @Expose
    private String acceptableVolumeBreadth;
    @SerializedName(value = "acceptable_volume_width", alternate = "width")
    @Expose
    private String acceptableVolumeWidth;
    @SerializedName("mode_of_travel")
    @Expose
    private String modeOfTravel;
    @SerializedName(value = "vehicle_train_number", alternate = "vehicle_number")
    @Expose
    private String vehicleTrainNumber;
    @SerializedName("other_detail")
    @Expose
    private String otherDetail;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("updated_date")
    @Expose
    private String updatedDate;
    @SerializedName("from_city")
    @Expose
    private String fromCity;
    @SerializedName("to_city")
    @Expose
    private String toCity;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("email_id")
    @Expose
    private String emailId;

    public Integer getTravelId() {
        return travelId;
    }

    public void setTravelId(Integer travelId) {
        this.travelId = travelId;
    }

    public Integer getEstimate_amount() {
        return estimate_amount;
    }

    public void setEstimate_amount(Integer estimate_amount) {
        this.estimate_amount = estimate_amount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getFromCityId() {
        return fromCityId;
    }

    public void setFromCityId(Integer fromCityId) {
        this.fromCityId = fromCityId;
    }

    public String getFromPincode() {
        return fromPincode;
    }

    public void setFromPincode(String fromPincode) {
        this.fromPincode = fromPincode;
    }

    public Integer getToCityId() {
        return toCityId;
    }

    public void setToCityId(Integer toCityId) {
        this.toCityId = toCityId;
    }

    public String getToPincode() {
        return toPincode;
    }

    public void setToPincode(String toPincode) {
        this.toPincode = toPincode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(String deliveryOption) {
        this.deliveryOption = deliveryOption;
    }

    public String getPreferredWeight() {
        return preferredWeight;
    }

    public void setPreferredWeight(String preferredWeight) {
        this.preferredWeight = preferredWeight;
    }

    public String getAcceptableVolumeLength() {
        return acceptableVolumeLength;
    }

    public void setAcceptableVolumeLength(String acceptableVolumeLength) {
        this.acceptableVolumeLength = acceptableVolumeLength;
    }

    public String getAcceptableVolumeBreadth() {
        return acceptableVolumeBreadth;
    }

    public void setAcceptableVolumeBreadth(String acceptableVolumeBreadth) {
        this.acceptableVolumeBreadth = acceptableVolumeBreadth;
    }

    public String getAcceptableVolumeWidth() {
        return acceptableVolumeWidth;
    }

    public void setAcceptableVolumeWidth(String acceptableVolumeWidth) {
        this.acceptableVolumeWidth = acceptableVolumeWidth;
    }

    public String getModeOfTravel() {
        return modeOfTravel;
    }

    public void setModeOfTravel(String modeOfTravel) {
        this.modeOfTravel = modeOfTravel;
    }

    public String getVehicleTrainNumber() {
        return vehicleTrainNumber;
    }

    public void setVehicleTrainNumber(String vehicleTrainNumber) {
        this.vehicleTrainNumber = vehicleTrainNumber;
    }

    public String getOtherDetail() {
        return otherDetail;
    }

    public void setOtherDetail(String otherDetail) {
        this.otherDetail = otherDetail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }
}
