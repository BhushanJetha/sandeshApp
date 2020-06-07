package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponseModel extends CommonResponse {

    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("isAddressAvailable")
    @Expose
    private Boolean isAddressAvailable;
    @SerializedName("refferal_code")
    @Expose
    private String refferalCode;
    @SerializedName("token")
    @Expose
    private String token;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getIsAddressAvailable() {
        return isAddressAvailable;
    }

    public void setIsAddressAvailable(Boolean isAddressAvailable) {
        this.isAddressAvailable = isAddressAvailable;
    }

    public String getRefferalCode() {
        return refferalCode;
    }

    public void setRefferalCode(String refferalCode) {
        this.refferalCode = refferalCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
