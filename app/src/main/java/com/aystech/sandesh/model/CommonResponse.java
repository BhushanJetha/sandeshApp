package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("status")
    @Expose
    private Integer status;

    public String getMessage() {
        return message;
    }

    public Integer getStatus() {
        return status;
    }
}
