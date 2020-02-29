package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aystech.sandesh.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TravellerDetailFragment extends Fragment {

    private Context context;

    private TextView tvFromCityName, tvToCityName, tvStartDate, tvStartTime, tvEndDate, tvEndTime,
            tvDeliveryOption, tvWeight, tvLength, tvBreadth, tvHeight, tvVehicleType, tvVehicleTrainNo,
            tvOtherDetail;

    public TravellerDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_traveller_detail, container, false);

        initView(view);

        //TODO API Call
        getTravellerDetail();

        return view;
    }

    private void initView(View view) {
        tvFromCityName = view.findViewById(R.id.tvFromCityName);
        tvToCityName = view.findViewById(R.id.tvToCityName);
        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvStartTime = view.findViewById(R.id.tvStartTime);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        tvEndTime = view.findViewById(R.id.tvEndTime);
        tvDeliveryOption = view.findViewById(R.id.tvDeliveryOption);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvLength = view.findViewById(R.id.tvLength);
        tvBreadth = view.findViewById(R.id.tvBreadth);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvVehicleType = view.findViewById(R.id.tvVehicleType);
        tvVehicleTrainNo = view.findViewById(R.id.tvVehicleTrainNo);
        tvOtherDetail = view.findViewById(R.id.tvOtherDetail);
    }

    private void getTravellerDetail() {

    }
}
