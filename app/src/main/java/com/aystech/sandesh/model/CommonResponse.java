package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("status")
    @Expose
    private Boolean status;

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }
}
