package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackParcelResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private TrackParcelModel data = null;

    public TrackParcelModel getData() {
        return data;
    }

    public void setData(TrackParcelModel data) {
        this.data = data;
    }
}
