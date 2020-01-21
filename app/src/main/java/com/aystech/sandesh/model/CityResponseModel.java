package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CityResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private List<CityModel> data = null;

    public List<CityModel> getData() {
        return data;
    }

    public Integer getCityId(String manuName){
        int manuId = 0;
        for (int i=0; i< getData().size(); i++){
            if (getData().get(i).getCityName().equals(manuName)){
                manuId = getData().get(i).getCityId();
            }
        }
        return manuId;
    }
}
