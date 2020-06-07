package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyTransactionResponseModel{
    @SerializedName("accountData")
    @Expose
    private List<WalletTransactionModel> accountData = null;
    @SerializedName("transactionData")
    @Expose
    private List<WalletTransactionModel> transactionData = null;

    public List<WalletTransactionModel> getAccountData() {
        return accountData;
    }

    public void setAccountData(List<WalletTransactionModel> accountData) {
        this.accountData = accountData;
    }

    public List<WalletTransactionModel> getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(List<WalletTransactionModel> transactionData) {
        this.transactionData = transactionData;
    }

}
