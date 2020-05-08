package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PackagingResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private List<PackagingModel> data = null;

    public List<PackagingModel> getData() {
        return data;
    }
}
