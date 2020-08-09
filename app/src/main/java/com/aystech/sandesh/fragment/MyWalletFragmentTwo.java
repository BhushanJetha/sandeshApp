package com.aystech.sandesh.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.activity.PaymentActivity;
import com.aystech.sandesh.activity.WithdrawAmountActivity;
import com.aystech.sandesh.adapter.MyWalletTabLayoutAdapter;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Connectivity;
import com.aystech.sandesh.utils.UserSession;
import com.aystech.sandesh.utils.ViewProgressDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyWalletFragmentTwo extends Fragment implements View.OnClickListener {

    private Context context;

    private TabLayout walletTabLayout;
    private ViewPager viewPager;

    private TextView tvUserName;
    private TextView tvWalletAmt;
    private Button btnAddMoney, btnTransferMoney;

    private Double walletBal;

    private UserSession userSession;
    private ViewProgressDialog viewProgressDialog;

    public MyWalletFragmentTwo() {
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
        View view = inflater.inflate(R.layout.fragment_my_wallet_fragment_two, container, false);

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();
        userSession = new UserSession(context);

        walletTabLayout = view.findViewById(R.id.walletTabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserName.setText(userSession.getUSER_NAME());
        tvWalletAmt = view.findViewById(R.id.tvWalletAmt);
        btnAddMoney = view.findViewById(R.id.btnAddMoney);
        btnTransferMoney = view.findViewById(R.id.btnTransferMoney);

        walletTabLayout.addTab(walletTabLayout.newTab().setText("Statement"));
        walletTabLayout.addTab(walletTabLayout.newTab().setText("Royalty"));

        walletTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        MyWalletTabLayoutAdapter adapter = new MyWalletTabLayoutAdapter(context, getChildFragmentManager(),
                walletTabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(walletTabLayout));

        walletTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void onClickListener() {
        btnAddMoney.setOnClickListener(this);
        btnTransferMoney.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);

        if(Connectivity.isConnected(context)) {
            //TODO Call API
            getWalletBalance();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddMoney:
                addAmountInWalletDialog();
                break;

            case R.id.btnTransferMoney:
                Intent intent = new Intent(context, WithdrawAmountActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void addAmountInWalletDialog() {
        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.dialog_add_amt_in_wallet, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final AlertDialog dialog = alert.create();
        dialog.show();

        TextView tvTitle = alertLayout.findViewById(R.id.tvTitle);
        tvTitle.setText("Add money to\nSandesh Wallet");
        final EditText etAddBal = alertLayout.findViewById(R.id.etAddBal);
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

                    Intent intent = new Intent(context, PaymentActivity.class);
                    intent.putExtra("add_amt", etAddBal.getText().toString().trim());
                    startActivity(intent);
                }
            }
        });
    }

    private void getWalletBalance() {
        viewProgressDialog.showProgress(context);

        RetrofitInstance.getClient().getWalletBalance().enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {

                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getBalance() != null) {
                        walletBal = response.body().getBalance();
                        tvWalletAmt.setText("Rs. " + String.format("%.2f", walletBal));
                    } else {
                        walletBal = 0.0;
                        tvWalletAmt.setText("Rs. " + walletBal);
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
