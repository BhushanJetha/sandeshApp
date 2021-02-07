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
    @SerializedName(value = "balance", alternate = "amount")
    @Expose
    private Double balance;
    @SerializedName(value = "withdrawable_amount")
    @Expose
    private Double withdrawable_amount;
    @SerializedName(value = "charges_neft")
    @Expose
    private Double charges_neft;
    @SerializedName("is_verified")
    @Expose
    private Boolean is_verified;
    @SerializedName("invoice")
    @Expose
    private String invoice;
    @SerializedName("order_id")
    @Expose
    private String orderId;

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }

    public Double getBalance() {
        return balance;
    }

    public Double getWithdrawable_amount() {
        return withdrawable_amount;
    }

    public Double getCharges_neft() {
        return charges_neft;
    }

    public Boolean getIs_verified() {
        return is_verified;
    }

    public String getInvoice() {
        return invoice;
    }

    public String getOrderId() {
        return orderId;
    }
}
