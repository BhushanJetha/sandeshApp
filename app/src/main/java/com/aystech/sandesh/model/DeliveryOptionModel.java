package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryOptionModel {
    @SerializedName("delivery_option_id")
    @Expose
    private Integer deliveryOptionId;
    @SerializedName("delivery_option")
    @Expose
    private String deliveryOption;

    public Integer getDeliveryOptionId() {
        return deliveryOptionId;
    }

    public void setDeliveryOptionId(Integer deliveryOptionId) {
        this.deliveryOptionId = deliveryOptionId;
    }

    public String getDeliveryOption() {
        return deliveryOption;
    }

    public void setDeliveryOption(String deliveryOption) {
        this.deliveryOption = deliveryOption;
    }
}
