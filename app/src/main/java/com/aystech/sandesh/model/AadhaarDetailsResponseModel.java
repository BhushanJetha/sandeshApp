package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AadhaarDetailsResponseModel extends CommonResponse{
    @SerializedName("data")
    @Expose
    private AadhaarDetailsModel data;

    public AadhaarDetailsModel getData() {
        return data;
    }

    public void setData(AadhaarDetailsModel data) {
        this.data = data;
    }

}
