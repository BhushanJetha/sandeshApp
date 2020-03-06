package com.aystech.sandesh.interfaces;

import com.aystech.sandesh.model.SearchOrderModel;
import com.aystech.sandesh.model.SearchTravellerModel;

public interface OnItemClickListener {
    void onItemClicked(SearchOrderModel searchOrderModel);

    void onItemClicked(SearchTravellerModel searchTravellerModel);

    void openOtpDialog();
}
