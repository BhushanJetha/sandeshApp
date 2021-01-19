package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QualityResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private final List<QualityModel> data = null;

    public List<QualityModel> getData() {
        return data;
    }
}
