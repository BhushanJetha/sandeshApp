package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NatureOfGoodsResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private List<NatureOfGoodsModel> data = null;

    public List<NatureOfGoodsModel> getData() {
        return data;
    }
}
