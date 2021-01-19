package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchTravellerResponseModel extends CommonResponse{

    @SerializedName("data")
    @Expose
    private final List<SearchTravellerModel> data = null;

    public List<SearchTravellerModel> getData() {
        return data;
    }
}
