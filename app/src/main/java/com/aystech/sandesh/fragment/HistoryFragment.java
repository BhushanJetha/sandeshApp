package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.adapter.NoDataAdapter;
import com.aystech.sandesh.adapter.OrderAdapter;
import com.aystech.sandesh.interfaces.OnItemClickListener;
import com.aystech.sandesh.model.AcceptedOrdersModel;
import com.aystech.sandesh.model.SearchOrderModel;
import com.aystech.sandesh.model.SearchTravellerModel;
import com.aystech.sandesh.model.ShowHistoryInnerModel;
import com.aystech.sandesh.model.ShowHistoryResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.ViewProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HistoryFragment";
    private Context context;

    private ShowHistoryInnerModel showHistoryInnerModel;

    private OrderDetailFragment orderDetailFragment;
    private TravellerDetailFragment travellerDetailFragment;

    private TextView tvTraveller, tvParcel;
    private RecyclerView rvHistory;
    private OrderAdapter orderAdapter;

    private String tag;

    private ViewProgressDialog viewProgressDialog;

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

        orderDetailFragment = (OrderDetailFragment) Fragment.instantiate(context,
                OrderDetailFragment.class.getName());
        travellerDetailFragment = (TravellerDetailFragment) Fragment.instantiate(context,
                TravellerDetailFragment.class.getName());

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
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindDataToRV(String tag) {
        if (showHistoryInnerModel != null) {
            if (tag.equals("travel")) {
                orderAdapter = new OrderAdapter(context, "traveller", new OnItemClickListener() {
                    @Override
                    public void onOrderItemClicked(SearchOrderModel searchOrderModel) {
                    }

                    @Override
                    public void onTravellerItemClicked(SearchTravellerModel searchTravellerModel) {
                        FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                                travellerDetailFragment, R.id.frame_container, true);
                        Bundle bundle = new Bundle();
                        bundle.putInt("travel_id", searchTravellerModel.getTravelId());
                        bundle.putString("tag", "history");
                        travellerDetailFragment.setArguments(bundle);
                    }

                    @Override
                    public void openOtpDialog(AcceptedOrdersModel searchTravellerModel) {
                    }
                });
                orderAdapter.addTravellerList(showHistoryInnerModel.getTravel());
            } else if (tag.equals("parcel")) {
                orderAdapter = new OrderAdapter(context, "order", new OnItemClickListener() {
                    @Override
                    public void onOrderItemClicked(SearchOrderModel searchOrderModel) {
                        FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                                orderDetailFragment, R.id.frame_container, true);
                        Bundle bundle = new Bundle();
                        bundle.putInt("parcel_id", searchOrderModel.getParcelId());
                        bundle.putString("tag", "history");
                        orderDetailFragment.setArguments(bundle);
                    }

                    @Override
                    public void onTravellerItemClicked(SearchTravellerModel searchTravellerModel) {
                    }

                    @Override
                    public void openOtpDialog(AcceptedOrdersModel searchTravellerModel) {
                    }
                });
                orderAdapter.addOrderList(showHistoryInnerModel.getParcel());
            }
            rvHistory.setAdapter(orderAdapter);
        } else {
            NoDataAdapter noDataAdapter = new NoDataAdapter(context, "No Data Found!");
            rvHistory.setAdapter(noDataAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
