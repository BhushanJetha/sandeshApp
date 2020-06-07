package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NominateAPModel {
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("pancard_no")
    @Expose
    private String pancardNo;
    @SerializedName("pancard_pic")
    @Expose
    private String pancardPic;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPancardNo() {
        return pancardNo;
    }

    public void setPancardNo(String pancardNo) {
        this.pancardNo = pancardNo;
    }

    public String getPancardPic() {
        return pancardPic;
    }

    public void setPancardPic(String pancardPic) {
        this.pancardPic = pancardPic;
    }
}
