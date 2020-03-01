package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TravelDetailResponseModel extends CommonResponse{
    @SerializedName("data")
    @Expose
    private SearchTravellerModel data = null;

    public SearchTravellerModel getData() {
        return data;
    }
}
