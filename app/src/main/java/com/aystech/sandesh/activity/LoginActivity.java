package com.aystech.sandesh.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
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
import com.aystech.sandesh.utils.JWTUtils;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.UserSession;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView tvRegisterHere, tvForgotPassword;
    private EditText etUserName, etPassword;
    private String strUserName, strPassword;
    private int login_count = 0;

    UserSession userSession;
    ViewProgressDialog viewProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        onClick();
    }

    private void init() {
        userSession = new UserSession(this);
        viewProgressDialog = ViewProgressDialog.getInstance();

        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        SpannableString registerUser = new SpannableString(getResources().getString(R.string.new_user));
        registerUser.setSpan(new UnderlineSpan(), 0, registerUser.length(), 0);
        tvRegisterHere.setText(registerUser);

        SpannableString forgotPassword = new SpannableString(getResources().getString(R.string.forgot_password));
        forgotPassword.setSpan(new UnderlineSpan(), 0, forgotPassword.length(), 0);
        tvForgotPassword.setText(forgotPassword);

        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);

        if (userSession.getLoginCount() != null && !userSession.getLoginCount().equals("")) {
            login_count = Integer.parseInt(userSession.getLoginCount());
        }

        Log.d("Login count-->", String.valueOf(login_count));
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

                if(login_count < 5) {
                    if (!strUserName.isEmpty() && !strPassword.isEmpty()) {
                        //TODO API Call
                        doLoginAPICall();
                    } else {
                        Toast.makeText(LoginActivity.this, "Please enter user name and password !!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    showPasswordLockDialog();
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

                if(!pass.isEmpty()){
                    if(pass.length()==10){
                        //TODO API Call
                        forgetPassword(pass);

                        dialog.dismiss();
                    }else {
                        Uitility.showToast(LoginActivity.this, "Please enter 10 digit mobile number !");
                    }
                }else {
                    Uitility.showToast(LoginActivity.this, "Please enter your mobile number !");
                }
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
                    if (response.body().getStatus()) {
                        login_count = 0;
                        userSession.setResetPasswordStatus("reset");
                        Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else
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
        jsonObject.addProperty("fcm_id", userSession.getFCMId());
        jsonObject.addProperty("device_type", "Android");

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

                        userSession.setJWTToken(response.body().getToken());
                        userSession.setUserName(response.body().getUserName());
                        userSession.setLoginCount(0);

                        try {
                            String json = JWTUtils.decoded(response.body().getToken().split(" ")[1]);
                            JSONObject jsonObject1 = new JSONObject(json);
                            Log.d("LoginActivity", "onResponse: " + jsonObject1.toString());
                            userSession.setUserId(jsonObject1.getString("user_id"));
                            userSession.setUserType(jsonObject1.getString("user_type"));
                            userSession.setUserMobile(jsonObject1.getString("mobile_no"));
                            userSession.setUserEmail(jsonObject1.getString("email_id"));
                            userSession.setUserEmail(jsonObject1.getString("email_id"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (response.body().getIsAddressAvailable()) {
                            Constants.fragmentType = "Dashboard";
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Intent i = new Intent(LoginActivity.this, AddressDetailActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        int login_count = 1;
                        if (userSession.getLoginCount() != null && !userSession.getLoginCount().equals("")) {
                            login_count = Integer.parseInt(userSession.getLoginCount());
                            if (login_count < 5) {
                                login_count++;
                                userSession.setLoginCount(login_count);
                            } else {
                                showPasswordLockDialog();
                            }
                        } else {
                            userSession.setLoginCount(login_count);
                        }
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

    private void showPasswordLockDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Sandesh")
                .setMessage("Your account is locked, Because of 5 wrong attempt. Please reset your password !")
                .setCancelable(true)
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                })

                .show();
    }
}
