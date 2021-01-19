package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailResponseModel extends CommonResponse {

    @SerializedName("data")
    @Expose
    private final TravelDetailModel data = null;

    public TravelDetailModel getData() {
        return data;
    }
}
