package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchOrderModel {
    @SerializedName("parcel_id")
    @Expose
    private Integer parcelId;
    @SerializedName("estimate_amount")
    @Expose
    private Integer estimate_amount;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("middle_name")
    @Expose
    private String middleName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("from_pincode")
    @Expose
    private String fromPincode;
    @SerializedName("from_address_detail")
    @Expose
    private String fromAddressDetail;
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
    @SerializedName("nature_of_goods")
    @Expose
    private String natureOfGoods;
    @SerializedName("good_description")
    @Expose
    private String goodDescription;
    @SerializedName("quality")
    @Expose
    private String quality;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("packaging")
    @Expose
    private String packaging;
    @SerializedName("isProhibited")
    @Expose
    private String isProhibited;
    @SerializedName("value_of_goods")
    @Expose
    private String valueOfGoods;
    @SerializedName("ownership")
    @Expose
    private String ownership;
    @SerializedName("invoice_pic")
    @Expose
    private String invoicePic;
    @SerializedName("parcel_pic")
    @Expose
    private String parcelPic;
    @SerializedName("receiver_name")
    @Expose
    private String receiverName;
    @SerializedName("receiver_mobile_no")
    @Expose
    private String receiverMobileNo;
    @SerializedName("receiver_address_detail")
    @Expose
    private String receiverAddressDetail;
    @SerializedName("status")
    @Expose
    private String status;

    public Integer getParcelId() {
        return parcelId;
    }

    public void setParcelId(Integer parcelId) {
        this.parcelId = parcelId;
    }

    public Integer getEstimate_amount() {
        return estimate_amount;
    }

    public void setEstimate_amount(Integer estimate_amount) {
        this.estimate_amount = estimate_amount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFromPincode() {
        return fromPincode;
    }

    public void setFromPincode(String fromPincode) {
        this.fromPincode = fromPincode;
    }

    public String getFromAddressDetail() {
        return fromAddressDetail;
    }

    public void setFromAddressDetail(String fromAddressDetail) {
        this.fromAddressDetail = fromAddressDetail;
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

    public String getNatureOfGoods() {
        return natureOfGoods;
    }

    public void setNatureOfGoods(String natureOfGoods) {
        this.natureOfGoods = natureOfGoods;
    }

    public String getGoodDescription() {
        return goodDescription;
    }

    public void setGoodDescription(String goodDescription) {
        this.goodDescription = goodDescription;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public String getIsProhibited() {
        return isProhibited;
    }

    public void setIsProhibited(String isProhibited) {
        this.isProhibited = isProhibited;
    }

    public String getValueOfGoods() {
        return valueOfGoods;
    }

    public void setValueOfGoods(String valueOfGoods) {
        this.valueOfGoods = valueOfGoods;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public String getInvoicePic() {
        return invoicePic;
    }

    public void setInvoicePic(String invoicePic) {
        this.invoicePic = invoicePic;
    }

    public String getParcelPic() {
        return parcelPic;
    }

    public void setParcelPic(String parcelPic) {
        this.parcelPic = parcelPic;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobileNo() {
        return receiverMobileNo;
    }

    public void setReceiverMobileNo(String receiverMobileNo) {
        this.receiverMobileNo = receiverMobileNo;
    }

    public String getReceiverAddressDetail() {
        return receiverAddressDetail;
    }

    public void setReceiverAddressDetail(String receiverAddressDetail) {
        this.receiverAddressDetail = receiverAddressDetail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
