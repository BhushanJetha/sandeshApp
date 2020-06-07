package com.aystech.sandesh.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WalletTransactionModel implements Parcelable {
    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName(value = "transaction_id", alternate = "transactionID")
    @Expose
    private String transactionId;
    @SerializedName(value = "transaction_type",alternate = "payment_type")
    @Expose
    private String transactionType;
    @SerializedName(value = "transaction_mode", alternate = "paymentMode")
    @Expose
    private String transactionMode;
    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;

    public WalletTransactionModel(Double amount, String transactionId,
                                  String transactionType, String transactionMode, String transactionDate) {
        this.amount = amount;
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.transactionMode = transactionMode;
        this.transactionDate = transactionDate;
    }

    protected WalletTransactionModel(Parcel in) {
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readDouble();
        }
        transactionId = in.readString();
        transactionType = in.readString();
        transactionMode = in.readString();
        transactionDate = in.readString();
    }

    public static final Creator<WalletTransactionModel> CREATOR = new Creator<WalletTransactionModel>() {
        @Override
        public WalletTransactionModel createFromParcel(Parcel in) {
            return new WalletTransactionModel(in);
        }

        @Override
        public WalletTransactionModel[] newArray(int size) {
            return new WalletTransactionModel[size];
        }
    };

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getTransactionMode() {
        return transactionMode;
    }

    public void setTransactionMode(String transactionMode) {
        this.transactionMode = transactionMode;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (amount == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(amount);
        }
        dest.writeString(transactionId);
        dest.writeString(transactionType);
        dest.writeString(transactionMode);
        dest.writeString(transactionDate);
    }
}
