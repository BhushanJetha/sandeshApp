package com.aystech.sandesh.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CorporateModel implements Parcelable {
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("gst_no")
    @Expose
    private String gstNo;
    @SerializedName("auth_person_name")
    @Expose
    private String authPersonName;
    @SerializedName("designation")
    @Expose
    private String designation;

    protected CorporateModel(Parcel in) {
        companyName = in.readString();
        branch = in.readString();
        gstNo = in.readString();
        authPersonName = in.readString();
        designation = in.readString();
    }

    public static final Creator<CorporateModel> CREATOR = new Creator<CorporateModel>() {
        @Override
        public CorporateModel createFromParcel(Parcel in) {
            return new CorporateModel(in);
        }

        @Override
        public CorporateModel[] newArray(int size) {
            return new CorporateModel[size];
        }
    };

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAuthPersonName() {
        return authPersonName;
    }

    public void setAuthPersonName(String authPersonName) {
        this.authPersonName = authPersonName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(companyName);
        dest.writeString(branch);
        dest.writeString(gstNo);
        dest.writeString(authPersonName);
        dest.writeString(designation);
    }
}
