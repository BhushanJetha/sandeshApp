package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrdersResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private final List<SearchOrderModel> data = null;

    public List<SearchOrderModel> getData() {
        return data;
    }
}
