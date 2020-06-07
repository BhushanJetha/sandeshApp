package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrackParcelResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private List<TrackParcelModel> data = null;

    public List<TrackParcelModel> getData() {
        return data;
    }

    public void setData(List<TrackParcelModel> data) {
        this.data = data;
    }
}
