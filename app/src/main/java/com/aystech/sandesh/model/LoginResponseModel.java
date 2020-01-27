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

    @SerializedName("isAddressAvailable")
    @Expose
    private Boolean isAddressAvailable;

    public String getUserType() {
        return userType;
    }

    public String getToken() {
        return token;
    }

    public Boolean getIsAddressAvailable() { return  isAddressAvailable;}
}
