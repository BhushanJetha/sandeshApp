package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailResponseModel extends CommonResponse {

    @SerializedName("data")
    @Expose
    private SearchOrderModel data = null;

    public SearchOrderModel getData() {
        return data;
    }
}
