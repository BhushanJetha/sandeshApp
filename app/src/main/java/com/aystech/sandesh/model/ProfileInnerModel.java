package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileInnerModel {
    @SerializedName("userData")
    @Expose
    private UserModel userData;
    @SerializedName("address")
    @Expose
    private AddressModel address;

    public UserModel getUserData() {
        return userData;
    }

    public AddressModel getAddress() {
        return address;
    }
}
