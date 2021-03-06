package com.aystech.sandesh.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.activity.PaymentActivity;
import com.aystech.sandesh.adapter.NoDataAdapter;
import com.aystech.sandesh.adapter.OrderAdapter;
import com.aystech.sandesh.interfaces.OnItemClickListener;
import com.aystech.sandesh.model.AcceptedOrdersModel;
import com.aystech.sandesh.model.AcceptedOrdersResponseModel;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.SearchOrderModel;
import com.aystech.sandesh.model.SearchOrderResponseModel;
import com.aystech.sandesh.model.SearchTravellerModel;
import com.aystech.sandesh.model.SearchTravellerResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Connectivity;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListFragment extends Fragment {

    private static final String TAG = "OrderListFragment";
    private Context context;

    private DashboardFragment dashboardFragment;
    private OrderDetailFragment orderDetailFragment;
    private TravellerDetailFragment travellerDetailFragment;

    private RecyclerView rvOrder;
    private OrderAdapter orderAdapter;
    private TextView tvScreenTitle;

    private int travel_id, parcel_id, delivery_id;
    private String tag = ""; //default value

    private ViewProgressDialog viewProgressDialog;

    public OrderListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        dashboardFragment = (DashboardFragment) Fragment.instantiate(context,
                DashboardFragment.class.getName());
        orderDetailFragment = (OrderDetailFragment) Fragment.instantiate(context,
                OrderDetailFragment.class.getName());
        travellerDetailFragment = (TravellerDetailFragment) Fragment.instantiate(context,
                TravellerDetailFragment.class.getName());

        if (getArguments() != null) {
            if (getArguments().getInt("parcel_id") != 0) {
                parcel_id = getArguments().getInt("parcel_id");
                tag = getArguments().getString("tag");
            }
            if (getArguments().getInt("travel_id") != 0) {
                travel_id = getArguments().getInt("travel_id");
                tag = getArguments().getString("tag");
            }
            if (getArguments().getString("tag") != null &&
                    Objects.requireNonNull(getArguments().getString("tag")).equals("upcoming_orders")) {
                tag = getArguments().getString("tag");
            }
            if (getArguments().getString("tag") != null &&
                    Objects.requireNonNull(getArguments().getString("tag")).equals("upcoming_rides")) {
                tag = getArguments().getString("tag");
            }
        }

        initView(view);

        switch (tag) {
            case "traveller":
            case "upcoming_orders":  //this is for traveller detail
                if (Connectivity.isConnected(context)) {
                    //TODO API Call
                    getMyOrderList();
                }
                if (tag.equals("traveller"))
                    tvScreenTitle.setText("My Order List");
                else
                    tvScreenTitle.setText("Upcoming Orders");
                break;
            case "order":
            case "upcoming_rides":  //this is for order detail
                if (Connectivity.isConnected(context)) {
                    //TODO API Call
                    getMyTravellerList();
                }
                if (tag.equals("order"))
                    tvScreenTitle.setText("My Journey");
                else
                    tvScreenTitle.setText("Upcoming Rides");
                break;
            case "order_clicked_verify":
            case "order_clicked_verify_end_journey":  //this is for start journey
                if (Connectivity.isConnected(context)) {
                    //TODO API Call
                    getMyAcceptedOrderList();
                }
                tvScreenTitle.setText("Accepted Order List");
                break;
            case "order_clicked_accept_reject_traveller":  //this is for order list
                if (Connectivity.isConnected(context)) {
                    //TODO API Call
                    getMyRequestedOrders();
                }
                tvScreenTitle.setText("Request From Sender");
                break;
            case "order_clicked_accept_reject_sender":  //this is for order list
                if (Connectivity.isConnected(context)) {
                    //TODO API Call
                    getMyTravellerRequestList();
                }
                tvScreenTitle.setText("Request From Travellers");
                break;
        }

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        tvScreenTitle = view.findViewById(R.id.tvScreenTitle);
        rvOrder = view.findViewById(R.id.rvOrder);
    }

    private void getMyOrderList() {
        viewProgressDialog.showProgress(context);

        RetrofitInstance.getClient().getMyOrderList().enqueue(new Callback<SearchOrderResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SearchOrderResponseModel> call, @NonNull Response<SearchOrderResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        bindMyOrderDataToRV(response.body().getData());
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchOrderResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindMyOrderDataToRV(List<SearchOrderModel> data) {
        if (data.size() > 0) {
            orderAdapter = new OrderAdapter(context, "order", new OnItemClickListener() {
                @Override
                public void onOrderItemClicked(SearchOrderModel searchOrderModel) {
                    parcel_id = searchOrderModel.getParcelId();
                    if (tag.equals("upcoming_orders")) {
                        FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                                orderDetailFragment, R.id.frame_container, true);
                        Bundle bundle = new Bundle();
                        bundle.putInt("parcel_id", searchOrderModel.getParcelId());
                        bundle.putString("tag", tag);
                        orderDetailFragment.setArguments(bundle);
                    } else {
                        openDialog(); //bindMyOrderDataToRV ----> onOrderItemClicked
                    }
                }

                @Override
                public void onTravellerItemClicked(SearchTravellerModel searchTravellerModel) {
                    openDialog(); //bindMyOrderDataToRV ----> onTravellerItemClicked
                }

                @Override
                public void openOtpDialog(AcceptedOrdersModel searchTravellerModel) {
                }
            });
            orderAdapter.addOrderList(data);
            rvOrder.setAdapter(orderAdapter);
        } else {
            NoDataAdapter noDataAdapter = new NoDataAdapter(context, "No Orders Found!");
            rvOrder.setAdapter(noDataAdapter);
        }
    }

    private void getMyTravellerList() {
        viewProgressDialog.showProgress(context);

        RetrofitInstance.getClient().getMyTravellerList().enqueue(new Callback<SearchTravellerResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SearchTravellerResponseModel> call, @NonNull Response<SearchTravellerResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        bindMyTravellerDataToRV(response.body().getData());
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchTravellerResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindMyTravellerDataToRV(List<SearchTravellerModel> data) {
        if (data.size() > 0) {
            orderAdapter = new OrderAdapter(context, "traveller", new OnItemClickListener() {
                @Override
                public void onOrderItemClicked(SearchOrderModel searchOrderModel) {
                }

                @Override
                public void onTravellerItemClicked(SearchTravellerModel searchTravellerModel) {
                    travel_id = searchTravellerModel.getTravelId();
                    if (tag.equals("upcoming_rides")) {
                        FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                                travellerDetailFragment, R.id.frame_container, true);
                        Bundle bundle = new Bundle();
                        bundle.putInt("travel_id", travel_id);
                        bundle.putString("tag", tag);
                        travellerDetailFragment.setArguments(bundle);
                    } else {
                        openDialog(); //bindMyTravellerDataToRV ----> onTravellerItemClicked
                    }
                }

                @Override
                public void openOtpDialog(AcceptedOrdersModel searchTravellerModel) {
                }
            });
            orderAdapter.addTravellerList(data);
            rvOrder.setAdapter(orderAdapter);
        } else {
            NoDataAdapter noDataAdapter = new NoDataAdapter(context, "No Travellers Found!");
            rvOrder.setAdapter(noDataAdapter);
        }
    }

    private void getMyAcceptedOrderList() {
        viewProgressDialog.showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("travel_id", travel_id);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<AcceptedOrdersResponseModel> call = null;
        if (tag.equals("order_clicked_verify")) {
            call = apiInterface.getMyAcceptedOrders(jsonObject);
        } else if (tag.equals("order_clicked_verify_end_journey")) {
            call = apiInterface.getMyVerifiedOrder(jsonObject);
        }
        call.enqueue(new Callback<AcceptedOrdersResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<AcceptedOrdersResponseModel> call, @NonNull Response<AcceptedOrdersResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        bindMyAcceptedOrderDataToRV(response.body().getData());
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AcceptedOrdersResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindMyAcceptedOrderDataToRV(List<AcceptedOrdersModel> data) {
        if (data.size() > 0) {
            orderAdapter = new OrderAdapter(context, "order_clicked_verify", new OnItemClickListener() {
                @Override
                public void onOrderItemClicked(SearchOrderModel searchOrderModel) {
                }

                @Override
                public void onTravellerItemClicked(SearchTravellerModel searchTravellerModel) {
                }

                @Override
                public void openOtpDialog(AcceptedOrdersModel searchTravellerModel) {
                    parcel_id = searchTravellerModel.getParcelId();
                    travel_id = searchTravellerModel.getTravelId();
                    delivery_id = searchTravellerModel.getDeliveryId();

                    FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                            orderDetailFragment, R.id.frame_container, false);
                    Bundle bundle = new Bundle();
                    bundle.putInt("parcel_id", parcel_id);
                    bundle.putInt("travel_id", travel_id); //this for to send current location
                    bundle.putInt("delivery_id", delivery_id); //this for to send current location
                    bundle.putString("tag", tag);
                    orderDetailFragment.setArguments(bundle);
                }
            });
            orderAdapter.addAcceptedOrders(data);
            rvOrder.setAdapter(orderAdapter);
        } else {
            NoDataAdapter noDataAdapter = new NoDataAdapter(context, "No Orders Found!");
            rvOrder.setAdapter(noDataAdapter);
        }
    }

    private void getMyTravellerRequestList() {
        viewProgressDialog.showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("parcel_id", parcel_id);

        RetrofitInstance.getClient().getMyTravellerRequestList(jsonObject).enqueue(new Callback<SearchTravellerResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SearchTravellerResponseModel> call, @NonNull Response<SearchTravellerResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        bindDataToTravellerRequestListRV(response.body().getData());
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchTravellerResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindDataToTravellerRequestListRV(List<SearchTravellerModel> data) {
        if (data.size() > 0) {
            orderAdapter = new OrderAdapter(context, "traveller", new OnItemClickListener() {
                @Override
                public void onOrderItemClicked(SearchOrderModel searchOrderModel) {
                }

                @Override
                public void onTravellerItemClicked(SearchTravellerModel searchTravellerModel) {
                    delivery_id = searchTravellerModel.getDeliveryId();
                    travel_id = searchTravellerModel.getTravelId();
                    FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                            travellerDetailFragment, R.id.frame_container, false);
                    Bundle bundle = new Bundle();
                    bundle.putInt("delivery_id", delivery_id);
                    bundle.putInt("travel_id", travel_id);
                    bundle.putString("tag", "accept_reject_order_sender");
                    travellerDetailFragment.setArguments(bundle);
                }

                @Override
                public void openOtpDialog(AcceptedOrdersModel searchTravellerModel) {
                }
            });
            orderAdapter.addTravellerList(data);
            rvOrder.setAdapter(orderAdapter);
        } else {
            NoDataAdapter noDataAdapter = new NoDataAdapter(context, "No Orders Found!");
            rvOrder.setAdapter(noDataAdapter);
        }
    }

    private void getMyRequestedOrders() {
        viewProgressDialog.showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("travel_id", travel_id);

        RetrofitInstance.getClient().myRequestedOrders(jsonObject).enqueue(new Callback<SearchOrderResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SearchOrderResponseModel> call, @NonNull Response<SearchOrderResponseModel> response) {
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
            public void onFailure(@NonNull Call<SearchOrderResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindDataToRV(List<SearchOrderModel> data) {
        if (data.size() > 0) {
            orderAdapter = new OrderAdapter(context, "order", new OnItemClickListener() {
                @Override
                public void onOrderItemClicked(SearchOrderModel searchOrderModel) {
                    delivery_id = searchOrderModel.getDeliveryId();
                    parcel_id = searchOrderModel.getParcelId();
                    FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                            orderDetailFragment, R.id.frame_container, false);
                    Bundle bundle = new Bundle();
                    bundle.putInt("delivery_id", delivery_id);
                    bundle.putInt("parcel_id", parcel_id);
                    bundle.putString("tag", "accept_reject_order_traveller");
                    orderDetailFragment.setArguments(bundle);
                }

                @Override
                public void onTravellerItemClicked(SearchTravellerModel searchTravellerModel) {
                }

                @Override
                public void openOtpDialog(AcceptedOrdersModel searchTravellerModel) {
                }
            });
            orderAdapter.addOrderList(data);
            rvOrder.setAdapter(orderAdapter);
        } else {
            NoDataAdapter noDataAdapter = new NoDataAdapter(context, "No Orders Found!");
            rvOrder.setAdapter(noDataAdapter);
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

                        if (Connectivity.isConnected(context)) {
                            //TODO API Call
                            sendDeliveryRequest();
                        }
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
        viewProgressDialog.showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("travel_id", travel_id);
        jsonObject.addProperty("parcel_id", parcel_id);
        if (tag.equals("order"))
            jsonObject.addProperty("requestor_type", "Traveller");
        else
            jsonObject.addProperty("requestor_type", "sender");

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.sendDeliveryRequest(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                                dashboardFragment, R.id.frame_container, false);
                    } else if (response.body().getBalance() != null && response.body().getBalance() > 0.0) { //Insufficient wallet balance.
                        addAmountInWalletDialog(response.body().getBalance());
                    } else
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void addAmountInWalletDialog(Double balance) {
        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.dialog_add_amt_in_wallet, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final AlertDialog dialog = alert.create();
        dialog.show();

        final EditText etAddBal = alertLayout.findViewById(R.id.etAddBal);
        etAddBal.setText("" + balance);
        Button btnAddBalance = alertLayout.findViewById(R.id.btnAddBalance);

        btnAddBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etAddBal.getText().toString().trim())) {
                    etAddBal.setError("Please enter valid amount");
                    etAddBal.requestFocus();
                } else {
                    etAddBal.setError(null);

                    dialog.dismiss();

                    beforeGoToPaymentClassGetOrderIdFromServer(etAddBal.getText().toString().trim());
                }
            }
        });
    }

    private void beforeGoToPaymentClassGetOrderIdFromServer(final String balAmt) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("amount", balAmt);

        RetrofitInstance.getClient().getOrderIdBeforeAddBal(jsonObject).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Intent intent = new Intent(context, PaymentActivity.class);
                        intent.putExtra("add_amt", balAmt);
                        intent.putExtra("order_id", response.body().getOrderId());
                        startActivity(intent);
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
