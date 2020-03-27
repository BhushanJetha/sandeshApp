package com.aystech.sandesh.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.activity.PaymentActivity;
import com.aystech.sandesh.adapter.WalletAdapter;
import com.aystech.sandesh.model.WalletTransactionModel;
import com.aystech.sandesh.model.WalletTransactionResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.UserSession;
import com.aystech.sandesh.utils.ViewProgressDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWalletFragmentOne extends Fragment {

    private static final String TAG = "MyWalletFragmentOne";
    private Context context;

    private TextView tvUserName;
    private TextView tvWalletAmt;
    private EditText etAddAmt;
    private Button btnAddBal;
    RecyclerView rvPaymentHistory;
    WalletAdapter walletAdapter;

    private UserSession userSession;
    private ViewProgressDialog viewProgressDialog;

    public MyWalletFragmentOne() {
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
        View view = inflater.inflate(R.layout.fragment_my_wallet_fragment_one, container, false);

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        userSession = new UserSession(context);
        viewProgressDialog = ViewProgressDialog.getInstance();

        rvPaymentHistory = view.findViewById(R.id.rvPaymentHistory);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserName.setText(userSession.getUSER_NAME());
        tvWalletAmt = view.findViewById(R.id.tvWalletAmt);
        etAddAmt = view.findViewById(R.id.etAddAmt);
        btnAddBal = view.findViewById(R.id.btnAddBal);
    }

    private void onClickListener() {
        btnAddBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etAddAmt.getText().toString().trim())) {
                    etAddAmt.setError("Please add input");
                    etAddAmt.requestFocus();
                } else {
                    Intent intent = new Intent(context, PaymentActivity.class);
                    intent.putExtra("add_amt", etAddAmt.getText().toString().trim());
                    startActivity(intent);
                }
            }
        });
    }

    private void getMyTransactionList() {
        viewProgressDialog.showProgress(context);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<WalletTransactionResponseModel> call = apiInterface.getMyTransactionList();
        call.enqueue(new Callback<WalletTransactionResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<WalletTransactionResponseModel> call, @NonNull Response<WalletTransactionResponseModel> response) {

                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    tvWalletAmt.setText("Rs. " + response.body().getBalance());
                    bindDataToUI(response.body().getData());
                }
            }

            @Override
            public void onFailure(@NonNull Call<WalletTransactionResponseModel> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: "+t.getLocalizedMessage());
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindDataToUI(List<WalletTransactionModel> data) {
        WalletAdapter walletAdapter = new WalletAdapter(context, data);
        rvPaymentHistory.setAdapter(walletAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);

        //TODO API Call
        getMyTransactionList();
    }

}
