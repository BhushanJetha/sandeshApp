package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletTransactionModel {
    @SerializedName("trans_id")
    @Expose
    private Integer transId;
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @SerializedName(value = "payment_type", alternate = "transaction_type")
    @Expose
    private String paymentType;
    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;

    public Integer getTransId() {
        return transId;
    }

    public void setTransId(Integer transId) {
        this.transId = transId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
}
