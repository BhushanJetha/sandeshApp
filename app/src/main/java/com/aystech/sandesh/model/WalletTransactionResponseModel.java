package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class WalletTransactionResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private ArrayList<WalletTransactionModel> data = null;

    public ArrayList<WalletTransactionModel> getData() {
        return data;
    }

    public void setData(ArrayList<WalletTransactionModel> data) {
        this.data = data;
    }
}
