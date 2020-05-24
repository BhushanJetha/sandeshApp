package com.aystech.sandesh.fragment;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.TravelDetailModel;
import com.aystech.sandesh.model.TravelDetailResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TravellerDetailFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private TravelDetailModel travelDetailModel;

    private DashboardFragment dashboardFragment;
    private OrderListFragment orderListFragment;
    private PlanTravelFragment planTravelFragment;

    private TextView tvName, tvMobileNo, tvAddress, tvFromStateName, tvToStateName,
            tvFromCityName, tvToCityName, tvStartDate, tvStartTime, tvEndDate, tvEndTime,
            tvToPincode, tvFromPincode, tvDeliveryOption, tvWeight, tvLength, tvBreadth,
            tvHeight, tvVehicleType, tvVehicleTrainNo, tvOtherDetail;
    private ImageView imgTravelEdit;
    private Button btnSendRequest, btnTravelDelete;
    private ConstraintLayout clAcceptRejectOrder;
    private EditText etAcceptRejectComment;
    private Button btnRejectOrder, btnAcceptOrder;

    private int travel_id, delivery_id;
    private String tag, status;

    private ViewProgressDialog viewProgressDialog;

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

        dashboardFragment = (DashboardFragment) Fragment.instantiate(context,
                DashboardFragment.class.getName());
        orderListFragment = (OrderListFragment) Fragment.instantiate(context,
                OrderListFragment.class.getName());
        planTravelFragment = (PlanTravelFragment)
                Fragment.instantiate(context, PlanTravelFragment.class.getName());

        if (getArguments() != null) {
            delivery_id = getArguments().getInt("delivery_id");
            travel_id = getArguments().getInt("travel_id");
            tag = getArguments().getString("tag");
        }

        initView(view);

        if (tag != null && tag.equals("accept_reject_order_sender")) {
            btnSendRequest.setVisibility(View.GONE);
            clAcceptRejectOrder.setVisibility(View.VISIBLE);
        } else {
            btnSendRequest.setVisibility(View.VISIBLE);
            clAcceptRejectOrder.setVisibility(View.GONE);
        }

        onClickListener();

        //TODO API Call
        getTravellerDetail();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        tvName = view.findViewById(R.id.tvName);
        tvMobileNo = view.findViewById(R.id.tvMobileNo);
        tvAddress = view.findViewById(R.id.tvAddress);
        imgTravelEdit = view.findViewById(R.id.imgTravelEdit);
        tvFromStateName = view.findViewById(R.id.tvFromStateName);
        tvToStateName = view.findViewById(R.id.tvToStateName);
        tvFromCityName = view.findViewById(R.id.tvFromCityName);
        tvToCityName = view.findViewById(R.id.tvToCityName);
        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvStartTime = view.findViewById(R.id.tvStartTime);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        tvEndTime = view.findViewById(R.id.tvEndTime);
        tvToPincode = view.findViewById(R.id.tvToPincode);
        tvFromPincode = view.findViewById(R.id.tvFromPincode);
        tvDeliveryOption = view.findViewById(R.id.tvDeliveryOption);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvLength = view.findViewById(R.id.tvLength);
        tvBreadth = view.findViewById(R.id.tvBreadth);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvVehicleType = view.findViewById(R.id.tvVehicleType);
        tvVehicleTrainNo = view.findViewById(R.id.tvVehicleTrainNo);
        tvOtherDetail = view.findViewById(R.id.tvOtherDetail);
        btnSendRequest = view.findViewById(R.id.btnSendRequest);
        btnTravelDelete = view.findViewById(R.id.btnTravelDelete);
        clAcceptRejectOrder = view.findViewById(R.id.clAcceptRejectOrder);
        etAcceptRejectComment = view.findViewById(R.id.etAcceptRejectComment);
        btnRejectOrder = view.findViewById(R.id.btnRejectOrder);
        btnAcceptOrder = view.findViewById(R.id.btnAcceptOrder);
        if (tag != null && !tag.equals("")) {
            if (tag.equals("normal"))
                btnSendRequest.setVisibility(View.VISIBLE);
            if (tag.equals("upcoming_rides") || tag.equals("success_travel"))
                imgTravelEdit.setVisibility(View.VISIBLE);
        }
    }

    private void onClickListener() {
        btnSendRequest.setOnClickListener(this);
        imgTravelEdit.setOnClickListener(this);
        btnTravelDelete.setOnClickListener(this);
        btnAcceptOrder.setOnClickListener(this);
        btnRejectOrder.setOnClickListener(this);
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
                    if (response.body().getStatus()) {
                        travelDetailModel = response.body().getData();
                        bindDataToUI(response.body().getData());
                    } else
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TravelDetailResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void bindDataToUI(TravelDetailModel data) {
        if (data.getTravelPlan().getFull_name() != null &&
                !data.getTravelPlan().getFull_name().equals(""))
            tvName.setText("" + data.getTravelPlan().getFull_name());
        else
            tvName.setText("-");

        if (data.getTravelPlan().getMobileNo() != null &&
                !data.getTravelPlan().getMobileNo().equals(""))
            tvMobileNo.setText("" + data.getTravelPlan().getMobileNo());
        else
            tvMobileNo.setText("-");

        if (data.getAddress() != null &&
                !data.getAddress().equals(""))
            tvAddress.setText(data.getAddress().getAddressLine1() + ", " +
                    data.getAddress().getAddressLine2() + ", " + data.getAddress().getLandmark()
                    + ", " + data.getAddress().getState() + ", " + data.getAddress().getCity()
                    + ", " + data.getAddress().getPincode());
        else
            tvAddress.setText("-");

        tvFromStateName.setText(data.getTravelPlan().getFrom_state());
        tvToStateName.setText(data.getTravelPlan().getTo_state());
        tvFromCityName.setText(data.getTravelPlan().getFromCity());
        tvToCityName.setText(data.getTravelPlan().getToCity());
        tvStartDate.setText(data.getTravelPlan().getStartDate());
        tvStartTime.setText(data.getTravelPlan().getStartTime());
        tvEndDate.setText(data.getTravelPlan().getEndDate());
        tvEndTime.setText(data.getTravelPlan().getEndTime());
        tvToPincode.setText(data.getTravelPlan().getToPincode());
        tvFromPincode.setText(data.getTravelPlan().getFromPincode());
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

        if (data.getTravelPlan().getStatus().equals("Fresh Parcel")) {
            imgTravelEdit.setVisibility(View.VISIBLE);
            btnTravelDelete.setVisibility(View.VISIBLE);
        } else {
            imgTravelEdit.setVisibility(View.GONE);
            btnTravelDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.btnSendRequest:
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        orderListFragment, R.id.frame_container, true);
                bundle.putInt("travel_id", travel_id);
                bundle.putString("tag", "traveller");
                orderListFragment.setArguments(bundle);
                break;

            case R.id.imgTravelEdit:
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        planTravelFragment, R.id.frame_container, true);
                bundle.putParcelable("travel_detail", travelDetailModel);
                bundle.putString("tag", "edit");
                planTravelFragment.setArguments(bundle);
                break;

            case R.id.btnRejectOrder:
                if (TextUtils.isEmpty(etAcceptRejectComment.getText().toString().trim())) {
                    etAcceptRejectComment.setError("Please enter your rejection reason here");
                    etAcceptRejectComment.requestFocus();
                } else {
                    status = "reject";
                    //TODO API Call
                    sendOrderRequestStatus(); //btnRejectOrder
                }
                break;

            case R.id.btnAcceptOrder:
                status = "accept";
                //TODO API Call
                sendOrderRequestStatus(); //btnAcceptOrder
                break;

            case R.id.btnTravelDelete:
                //TODO API Call
                deleteTravelDetail();
                break;
        }
    }

    private void sendOrderRequestStatus() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("delivery_id", delivery_id);
        jsonObject.addProperty("status", status);
        jsonObject.addProperty("rejection_reason", etAcceptRejectComment.getText().toString());
        jsonObject.addProperty("request_acceptor", "sender");

        RetrofitInstance.getClient().sendOrderRequestStatus(jsonObject).enqueue(new Callback<CommonResponse>() {
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

    private void deleteTravelDetail() {
        viewProgressDialog.showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("travel_id", travel_id);

        RetrofitInstance.getClient().deleteTravelPlan(jsonObject).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

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
                viewProgressDialog.hideDialog();
            }
        });
    }
}
