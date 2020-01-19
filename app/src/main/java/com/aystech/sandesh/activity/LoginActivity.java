package com.aystech.sandesh.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.model.CommonResponse;
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

        viewProgressDialog = ViewProgressDialog.getInstance();

        init();
        onClick();
    }

    private void init(){
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);

        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
    }

    private void onClick(){
        tvRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.fragmentType = "Registration";
                Intent i = new Intent(LoginActivity.this,   MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strUserName = etUserName.getText().toString();
                strPassword = etPassword.getText().toString();

                /*if(!strUserName.isEmpty() && !strPassword.isEmpty()){
                        doLoginAPICall();
                } else {
                    Toast.makeText(LoginActivity.this,"Please enter user name and password !!", Toast.LENGTH_SHORT).show();
                }*/

                Constants.fragmentType = "Dashboard";
                Intent i = new Intent(LoginActivity.this,   MainActivity.class);
                startActivity(i);
                finish();

            }
        });
    }

    private void doLoginAPICall() {
        ViewProgressDialog.getInstance().showProgress(this);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile_no",strUserName);
        jsonObject.addProperty("password",strPassword);
        jsonObject.addProperty("fcm_id","");

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.doLogin(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    Constants.fragmentType = "Dashboard";
                    Intent i = new Intent(LoginActivity.this,   MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }
}
