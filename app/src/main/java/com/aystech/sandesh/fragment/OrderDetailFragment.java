package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.OrderDetailResponseModel;
import com.aystech.sandesh.model.TravelDetailModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.AppController;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailFragment extends Fragment implements View.OnClickListener {

    private Context context;

    OrderListFragment orderListFragment;
    DashboardFragment dashboardFragment;

    private TextView tvFromCityName, tvToCityName, tvStartDate, tvStartTime, tvEndDate, tvEndTime,
            tvDeliveryOption, tvNatureGoods, tvGoodsDesc, tvQuality, tvWeight, tvPackaging, tvGoods,
            tvReceiverName, tvReceiverMobileNo, tvReceiverAddress;

    private RadioButton rbCommercial, rbNonCommercial, rbHazardousYes, rbHazardousNo,
            rbProhibitedYes, rbProhibitedNo, rbFraglleYes, rbFraglleNo, rbFlamableToxicExplosiveYes,
            rbFlamableToxicExplosiveNo;

    private ImageView imgInvoice, imgParcel;

    private ConstraintLayout clAfterVerify;
    private EditText etComment;
    private Button btnSendRequest, btnReject, btnVerify;

    private int parcel_id;
    private String tag, status;

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

        orderListFragment = (OrderListFragment) Fragment.instantiate(context,
                OrderListFragment.class.getName());
        dashboardFragment = (DashboardFragment) Fragment.instantiate(context,
                DashboardFragment.class.getName());

        if (getArguments() != null) {
            if (getArguments().getString("tag") != null &&
                    Objects.requireNonNull(getArguments().getString("tag")).equals("after_verify")) {
                parcel_id = getArguments().getInt("parcel_id");
                tag = getArguments().getString("tag");
            } else {
                parcel_id = getArguments().getInt("parcel_id");
            }
        }

        initView(view);

        if (tag != null && tag.equals("after_verify")) {
            btnSendRequest.setVisibility(View.GONE);
            clAfterVerify.setVisibility(View.VISIBLE);
        } else {
            btnSendRequest.setVisibility(View.VISIBLE);
            clAfterVerify.setVisibility(View.GONE);
        }

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
        rbCommercial = view.findViewById(R.id.rbCommercial);
        rbNonCommercial = view.findViewById(R.id.rbNonCommercial);
        imgInvoice = view.findViewById(R.id.imgInvoice);
        imgParcel = view.findViewById(R.id.imgParcel);
        rbHazardousYes = view.findViewById(R.id.rbHazardousYes);
        rbHazardousNo = view.findViewById(R.id.rbHazardousNo);
        rbProhibitedYes = view.findViewById(R.id.rbProhibitedYes);
        rbProhibitedNo = view.findViewById(R.id.rbProhibitedNo);
        rbFraglleYes = view.findViewById(R.id.rbFraglleYes);
        rbFraglleNo = view.findViewById(R.id.rbFraglleNo);
        rbFlamableToxicExplosiveYes = view.findViewById(R.id.rbFlamableToxicExplosiveYes);
        rbFlamableToxicExplosiveNo = view.findViewById(R.id.rbFlamableToxicExplosiveNo);
        tvReceiverName = view.findViewById(R.id.tvReceiverName);
        tvReceiverMobileNo = view.findViewById(R.id.tvReceiverMobileNo);
        tvReceiverAddress = view.findViewById(R.id.tvReceiverAddress);
        btnSendRequest = view.findViewById(R.id.btnSendRequest);
        clAfterVerify = view.findViewById(R.id.clAfterVerify);
        etComment = view.findViewById(R.id.etComment);
        btnReject = view.findViewById(R.id.btnReject);
        btnVerify = view.findViewById(R.id.btnVerify);
    }

    private void onClickListener() {
        btnSendRequest.setOnClickListener(this);
        btnReject.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
    }

    private void getOrderDetail() {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("parcel_id", parcel_id);

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

    private void bindDataToUI(TravelDetailModel data) {
        tvFromCityName.setText(data.getParcelData().getFromCity()); //need to set data
        tvToCityName.setText(data.getParcelData().getToCity()); //need to set data
        tvStartDate.setText(data.getParcelData().getStartDate());
        tvStartTime.setText(data.getParcelData().getStartTime());
        tvEndDate.setText(data.getParcelData().getEndDate()); //need to set data
        tvEndTime.setText(data.getParcelData().getEndTime()); //need to set data
        tvDeliveryOption.setText(data.getParcelData().getDeliveryOption());
        tvNatureGoods.setText(data.getParcelData().getNatureOfGoods());

        if (data.getParcelData().getGoodDescription() != null && !data.getParcelData().getGoodDescription().equals(""))
            tvGoodsDesc.setText(data.getParcelData().getGoodDescription());
        else
            tvGoodsDesc.setText("-");

        tvQuality.setText(data.getParcelData().getQuality());
        tvWeight.setText(data.getParcelData().getWeight());
        tvPackaging.setText(data.getParcelData().getPackaging());

        if (data.getParcelData().getValueOfGoods() != null && !data.getParcelData().getValueOfGoods().equals(""))
            tvGoods.setText(data.getParcelData().getValueOfGoods());
        else
            tvGoods.setText("-");

        if (data.getParcelData().getOwnership() != null && !data.getParcelData().getOwnership().equals("")) {
            if (data.getParcelData().getOwnership().equals("Commercial")) {
                rbCommercial.setChecked(true);
                rbNonCommercial.setChecked(false);
            } else {
                rbCommercial.setChecked(false);
                rbNonCommercial.setChecked(true);
            }
        }

        Glide.with(context)
                .load(AppController.imageURL + data.getParcelData().getInvoicePic())
                .error(R.drawable.ic_logo_sandesh)
                .into(imgInvoice);

        Glide.with(context)
                .load(AppController.imageURL + data.getParcelData().getParcelPic())
                .error(R.drawable.ic_logo_sandesh)
                .into(imgParcel);

        if (data.getParcelData().getIsHazardous() != null && !data.getParcelData().getIsHazardous().equals("")) {
            if (data.getParcelData().getIsHazardous().equals("Yes")) {
                rbHazardousYes.setChecked(true);
                rbHazardousNo.setChecked(false);
            } else {
                rbHazardousYes.setChecked(false);
                rbHazardousNo.setChecked(true);
            }
        }

        if (data.getParcelData().getIsProhibited() != null && !data.getParcelData().getIsProhibited().equals("")) {
            if (data.getParcelData().getIsProhibited().equals("Yes")) {
                rbProhibitedYes.setChecked(true);
                rbProhibitedNo.setChecked(false);
            } else {
                rbProhibitedYes.setChecked(false);
                rbProhibitedNo.setChecked(true);
            }
        }

        if (data.getParcelData().getIsFragile() != null && !data.getParcelData().getIsFragile().equals("")) {
            if (data.getParcelData().getIsFragile().equals("Yes")) {
                rbFraglleYes.setChecked(true);
                rbFraglleNo.setChecked(false);
            } else {
                rbFraglleYes.setChecked(false);
                rbFraglleNo.setChecked(true);
            }
        }

        if (data.getParcelData().getIsFlamable() != null && !data.getParcelData().getIsFlamable().equals("")) {
            if (data.getParcelData().getIsFlamable().equals("Yes")) {
                rbFlamableToxicExplosiveYes.setChecked(true);
                rbFlamableToxicExplosiveNo.setChecked(false);
            } else {
                rbFlamableToxicExplosiveYes.setChecked(false);
                rbFlamableToxicExplosiveNo.setChecked(true);
            }
        }

        if (data.getParcelData().getReceiverName() != null && !data.getParcelData().getReceiverName().equals(""))
            tvReceiverName.setText(data.getParcelData().getReceiverName());
        else
            tvReceiverName.setText("-");

        if (data.getParcelData().getReceiverMobileNo() != null && !data.getParcelData().getReceiverMobileNo().equals(""))
            tvReceiverMobileNo.setText(data.getParcelData().getReceiverMobileNo());
        else
            tvReceiverMobileNo.setText("-");

        if (data.getParcelData().getReceiverAddressDetail() != null && !data.getParcelData().getReceiverAddressDetail().equals(""))
            tvReceiverAddress.setText(data.getParcelData().getReceiverAddressDetail());
        else
            tvReceiverAddress.setText("-");

        status = data.getParcelData().getStatus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSendRequest:
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(), orderListFragment, R.id.frame_container,
                        false);
                Bundle bundle = new Bundle();
                bundle.putInt("parcel_id", parcel_id);
                bundle.putString("tag", "order");
                orderListFragment.setArguments(bundle);
                break;

            case R.id.btnReject:
                if (TextUtils.isEmpty(etComment.getText().toString().trim())) {
                    etComment.setError("Please enter your comment here");
                    etComment.requestFocus();
                } else {
                    //TODO API Call
                    sendVerificationStatus();
                }
                break;

            case R.id.btnVerify:
                //TODO API Call
                sendVerificationStatus();
                break;
        }
    }

    private void sendVerificationStatus() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("parcel_id", parcel_id);
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("comment", etComment.getText().toString());

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.sendVerificationStatus(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(), dashboardFragment, R.id.frame_container,
                                false);
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
            }
        });
    }
}
