package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.adapter.NoDataAdapter;
import com.aystech.sandesh.adapter.OrderAdapter;
import com.aystech.sandesh.interfaces.OnItemClickListener;
import com.aystech.sandesh.model.AcceptedOrdersModel;
import com.aystech.sandesh.model.SearchOrderModel;
import com.aystech.sandesh.model.SearchTravellerModel;
import com.aystech.sandesh.model.SearchTravellerResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.ViewProgressDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyRequestedOrderFragments extends Fragment {

    private static final String TAG = "MyRequestedOrderFragmen";
    private Context context;

    private OrderListFragment orderListFragment;

    private RecyclerView rvMyRequestedOrders;
    private OrderAdapter orderAdapter;

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

        //TODO API Call
        getData();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        rvMyRequestedOrders = view.findViewById(R.id.rvMyRequestedOrders);
    }

    private void getData() {
        ViewProgressDialog.getInstance().showProgress(context);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<SearchTravellerResponseModel> call = apiInterface.getMyTravellerList();
        call.enqueue(new Callback<SearchTravellerResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SearchTravellerResponseModel> call, @NonNull Response<SearchTravellerResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        bindDataToRV(response.body().getData());
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchTravellerResponseModel> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindDataToRV(List<SearchTravellerModel> data) {
        if (data.size() > 0) {
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
                    bundle.putString("tag", "order_clicked_accept_reject");
                    orderListFragment.setArguments(bundle);
                }

                @Override
                public void openOtpDialog(AcceptedOrdersModel searchTravellerModel) {
                }
            });
            orderAdapter.addTravellerList(data);
            rvMyRequestedOrders.setAdapter(orderAdapter);
        } else {
            NoDataAdapter noDataAdapter = new NoDataAdapter(context, "No Traveller Found!");
            rvMyRequestedOrders.setAdapter(noDataAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
