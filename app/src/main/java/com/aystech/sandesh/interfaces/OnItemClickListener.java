package com.aystech.sandesh.interfaces;

import com.aystech.sandesh.model.AcceptedOrdersModel;
import com.aystech.sandesh.model.SearchOrderModel;
import com.aystech.sandesh.model.SearchTravellerModel;

public interface OnItemClickListener {
    void onOrderItemClicked(SearchOrderModel searchOrderModel);

    void onTravellerItemClicked(SearchTravellerModel searchTravellerModel);

    void openOtpDialog(AcceptedOrdersModel searchTravellerModel);
}
