package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AcceptedOrdersResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private List<AcceptedOrdersModel> data = null;

    public List<AcceptedOrdersModel> getData() {
        return data;
    }

    public void setData(List<AcceptedOrdersModel> data) {
        this.data = data;
    }
}
