package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.adapter.OrderAdapter;
import com.aystech.sandesh.model.ShowHistoryInnerModel;
import com.aystech.sandesh.model.ShowHistoryResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.ViewProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment implements View.OnClickListener {

    Context context;

    ShowHistoryInnerModel showHistoryInnerModel;

    TextView tvTraveller, tvParcel;
    RecyclerView rvHistory;
    OrderAdapter orderAdapter;

    String tag;

    ViewProgressDialog viewProgressDialog;

    public HistoryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        initView(view);

        onClickListener();

        //TODO API Call
        getHistoryData();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        tvTraveller = view.findViewById(R.id.tvTraveller);
        tvParcel = view.findViewById(R.id.tvParcel);
        rvHistory = view.findViewById(R.id.rvHistory);
    }

    private void onClickListener() {
        tvTraveller.setOnClickListener(this);
        tvParcel.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTraveller:
                tag = "travel";

                tvTraveller.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorAccent));
                tvTraveller.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                tvParcel.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorWhite));
                tvParcel.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));

                bindDataToRV(tag);
                break;

            case R.id.tvParcel:
                tag = "parcel";

                tvParcel.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorAccent));
                tvParcel.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                tvTraveller.setBackgroundTintList(context.getResources().getColorStateList(R.color.colorWhite));
                tvTraveller.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));

                bindDataToRV(tag);
                break;
        }
    }

    private void getHistoryData() {
        ViewProgressDialog.getInstance().showProgress(context);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<ShowHistoryResponseModel> call = apiInterface.showHistory();
        call.enqueue(new Callback<ShowHistoryResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ShowHistoryResponseModel> call, @NonNull Response<ShowHistoryResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        showHistoryInnerModel = response.body().getData();
                        bindDataToRV("travel");
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShowHistoryResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindDataToRV(String tag) {
        if (showHistoryInnerModel != null) {
            if (tag.equals("travel")) {
                orderAdapter = new OrderAdapter(context, showHistoryInnerModel.getTravel(), "traveller");
            } else if (tag.equals("parcel")) {
                orderAdapter = new OrderAdapter(context, showHistoryInnerModel.getParcel());
            }
            rvHistory.setAdapter(orderAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
