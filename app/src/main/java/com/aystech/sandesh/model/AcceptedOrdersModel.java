package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AcceptedOrdersModel {

    @SerializedName("delivery_id")
    @Expose
    private Integer deliveryId;
    @SerializedName("requestor_type")
    @Expose
    private String requestorType;
    @SerializedName("travel_id")
    @Expose
    private Integer travelId;
    @SerializedName("c_to_c")
    @Expose
    private Double cToC;
    @SerializedName("c_to_d")
    @Expose
    private Integer cToD;
    @SerializedName("d_to_d")
    @Expose
    private Double dToD;
    @SerializedName("d_to_c")
    @Expose
    private Integer dToC;
    @SerializedName("parcel_id")
    @Expose
    private Integer parcelId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
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
    @SerializedName("packaging")
    @Expose
    private String packaging;
    @SerializedName("isProhibited")
    @Expose
    private String isProhibited;
    @SerializedName("isHazardous")
    @Expose
    private String isHazardous;
    @SerializedName("isFragile")
    @Expose
    private String isFragile;
    @SerializedName("isFlamable")
    @Expose
    private String isFlamable;
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
    @SerializedName("order_charges")
    @Expose
    private Integer orderCharges;
    @SerializedName("gst_amount")
    @Expose
    private Double gstAmount;
    @SerializedName("total_amount")
    @Expose
    private Double totalAmount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("verification_status")
    @Expose
    private String verificationStatus;
    @SerializedName("paymnet_status")
    @Expose
    private String paymnetStatus;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("company_name")
    @Expose
    private String company_name;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("email_id")
    @Expose
    private String emailId;
    @SerializedName("from_city")
    @Expose
    private String fromCity;
    @SerializedName("to_city")
    @Expose
    private String toCity;

    public Integer getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Integer deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getRequestorType() {
        return requestorType;
    }

    public void setRequestorType(String requestorType) {
        this.requestorType = requestorType;
    }

    public Integer getTravelId() {
        return travelId;
    }

    public void setTravelId(Integer travelId) {
        this.travelId = travelId;
    }

    public Double getCToC() {
        return cToC;
    }

    public void setCToC(Double cToC) {
        this.cToC = cToC;
    }

    public Integer getCToD() {
        return cToD;
    }

    public void setCToD(Integer cToD) {
        this.cToD = cToD;
    }

    public Double getDToD() {
        return dToD;
    }

    public void setDToD(Double dToD) {
        this.dToD = dToD;
    }

    public Integer getDToC() {
        return dToC;
    }

    public void setDToC(Integer dToC) {
        this.dToC = dToC;
    }

    public Integer getParcelId() {
        return parcelId;
    }

    public void setParcelId(Integer parcelId) {
        this.parcelId = parcelId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getIsHazardous() {
        return isHazardous;
    }

    public void setIsHazardous(String isHazardous) {
        this.isHazardous = isHazardous;
    }

    public String getIsFragile() {
        return isFragile;
    }

    public void setIsFragile(String isFragile) {
        this.isFragile = isFragile;
    }

    public String getIsFlamable() {
        return isFlamable;
    }

    public void setIsFlamable(String isFlamable) {
        this.isFlamable = isFlamable;
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

    public Integer getOrderCharges() {
        return orderCharges;
    }

    public void setOrderCharges(Integer orderCharges) {
        this.orderCharges = orderCharges;
    }

    public Double getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(Double gstAmount) {
        this.gstAmount = gstAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getPaymnetStatus() {
        return paymnetStatus;
    }

    public void setPaymnetStatus(String paymnetStatus) {
        this.paymnetStatus = paymnetStatus;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
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

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
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

    public void setEmailId(String emailId) {
        this.emailId = emailId;
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
}
