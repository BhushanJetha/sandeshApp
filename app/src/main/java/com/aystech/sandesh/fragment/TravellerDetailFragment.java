package com.aystech.sandesh.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.TravelDetailModel;
import com.aystech.sandesh.model.TravelDetailResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravellerDetailFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private TextView tvFromCityName, tvToCityName, tvStartDate, tvStartTime, tvEndDate, tvEndTime,
            tvDeliveryOption, tvWeight, tvLength, tvBreadth, tvHeight, tvVehicleType, tvVehicleTrainNo,
            tvOtherDetail;

    private Button btnSendRequest;

    private int travel_id, parcel_id;

    ViewProgressDialog viewProgressDialog;

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

        if (getArguments() != null)
            travel_id = getArguments().getInt("traveller_id");

        initView(view);

        onClickListener();

        //TODO API Call
        getTravellerDetail();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

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
        btnSendRequest = view.findViewById(R.id.btnSendRequest);
    }

    private void onClickListener() {
        btnSendRequest.setOnClickListener(this);
    }

    private void getTravellerDetail() {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("travel_id", travel_id);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<TravelDetailResponseModel> call = apiInterface.travelDetail(
                jsonObject
        );
        call.enqueue(new Callback<TravelDetailResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<TravelDetailResponseModel> call, @NonNull Response<TravelDetailResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus())
                        bindDataToUI(response.body().getData());
                    else
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TravelDetailResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindDataToUI(TravelDetailModel data) {
        tvFromCityName.setText(data.getTravelPlan().getFromCity()); //need to set data
        tvToCityName.setText(data.getTravelPlan().getToCity()); //need to set data
        tvStartDate.setText(data.getTravelPlan().getStartDate());
        tvStartTime.setText(data.getTravelPlan().getStartTime());
        tvEndDate.setText(data.getTravelPlan().getEndDate()); //need to set data
        tvEndTime.setText(data.getTravelPlan().getEndTime()); //need to set data
        tvDeliveryOption.setText(data.getTravelPlan().getDeliveryOption());
        tvWeight.setText(data.getTravelPlan().getPreferredWeight());

        if (data.getTravelPlan().getAcceptableVolumeLength() != null && !data.getTravelPlan().getAcceptableVolumeLength().equals(""))
            tvLength.setText(data.getTravelPlan().getAcceptableVolumeLength());
        else
            tvLength.setText("-");

        if (data.getTravelPlan().getAcceptableVolumeBreadth() != null && !data.getTravelPlan().getAcceptableVolumeBreadth().equals(""))
            tvBreadth.setText(data.getTravelPlan().getAcceptableVolumeBreadth());
        else
            tvBreadth.setText("-");

        if (data.getTravelPlan().getAcceptableVolumeWidth() != null && !data.getTravelPlan().getAcceptableVolumeWidth().equals(""))
            tvHeight.setText(data.getTravelPlan().getAcceptableVolumeWidth());
        else
            tvHeight.setText("-");

        tvVehicleType.setText(data.getTravelPlan().getModeOfTravel());

        if (data.getTravelPlan().getVehicleTrainNumber() != null && !data.getTravelPlan().getVehicleTrainNumber().equals(""))
            tvVehicleTrainNo.setText(data.getTravelPlan().getVehicleTrainNumber());
        else
            tvVehicleTrainNo.setText("-");

        if (data.getTravelPlan().getOtherDetail() != null && !data.getTravelPlan().getOtherDetail().equals(""))
            tvOtherDetail.setText(data.getTravelPlan().getOtherDetail());
        else
            tvOtherDetail.setText("-");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendRequest:
                openDialog();
                break;
        }
    }

    private void openDialog() {
        new AlertDialog.Builder(context)
                .setTitle("Send Request")
                .setMessage("Do you want to send the request?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        //TODO API Call
                        sendDeliveryRequest();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void sendDeliveryRequest() {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("travel_id", travel_id);
        jsonObject.addProperty("parcel_id", parcel_id);
        jsonObject.addProperty("requestor_type", "Travel");

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.sendDeliveryRequest(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus())
                        ((MainActivity) context).getSupportFragmentManager().popBackStack();
                    else
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }
}
