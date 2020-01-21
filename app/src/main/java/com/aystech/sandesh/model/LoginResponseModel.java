package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponseModel extends CommonResponse{
    @SerializedName("user_type")
    @Expose
    private String userType;

    @SerializedName("token")
    @Expose
    private String token;

    public String getUserType() {
        return userType;
    }

    public String getToken() {
        return token;
    }
}