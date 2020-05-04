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
import android.widget.Button;
import android.widget.TextView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.adapter.NoDataAdapter;
import com.aystech.sandesh.adapter.WalletAdapter;
import com.aystech.sandesh.model.WalletTransactionModel;
import com.aystech.sandesh.model.WalletTransactionResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.FragmentUtil;
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
    private Button btnAddBal;
    RecyclerView rvPaymentHistory;

    private Double walletBal;

    private MyWalletFragmentTwo myWalletFragmentTwo;

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


        myWalletFragmentTwo = (MyWalletFragmentTwo) Fragment.instantiate(context,
                MyWalletFragmentTwo.class.getName());

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
        btnAddBal = view.findViewById(R.id.btnAddBal);
    }

    private void onClickListener() {
        btnAddBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        myWalletFragmentTwo, R.id.frame_container, true);
                Bundle bundle = new Bundle();
                bundle.putDouble("walletBal", walletBal);
                myWalletFragmentTwo.setArguments(bundle);
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
                    if (response.body().getBalance() != null) {
                        walletBal = response.body().getBalance();
                        tvWalletAmt.setText("Rs. " + walletBal);
                    } else {
                        walletBal = 0.0;
                        tvWalletAmt.setText("Rs. " + walletBal);
                    }
                    bindDataToUI(response.body().getData());
                }
            }

            @Override
            public void onFailure(@NonNull Call<WalletTransactionResponseModel> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindDataToUI(List<WalletTransactionModel> data) {
        if (data.size() > 0) {
            WalletAdapter walletAdapter = new WalletAdapter(context, data);
            rvPaymentHistory.setAdapter(walletAdapter);
        } else {
            NoDataAdapter noDataAdapter = new NoDataAdapter(context, "No record found!");
            rvPaymentHistory.setAdapter(noDataAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);

        //TODO API Call
        getMyTransactionList();
    }

}
