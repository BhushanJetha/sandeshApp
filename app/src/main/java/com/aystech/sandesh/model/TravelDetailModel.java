package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TravelDetailModel {
    @SerializedName("travelPlan")
    @Expose
    private SearchTravellerModel travelPlan;
    @SerializedName("address")
    @Expose
    private AddressModel address;
    @SerializedName("corporateData")
    @Expose
    private CorporateModel corporateData;

    public SearchTravellerModel getTravelPlan() {
        return travelPlan;
    }

    public void setTravelPlan(SearchTravellerModel travelPlan) {
        this.travelPlan = travelPlan;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public CorporateModel getCorporateData() {
        return corporateData;
    }

    public void setCorporateData(CorporateModel corporateData) {
        this.corporateData = corporateData;
    }
}
