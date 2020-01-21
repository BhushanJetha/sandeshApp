package com.aystech.sandesh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.LoginResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView tvRegisterHere, tvForgotPassword;
    private EditText etUserName, etPassword;
    private String strUserName, strPassword;

    ViewProgressDialog viewProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        onClick();
    }

    private void init() {
        viewProgressDialog = ViewProgressDialog.getInstance();

        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
    }

    private void onClick() {
        tvRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.fragmentType = "Registration";
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strUserName = etUserName.getText().toString();
                strPassword = etPassword.getText().toString();

                if (!strUserName.isEmpty() && !strPassword.isEmpty()) {
                    doLoginAPICall();
                } else {
                    Toast.makeText(LoginActivity.this, "Please enter user name and password !!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgetPasswordDialog();
            }
        });
    }

    private void openForgetPasswordDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.forgot_password_dialog, null);

        final EditText etEmail = alertLayout.findViewById(R.id.et_email);
        Button btnResetPassword = alertLayout.findViewById(R.id.btnResetPassword);

        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final AlertDialog dialog = alert.create();
        dialog.show();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = etEmail.getText().toString();

                //TODO API Call
                forgetPassword(pass);

                dialog.dismiss();
            }
        });
    }

    private void forgetPassword(String mobileNo) {
        ViewProgressDialog.getInstance().showProgress(this);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile_no", mobileNo);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.forgotPassword(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus())
                        Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void doLoginAPICall() {
        ViewProgressDialog.getInstance().showProgress(this);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile_no", strUserName);
        jsonObject.addProperty("password", strPassword);
        jsonObject.addProperty("fcm_id", "");

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<LoginResponseModel> call = apiInterface.doLogin(
                jsonObject
        );
        call.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponseModel> call, @NonNull Response<LoginResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Constants.fragmentType = "Dashboard";
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }
}
