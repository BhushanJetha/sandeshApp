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
import com.aystech.sandesh.adapter.NoDataAdapter;
import com.aystech.sandesh.adapter.OrderAdapter;
import com.aystech.sandesh.interfaces.OnItemClickListener;
import com.aystech.sandesh.model.AcceptedOrdersModel;
import com.aystech.sandesh.model.SearchOrderModel;
import com.aystech.sandesh.model.SearchTravellerModel;
import com.aystech.sandesh.model.ShowHistoryInnerModel;
import com.aystech.sandesh.model.ShowHistoryResponseModel;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Connectivity;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.ViewProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRequestedOrderFragments extends Fragment implements View.OnClickListener {

    private Context context;

    private ShowHistoryInnerModel showHistoryInnerModel;

    private OrderListFragment orderListFragment;

    private TextView tvTraveller, tvParcel;
    private RecyclerView rvMyRequestedOrders;
    private OrderAdapter orderAdapter;

    private String tag;

    private ViewProgressDialog viewProgressDialog;

    public MyRequestedOrderFragments() {
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
        View view = inflater.inflate(R.layout.fragment_my_requested_order_fragments, container, false);

        orderListFragment = (OrderListFragment) Fragment.instantiate(context,
                OrderListFragment.class.getName());

        initView(view);

        onClickListener();

        if(Connectivity.isConnected(context)) {
            //TODO API Call
            getData();
        }

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        tvTraveller = view.findViewById(R.id.tvTraveller);
        tvParcel = view.findViewById(R.id.tvParcel);
        rvMyRequestedOrders = view.findViewById(R.id.rvMyRequestedOrders);
    }

    private void onClickListener() {
        tvTraveller.setOnClickListener(this);
        tvParcel.setOnClickListener(this);
    }

    private void getData() {
        viewProgressDialog.showProgress(context);

        RetrofitInstance.getClient().getMyOrderInbox().enqueue(new Callback<ShowHistoryResponseModel>() {
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
                if (showHistoryInnerModel.getTravel().size() > 0) {
                    orderAdapter = new OrderAdapter(context, "traveller", new OnItemClickListener() {
                        @Override
                        public void onOrderItemClicked(SearchOrderModel searchOrderModel) {
                        }

                        @Override
                        public void onTravellerItemClicked(SearchTravellerModel searchTravellerModel) {
                            FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                                    orderListFragment, R.id.frame_container,
                                    true);
                            Bundle bundle = new Bundle();
                            bundle.putInt("travel_id", searchTravellerModel.getTravelId());
                            bundle.putString("tag", "order_clicked_accept_reject_traveller");
                            orderListFragment.setArguments(bundle);
                        }

                        @Override
                        public void openOtpDialog(AcceptedOrdersModel searchTravellerModel) {
                        }
                    });
                    orderAdapter.addTravellerList(showHistoryInnerModel.getTravel());
                    rvMyRequestedOrders.setAdapter(orderAdapter);
                } else {
                    NoDataAdapter noDataAdapter = new NoDataAdapter(context, "No Travellers Found!");
                    rvMyRequestedOrders.setAdapter(noDataAdapter);
                }
            } else if (tag.equals("parcel")) {
                if (showHistoryInnerModel.getParcel().size() > 0) {
                    orderAdapter = new OrderAdapter(context, "order", new OnItemClickListener() {
                        @Override
                        public void onOrderItemClicked(SearchOrderModel searchOrderModel) {
                            FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                                    orderListFragment, R.id.frame_container,
                                    true);
                            Bundle bundle = new Bundle();
                            bundle.putInt("parcel_id", searchOrderModel.getParcelId());
                            bundle.putString("tag", "order_clicked_accept_reject_sender");
                            orderListFragment.setArguments(bundle);
                        }

                        @Override
                        public void onTravellerItemClicked(SearchTravellerModel searchTravellerModel) {
                        }

                        @Override
                        public void openOtpDialog(AcceptedOrdersModel searchTravellerModel) {
                        }
                    });
                    orderAdapter.addOrderList(showHistoryInnerModel.getParcel());
                    rvMyRequestedOrders.setAdapter(orderAdapter);
                } else {
                    NoDataAdapter noDataAdapter = new NoDataAdapter(context, "No Order Found!");
                    rvMyRequestedOrders.setAdapter(noDataAdapter);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
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
}
