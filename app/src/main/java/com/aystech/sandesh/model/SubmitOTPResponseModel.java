package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitOTPResponseModel extends KYCCommonModel{
    @SerializedName("data")
    @Expose
    private SubmitOTPModel data;

    public SubmitOTPModel getData() {
        return data;
    }

    public void setData(SubmitOTPModel data) {
        this.data = data;
    }
}
