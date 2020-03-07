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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.OrderDetailResponseModel;
import com.aystech.sandesh.model.SearchOrderModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private TextView tvFromCityName, tvToCityName, tvStartDate, tvStartTime, tvEndDate, tvEndTime,
            tvDeliveryOption, tvNatureGoods, tvGoodsDesc, tvQuality, tvWeight, tvPackaging, tvGoods,
            tvReceiverName, tvReceiverMobileNo, tvReceiverAddress;

    private RadioGroup rgOwnership, rgHazardous, rgProhibited, rgFraglle, rgFlamableToxicExplosive;
    private RadioButton rbCommercial, rbNonCommercial, rbHazardousYes, rbHazardousNo,
            rbProhibitedYes, rbProhibitedNo, rbFraglleYes, rbFraglleNo, rbFlamableToxicExplosiveYes,
            rbFlamableToxicExplosiveNo;

    private ImageView imgInvoice, imgParcel;

    private Button btnSendRequest;

    private int order_id, travel_id;

    ViewProgressDialog viewProgressDialog;

    public OrderDetailFragment() {
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
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        if (getArguments() != null)
            order_id = getArguments().getInt("order_id");

        initView(view);

        onClickListener();

        //TODO API Call
        getOrderDetail();

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
        tvNatureGoods = view.findViewById(R.id.tvNatureGoods);
        tvGoodsDesc = view.findViewById(R.id.tvGoodsDesc);
        tvQuality = view.findViewById(R.id.tvQuality);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvPackaging = view.findViewById(R.id.tvPackaging);
        tvGoods = view.findViewById(R.id.tvGoods);
        rgOwnership = view.findViewById(R.id.rgOwnership);
        rbCommercial = view.findViewById(R.id.rbCommercial);
        rbNonCommercial = view.findViewById(R.id.rbNonCommercial);
        imgInvoice = view.findViewById(R.id.imgInvoice);
        imgParcel = view.findViewById(R.id.imgParcel);
        rgHazardous = view.findViewById(R.id.rgHazardous);
        rbHazardousYes = view.findViewById(R.id.rbHazardousYes);
        rbHazardousNo = view.findViewById(R.id.rbHazardousNo);
        rgProhibited = view.findViewById(R.id.rgProhibited);
        rbProhibitedYes = view.findViewById(R.id.rbProhibitedYes);
        rbProhibitedNo = view.findViewById(R.id.rbProhibitedNo);
        rgFraglle = view.findViewById(R.id.rgFraglle);
        rbFraglleYes = view.findViewById(R.id.rbFraglleYes);
        rbFraglleNo = view.findViewById(R.id.rbFraglleNo);
        rgFlamableToxicExplosive = view.findViewById(R.id.rgFlamableToxicExplosive);
        rbFlamableToxicExplosiveYes = view.findViewById(R.id.rbFlamableToxicExplosiveYes);
        rbFlamableToxicExplosiveNo = view.findViewById(R.id.rbFlamableToxicExplosiveNo);
        tvReceiverName = view.findViewById(R.id.tvReceiverName);
        tvReceiverMobileNo = view.findViewById(R.id.tvReceiverMobileNo);
        tvReceiverAddress = view.findViewById(R.id.tvReceiverAddress);
        btnSendRequest = view.findViewById(R.id.btnSendRequest);
    }

    private void onClickListener() {
        btnSendRequest.setOnClickListener(this);
    }

    private void getOrderDetail() {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("order_id", order_id);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<OrderDetailResponseModel> call = apiInterface.orderDetail(
                jsonObject
        );
        call.enqueue(new Callback<OrderDetailResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<OrderDetailResponseModel> call, @NonNull Response<OrderDetailResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus())
                        bindDataToUI(response.body().getData());
                    else
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderDetailResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindDataToUI(SearchOrderModel data) {
        tvFromCityName.setText(""); //need to set data
        tvToCityName.setText(""); //need to set data
        tvStartDate.setText(data.getStartDate());
        tvStartTime.setText(data.getStartTime());
        tvEndDate.setText(data.getEndDate()); //need to set data
        tvEndTime.setText(data.getEndTime()); //need to set data
        tvDeliveryOption.setText(data.getDeliveryOption());
        tvNatureGoods.setText(data.getNatureOfGoods());

        if (data.getGoodDescription() != null && !data.getGoodDescription().equals(""))
            tvGoodsDesc.setText(data.getGoodDescription());
        else
            tvGoodsDesc.setText("-");

        tvQuality.setText(data.getQuality());
        tvWeight.setText(data.getWeight());
        tvPackaging.setText(data.getPackaging());

        if (data.getValueOfGoods() != null && !data.getValueOfGoods().equals(""))
            tvGoods.setText(data.getValueOfGoods());
        else
            tvGoods.setText("-");

        if (data.getReceiverName() != null && !data.getReceiverName().equals(""))
            tvReceiverName.setText(data.getReceiverName());
        else
            tvReceiverName.setText("-");

        if (data.getReceiverMobileNo() != null && !data.getReceiverMobileNo().equals(""))
            tvReceiverMobileNo.setText(data.getReceiverMobileNo());
        else
            tvReceiverMobileNo.setText("-");

        if (data.getReceiverAddressDetail() != null && !data.getReceiverAddressDetail().equals(""))
            tvReceiverAddress.setText(data.getReceiverAddressDetail());
        else
            tvReceiverAddress.setText("-");
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
        jsonObject.addProperty("parcel_id", order_id);
        jsonObject.addProperty("requestor_type", "Order");

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
