package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StateResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private List<StateModel> data = null;

    public List<StateModel> getData() {
        return data;
    }

    public Integer getStateId(String manuName){
        int manuId = 0;
        for (int i=0; i< getData().size(); i++){
            if (getData().get(i).getStateName().equals(manuName)){
                manuId = getData().get(i).getStateId();
            }
        }
        return manuId;
    }
}
