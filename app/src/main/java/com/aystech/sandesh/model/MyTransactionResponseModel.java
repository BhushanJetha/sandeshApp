package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyTransactionResponseModel extends CommonResponse {
    @SerializedName("data")
    @Expose
    private MyTransactionModel data;

    public MyTransactionModel getData() {
        return data;
    }

    public void setData(MyTransactionModel data) {
        this.data = data;
    }
}
