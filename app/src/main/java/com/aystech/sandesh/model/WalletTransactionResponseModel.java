package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WalletTransactionResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private List<WalletTransactionModel> data = null;

    public List<WalletTransactionModel> getData() {
        return data;
    }

    public void setData(List<WalletTransactionModel> data) {
        this.data = data;
    }
}
