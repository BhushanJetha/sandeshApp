package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileResponseModel extends CommonResponse{
    @SerializedName("data")
    @Expose
    private ProfileInnerModel data;

    public ProfileInnerModel getData() {
        return data;
    }
}
