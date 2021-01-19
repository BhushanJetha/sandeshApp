package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShowHistoryInnerModel {
    @SerializedName("travel")
    @Expose
    private final List<SearchTravellerModel> travel = null;
    @SerializedName("parcel")
    @Expose
    private final List<SearchOrderModel> parcel = null;

    public List<SearchTravellerModel> getTravel() {
        return travel;
    }

    public List<SearchOrderModel> getParcel() {
        return parcel;
    }

}
