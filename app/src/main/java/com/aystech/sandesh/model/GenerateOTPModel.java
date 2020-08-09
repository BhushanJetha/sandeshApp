package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateOTPModel {
    @SerializedName("valid_aadhaar")
    @Expose
    private Boolean validAadhaar;
    @SerializedName("otp_sent")
    @Expose
    private Boolean otpSent;
    @SerializedName("if_number")
    @Expose
    private Boolean ifNumber;
    @SerializedName("client_id")
    @Expose
    private String clientId;

    public Boolean getValidAadhaar() {
        return validAadhaar;
    }

    public void setValidAadhaar(Boolean validAadhaar) {
        this.validAadhaar = validAadhaar;
    }

    public Boolean getOtpSent() {
        return otpSent;
    }

    public void setOtpSent(Boolean otpSent) {
        this.otpSent = otpSent;
    }

    public Boolean getIfNumber() {
        return ifNumber;
    }

    public void setIfNumber(Boolean ifNumber) {
        this.ifNumber = ifNumber;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
