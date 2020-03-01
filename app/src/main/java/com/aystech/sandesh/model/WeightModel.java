package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeightModel {
    @SerializedName("weight_id")
    @Expose
    private Integer weightId;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("weight_unit")
    @Expose
    private String weightUnit;

    public Integer getWeightId() {
        return weightId;
    }

    public void setWeightId(Integer weightId) {
        this.weightId = weightId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    /**
     * Pay attention here, you have to override the toString method as the
     * ArrayAdapter will reads the toString of the given object for the name
     *
     * @return weight
     */
    @Override
    public String toString() {
        return weight;
    }
}
