package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private List<VehicleModel> data = null;

    public List<VehicleModel> getData() {
        return data;
    }
}
