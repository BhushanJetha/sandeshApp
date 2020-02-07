package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CorporateModel {
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("branch")
    @Expose
    private String branch;
    @SerializedName("auth_person_name")
    @Expose
    private String authPersonName;
    @SerializedName("designation")
    @Expose
    private String designation;

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
}
