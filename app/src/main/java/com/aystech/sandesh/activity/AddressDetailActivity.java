package com.aystech.sandesh.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.aystech.sandesh.R;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressDetailActivity extends AppCompatActivity {

    private Button btnSubmit;
    private Spinner spState, spCity;
    private EditText etAddressLine1, etAddressLine2, etLandmark, etPincode;
    private String strState, strCity, strAddressLine1, strAddressLine2, strLandmark, strPincode;
    private ViewProgressDialog viewProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);

        init();
        onClick();

    }

    private void init(){

        viewProgressDialog = ViewProgressDialog.getInstance();
        spState = findViewById(R.id.spState);
        spCity = findViewById(R.id.spCity);
        etAddressLine1 = findViewById(R.id.etAddressLine1);
        etAddressLine2 = findViewById(R.id.etAddressLine2);
        etLandmark = findViewById(R.id.etLandmark);
        etPincode = findViewById(R.id.etPincode);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void onClick(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.fragmentType = "Dashboard";
                Intent i = new Intent(AddressDetailActivity.this,   MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void doRigistrationAPICall() {
        ViewProgressDialog.getInstance().showProgress(this);

        strAddressLine1 = etAddressLine1.getText().toString();
        strAddressLine2 = etAddressLine2.getText().toString();
        strLandmark = etLandmark.getText().toString();
        strPincode = etPincode.getText().toString();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("state_id","");
        jsonObject.addProperty("city_id","");
        jsonObject.addProperty("address_line1",strAddressLine1);
        jsonObject.addProperty("address_line2",strAddressLine2);
        jsonObject.addProperty("landmark",strLandmark);
        jsonObject.addProperty("pincode",strPincode);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.doLogin(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {

                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }
}
