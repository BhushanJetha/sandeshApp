package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeightResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private final List<WeightModel> data = null;

    public List<WeightModel> getData() {
        return data;
    }

    public Integer getWeightId(String manuName) {
        int manuId = 0;
        for (int i = 0; i < getData().size(); i++) {
            if (getData().get(i).getWeight().equals(manuName)) {
                manuId = getData().get(i).getWeightId();
            }
        }
        return manuId;
    }

}
