package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchTravellerModel {
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
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("delivery_option")
    @Expose
    private String deliveryOption;
    @SerializedName("preferred_weight")
    @Expose
    private String preferredWeight;
    @SerializedName("acceptable_volume_length")
    @Expose
    private String acceptableVolumeLength;
    @SerializedName("acceptable_volume_width")
    @Expose
    private String acceptableVolumeWidth;
    @SerializedName("acceptable_volume_breadth")
    @Expose
    private String acceptableVolumeBreadth;
    @SerializedName("mode_of_travel")
    @Expose
    private String modeOfTravel;
    @SerializedName("vehicle_train_number")
    @Expose
    private String vehicleTrainNumber;
    @SerializedName("other_detail")
    @Expose
    private String otherDetail;

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

    public String getAcceptableVolumeWidth() {
        return acceptableVolumeWidth;
    }

    public void setAcceptableVolumeWidth(String acceptableVolumeWidth) {
        this.acceptableVolumeWidth = acceptableVolumeWidth;
    }

    public String getAcceptableVolumeBreadth() {
        return acceptableVolumeBreadth;
    }

    public void setAcceptableVolumeBreadth(String acceptableVolumeBreadth) {
        this.acceptableVolumeBreadth = acceptableVolumeBreadth;
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
}
