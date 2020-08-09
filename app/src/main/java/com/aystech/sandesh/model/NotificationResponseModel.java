package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationResponseModel extends CommonResponse{
    @SerializedName("data")
    @Expose
    private List<NotificationModel> data = null;

    public List<NotificationModel> getData() {
        return data;
    }

    public void setData(List<NotificationModel> data) {
        this.data = data;
    }
}
