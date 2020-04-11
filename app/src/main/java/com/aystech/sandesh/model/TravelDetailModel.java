package com.aystech.sandesh.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TravelDetailModel implements Parcelable {
    @SerializedName("travelPlan")
    @Expose
    private SearchTravellerModel travelPlan;
    @SerializedName("address")
    @Expose
    private AddressModel address;
    @SerializedName("corporateData")
    @Expose
    private CorporateModel corporateData;
    @SerializedName("parcelData")
    @Expose
    private SearchOrderModel parcelData;

    protected TravelDetailModel(Parcel in) {
        address = in.readParcelable(AddressModel.class.getClassLoader());
        corporateData = in.readParcelable(CorporateModel.class.getClassLoader());
    }

    public static final Creator<TravelDetailModel> CREATOR = new Creator<TravelDetailModel>() {
        @Override
        public TravelDetailModel createFromParcel(Parcel in) {
            return new TravelDetailModel(in);
        }

        @Override
        public TravelDetailModel[] newArray(int size) {
            return new TravelDetailModel[size];
        }
    };

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

    public SearchOrderModel getParcelData() {
        return parcelData;
    }

    public void setParcelData(SearchOrderModel parcelData) {
        this.parcelData = parcelData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(address, flags);
        dest.writeParcelable(corporateData, flags);
    }
}
