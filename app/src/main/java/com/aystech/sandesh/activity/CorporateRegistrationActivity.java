package com.aystech.sandesh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CorporateRegistrationActivity extends AppCompatActivity {

    private EditText etCompanyName, etBranch, etAuthorisedPersonName, etDesignation, etMobileNumber, etEmailId,
            etPassword, etReEnteredPassword, etRefferalCode;

    private String strCompanyName, strBranch, strAuthPersonName, strDesignation, strMobileNumber, strEmailId,
            strPassword, strReEnteredPassword, strRefferalCode, strFCMId, strGender, strBirthDate;
    private RadioButton rbMale, rbFemale;
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

    private void init() {

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

        cbAccetTermsAndConditions = findViewById(R.id.cbTermsCondition);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        ivProfilePicture = findViewById(R.id.imgProfilePicture);

        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void onClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmailId = etEmailId.getText().toString();
                strMobileNumber = etMobileNumber.getText().toString();
                strPassword = etPassword.getText().toString();
                strRefferalCode = etRefferalCode.getText().toString();
                strCompanyName = etCompanyName.getText().toString();
                strBranch = etBranch.getText().toString();
                strAuthPersonName = etAuthorisedPersonName.getText().toString();
                strDesignation = etDesignation.getText().toString();

                if (!strCompanyName.isEmpty()) {
                    if (!strBranch.isEmpty()) {
                        if (!strAuthPersonName.isEmpty()) {
                            if (!strDesignation.isEmpty()) {
                                if (!strEmailId.isEmpty()) {
                                    if (!Uitility.isValidEmailId(strEmailId)) {
                                        if (!strGender.isEmpty()) {
                                            if (!strPassword.isEmpty()) {
                                                if (!strReEnteredPassword.isEmpty()) {
                                                    if (strPassword.equals(strReEnteredPassword)) {
                                                        doRigistrationAPICall();
                                                    } else {
                                                        Uitility.showToast(CorporateRegistrationActivity.this, "Password and re-Entered Password not matched !!");
                                                    }
                                                } else {
                                                    Uitility.showToast(CorporateRegistrationActivity.this, "Please re-enter your password !!");
                                                }
                                            } else {
                                                Uitility.showToast(CorporateRegistrationActivity.this, "Password enter you password !!");
                                            }
                                        } else {
                                            Uitility.showToast(CorporateRegistrationActivity.this, "Please select your gender !!");
                                        }
                                    } else {
                                        Uitility.showToast(CorporateRegistrationActivity.this, "Please enter valid email id !!");
                                    }
                                } else {
                                    Uitility.showToast(CorporateRegistrationActivity.this, "Please enter your email id !!");
                                }
                            } else {
                                Uitility.showToast(CorporateRegistrationActivity.this, "Please enter designation !!");
                            }
                        } else {
                            Uitility.showToast(CorporateRegistrationActivity.this, "Please enter authorised person name !!");
                        }
                    } else {
                        Uitility.showToast(CorporateRegistrationActivity.this, "Please enter branch name !!");
                    }
                } else {
                    Uitility.showToast(CorporateRegistrationActivity.this, "Please enter company name !!");
                }
            }
        });

        rbMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strGender = "Male";
            }
        });

        rbFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strGender = "Female";
            }
        });
    }

    private void doRigistrationAPICall() {
        ViewProgressDialog.getInstance().showProgress(this);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email_id", strEmailId);
        jsonObject.addProperty("mobile_no", strMobileNumber);
        jsonObject.addProperty("password", strPassword);
        jsonObject.addProperty("gender", strGender);
        jsonObject.addProperty("birth_date", strBirthDate);
        jsonObject.addProperty("refferal_code", strRefferalCode);
        jsonObject.addProperty("profile_img", "");
        jsonObject.addProperty("fcm_id", strFCMId);
        jsonObject.addProperty("company_name", strCompanyName);
        jsonObject.addProperty("branch", strBranch);
        jsonObject.addProperty("auth_person_name", strAuthPersonName);
        jsonObject.addProperty("designation", strDesignation);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.doCorporateUserRegistration(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Intent i = new Intent(CorporateRegistrationActivity.this, AddressDetailActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(CorporateRegistrationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
