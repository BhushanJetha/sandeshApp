package com.aystech.sandesh.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NatureOfGoodsModel {
    @SerializedName("nature_of_goods_id")
    @Expose
    private Integer natureOfGoodsId;
    @SerializedName("nature_of_goods")
    @Expose
    private String natureOfGoods;

    public Integer getNatureOfGoodsId() {
        return natureOfGoodsId;
    }

    public void setNatureOfGoodsId(Integer natureOfGoodsId) {
        this.natureOfGoodsId = natureOfGoodsId;
    }

    public String getNatureOfGoods() {
        return natureOfGoods;
    }

    public void setNatureOfGoods(String natureOfGoods) {
        this.natureOfGoods = natureOfGoods;
    }
}
