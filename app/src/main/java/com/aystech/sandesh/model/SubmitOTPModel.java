package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitOTPModel {
    @SerializedName("address")
    @Expose
    private SumbitOTPAddressModel address;
    @SerializedName("raw_xml")
    @Expose
    private String rawXml;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("client_id")
    @Expose
    private String clientId;
    @SerializedName("share_code")
    @Expose
    private String shareCode;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("face_score")
    @Expose
    private Integer faceScore;
    @SerializedName("aadhaar_number")
    @Expose
    private String aadhaarNumber;
    @SerializedName("has_image")
    @Expose
    private Boolean hasImage;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("face_status")
    @Expose
    private Boolean faceStatus;
    @SerializedName("zip_data")
    @Expose
    private String zipData;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("zip")
    @Expose
    private String zip;
    @SerializedName("care_of")
    @Expose
    private String careOf;

    public SumbitOTPAddressModel getAddress() {
        return address;
    }

    public void setAddress(SumbitOTPAddressModel address) {
        this.address = address;
    }

    public String getRawXml() {
        return rawXml;
    }

    public void setRawXml(String rawXml) {
        this.rawXml = rawXml;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Integer getFaceScore() {
        return faceScore;
    }

    public void setFaceScore(Integer faceScore) {
        this.faceScore = faceScore;
    }

    public String getAadhaarNumber() {
        return aadhaarNumber;
    }

    public void setAadhaarNumber(String aadhaarNumber) {
        this.aadhaarNumber = aadhaarNumber;
    }

    public Boolean getHasImage() {
        return hasImage;
    }

    public void setHasImage(Boolean hasImage) {
        this.hasImage = hasImage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getFaceStatus() {
        return faceStatus;
    }

    public void setFaceStatus(Boolean faceStatus) {
        this.faceStatus = faceStatus;
    }

    public String getZipData() {
        return zipData;
    }

    public void setZipData(String zipData) {
        this.zipData = zipData;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCareOf() {
        return careOf;
    }

    public void setCareOf(String careOf) {
        this.careOf = careOf;
    }
}
