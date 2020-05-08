package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeliveryOptionResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private List<DeliveryOptionModel> data = null;

    public List<DeliveryOptionModel> getData() {
        return data;
    }
}
