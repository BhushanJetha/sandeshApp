package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowHistoryResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private ShowHistoryInnerModel data;

    public ShowHistoryInnerModel getData() {
        return data;
    }
}
