package com.aystech.sandesh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.fragment.DashboardFragment;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Connectivity;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithdrawAmountActivity extends AppCompatActivity {

    private DashboardFragment dashboardFragment;

    private Double walletBal;
    private boolean isWithdrawAmtGreter = false;

    ImageView imgNotification, imgLogout;
    Toolbar toolBar;
    TextView txtWalletBal;
    EditText etAmount, etAccountNumber, etIFSCCode, etAccountName;
    Button btnProceed;
    private ViewProgressDialog viewProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_amount);

        dashboardFragment = (DashboardFragment)
                Fragment.instantiate(this, DashboardFragment.class.getName());

        initView();

        if (Connectivity.isConnected(this)) {
            //TODO Call API
            getWalletBalance();
        }
    }

    private void initView() {
        viewProgressDialog = ViewProgressDialog.getInstance();

        imgNotification = findViewById(R.id.imgNotification);
        imgNotification.setVisibility(View.GONE);
        imgLogout = findViewById(R.id.imgLogout);
        imgLogout.setVisibility(View.GONE);

        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        txtWalletBal = findViewById(R.id.txtWalletBal);
        etAmount = findViewById(R.id.etAmount);
        etAmount.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if(s.toString() != null && s.toString() !=""  && !s.toString().isEmpty()){
                    if (Double.parseDouble(String.valueOf(s)) > walletBal) {
                        etAmount.setError("You can not withdraw amount above Rs. " + walletBal);
                        etAmount.requestFocus();
                        isWithdrawAmtGreter = true;
                    } else {
                        isWithdrawAmtGreter = false;
                        etAmount.setError(null);
                    }
                }
            }
        });
        etAccountNumber = findViewById(R.id.etAccountNumber);
        etIFSCCode = findViewById(R.id.etIFSCCode);
        etAccountName = findViewById(R.id.etAccountName);
        btnProceed = findViewById(R.id.btnProceed);
        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isWithdrawAmtGreter) {
                    etAmount.setError("You can not withdraw amount above Rs. " + walletBal);
                    etAmount.requestFocus();
                } else if (TextUtils.isEmpty(etAmount.getText().toString())) {
                    etAmount.setError("Please enter withdrawal amount");
                    etAmount.requestFocus();
                } else if (TextUtils.isEmpty(etAccountNumber.getText().toString())) {
                    etAccountNumber.setError("Please enter account number");
                    etAccountNumber.requestFocus();
                } else if (TextUtils.isEmpty(etIFSCCode.getText().toString())) {
                    etIFSCCode.setError("Please enter IFSC code");
                    etIFSCCode.requestFocus();
                } else if (TextUtils.isEmpty(etAccountName.getText().toString())) {
                    etAccountName.setError("Please enter account name");
                    etAccountName.requestFocus();
                } else {
                    etAmount.setError(null);
                    etAccountName.setError(null);
                    etIFSCCode.setError(null);
                    etAccountName.setError(null);
                    if (Connectivity.isConnected(WithdrawAmountActivity.this)) {
                        //TODO Call API
                        withdrawReq();
                    }
                }
            }
        });
    }

    private void getWalletBalance() {
        viewProgressDialog.showProgress(this);

        RetrofitInstance.getClient().getWalletBalance().enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getBalance() != null) {
                        walletBal = response.body().getWithdrawable_amount();
                        txtWalletBal.setText("Withdrawal Amount: Rs. " + String.format("%.2f", walletBal));
                        etAmount.setText(String.format("%.2f", walletBal));
                    } else {
                        walletBal = 0.0;
                        txtWalletBal.setText("Rs. " + walletBal);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void withdrawReq() {
        viewProgressDialog.showProgress(this);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("account_name",etAccountName.getText().toString());
        jsonObject.addProperty("ifsc",etIFSCCode.getText().toString());
        jsonObject.addProperty("account_number",etAccountNumber.getText().toString());
        jsonObject.addProperty("amount",etAmount.getText().toString());

        RetrofitInstance.getClient().withdrawRequest(jsonObject).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Toast.makeText(WithdrawAmountActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();


                        Intent i = new Intent(WithdrawAmountActivity.this,MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);

                       // FragmentUtil.commonMethodForFragment(getSupportFragmentManager(),
                                //dashboardFragment, R.id.frame_container, false);
                    } else {
                        Toast.makeText(WithdrawAmountActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}