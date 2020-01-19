package com.aystech.sandesh.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

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

public class CorporateRegistrationActivity extends AppCompatActivity {

    private EditText etCompanyName, etBranch, etAuthorisedPersonName, etDesignation, etMobileNumber, etEmailId,
            etPassword, etReEnteredPassword, etRefferalCode;

    private String strCompanyName, strBranch, strAuthPersonName, strDesignation, strMobileNumber, strEmailId,
            strPassword, strReEnteredPassword,strRefferalCode,  strFCMId, strGender, strBirthDate;
    private RadioButton rbMale, rbFemale, rbOther;
    private CheckBox cbAccetTermsAndConditions;
    private TextView tvBirthDate;
    private ImageView ivProfilePicture;
    private Button btnSubmit;
    private ViewProgressDialog viewProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate_registration);

        init();
        onClick();

    }

    private void init(){

        viewProgressDialog = ViewProgressDialog.getInstance();

        etCompanyName = findViewById(R.id.etCompanyName);
        etBranch = findViewById(R.id.etBranch);
        etAuthorisedPersonName = findViewById(R.id.etAuthorisedPersonName);
        etDesignation = findViewById(R.id.etDesignation);
        etMobileNumber = findViewById(R.id.etAuthorisedPersonMobileNumber);
        etEmailId = findViewById(R.id.etEmailId);
        etPassword = findViewById(R.id.etPassword);
        etReEnteredPassword = findViewById(R.id.etReEnteredPassword);
        etRefferalCode = findViewById(R.id.etReferalCode);

        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbOther = findViewById(R.id.rbOther);

        cbAccetTermsAndConditions = findViewById(R.id.cbTermsCondition);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        ivProfilePicture = findViewById(R.id.imgProfilePicture);

        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void onClick(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CorporateRegistrationActivity.this,   AddressDetailActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void doRigistrationAPICall() {
        ViewProgressDialog.getInstance().showProgress(this);

        strEmailId = etEmailId.getText().toString();
        strMobileNumber = etMobileNumber.getText().toString();
        strPassword = etPassword.getText().toString();
        strRefferalCode = etRefferalCode.getText().toString();
        strCompanyName = etCompanyName.getText().toString();
        strBranch = etBranch.getText().toString();
        strAuthPersonName = etAuthorisedPersonName.getText().toString();
        strDesignation = etDesignation.getText().toString();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email_id",strEmailId);
        jsonObject.addProperty("mobile_no",strMobileNumber);
        jsonObject.addProperty("password",strPassword);
        jsonObject.addProperty("gender",strGender);
        jsonObject.addProperty("birth_date",strBirthDate);
        jsonObject.addProperty("refferal_code",strRefferalCode);
        jsonObject.addProperty("profile_img","");
        jsonObject.addProperty("fcm_id",strFCMId);
        jsonObject.addProperty("company_name",strCompanyName);
        jsonObject.addProperty("branch",strBranch);
        jsonObject.addProperty("auth_person_name",strAuthPersonName);
        jsonObject.addProperty("designation",strDesignation);

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
                    Intent i = new Intent(CorporateRegistrationActivity.this,   MainActivity.class);
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
