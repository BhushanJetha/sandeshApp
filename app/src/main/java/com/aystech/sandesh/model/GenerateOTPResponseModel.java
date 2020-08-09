package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GenerateOTPResponseModel extends KYCCommonModel {
    @SerializedName("data")
    @Expose
    private GenerateOTPModel data;

    public GenerateOTPModel getData() {
        return data;
    }

    public void setData(GenerateOTPModel data) {
        this.data = data;
    }
}
