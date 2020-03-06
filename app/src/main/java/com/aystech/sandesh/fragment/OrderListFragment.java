package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.LoginActivity;
import com.aystech.sandesh.adapter.OrderAdapter;
import com.aystech.sandesh.interfaces.OnItemClickListener;
import com.aystech.sandesh.model.SearchOrderModel;
import com.aystech.sandesh.model.SearchOrderResponseModel;
import com.aystech.sandesh.model.SearchTravellerModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.ViewProgressDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListFragment extends Fragment {

    private Context context;

    private RecyclerView rvOrder;
    private OrderAdapter orderAdapter;

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

        initView(view);

        //TODO API Call
        searchOrderByData();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        rvOrder = view.findViewById(R.id.rvOrder);
    }

    private void searchOrderByData() {
        ViewProgressDialog.getInstance().showProgress(context);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<SearchOrderResponseModel> call = apiInterface.getOrderList();
        call.enqueue(new Callback<SearchOrderResponseModel>() {
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
            orderAdapter = new OrderAdapter(context, "order_clicked_verify", new OnItemClickListener() {
                @Override
                public void onItemClicked(SearchOrderModel searchOrderModel) {
                }

                @Override
                public void onItemClicked(SearchTravellerModel searchTravellerModel) {
                }

                @Override
                public void openOtpDialog() {
                    LayoutInflater inflater = getLayoutInflater();
                    View alertLayout = inflater.inflate(R.layout.verify_otp_dialog, null);

                    final EditText etOTP = alertLayout.findViewById(R.id.etOTP);
                    Button btnVerifyOTP = alertLayout.findViewById(R.id.btnVerifyOTP);

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    // this is set the view from XML inside AlertDialog
                    alert.setView(alertLayout);
                    // disallow cancel of AlertDialog on click of back button and outside touch
                    alert.setCancelable(true);

                    final AlertDialog dialog = alert.create();
                    dialog.show();

                    btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String otp = etOTP.getText().toString();

                            //TODO API Call
                            verifyOTP(otp);

                            dialog.dismiss();
                        }
                    });
                }
            });
            orderAdapter.addOrderList(data);
            rvOrder.setAdapter(orderAdapter);
        }
    }

    private void verifyOTP(String otp) {

    }
}
