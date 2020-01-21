package com.aystech.sandesh.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndividualRegistrationActivity extends AppCompatActivity {

    private EditText etFirstName, etMiddleName, etLastName, etEmailId, etPassword, etReEnteredPassword, etRefferalCode;
    private String strFirstName, strMiddleName, strLastName, strEmailId, strPassword, strReEnteredPassword,
            strRefferalCode, strMobileNumber, strFCMId, strGender, strBirthDate;
    private RadioButton rbMale, rbFemale, rbOther;
    private CheckBox cbAccetTermsAndConditions;
    private TextView tvBirthDate;
    private ImageView ivProfilePicture;
    private Button btnSubmit;
    private ViewProgressDialog viewProgressDialog;
    private LinearLayout llDateOfBirth;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_registration);

        init();

        onClick();
    }

    private void init() {
        viewProgressDialog = ViewProgressDialog.getInstance();

        etFirstName = findViewById(R.id.etFirstName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etLastName = findViewById(R.id.etLastName);
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
        llDateOfBirth = findViewById(R.id.llDateOfBirth);
    }

    private void onClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strFirstName = etFirstName.getText().toString();
                strMiddleName = etMiddleName.getText().toString();
                strLastName = etLastName.getText().toString();
                strEmailId = etEmailId.getText().toString();
                strPassword = etPassword.getText().toString();
                strFirstName = etFirstName.getText().toString();
                strFirstName = etFirstName.getText().toString();
                strFirstName = etFirstName.getText().toString();
                strFirstName = etFirstName.getText().toString();

                if (!strFirstName.isEmpty()) {
                    if (!strLastName.isEmpty()) {
                        if (!strEmailId.isEmpty()) {
                            if (!Uitility.isValidEmailId(strEmailId)) {
                                if (!strGender.isEmpty()) {
                                    if (!strPassword.isEmpty()) {
                                        if (!strReEnteredPassword.isEmpty()) {
                                            if (strPassword.equals(strReEnteredPassword)) {
                                                doRigistrationAPICall();
                                            } else {
                                                Uitility.showToast(IndividualRegistrationActivity.this, "Password and re-Entered Password not matched !!");
                                            }
                                        } else {
                                            Uitility.showToast(IndividualRegistrationActivity.this, "Please re-enter your password !!");
                                        }
                                    } else {
                                        Uitility.showToast(IndividualRegistrationActivity.this, "Password enter you password !!");
                                    }
                                } else {
                                    Uitility.showToast(IndividualRegistrationActivity.this, "Please select your gender !!");
                                }
                            } else {
                                Uitility.showToast(IndividualRegistrationActivity.this, "Please enter valid email id !!");
                            }
                        } else {
                            Uitility.showToast(IndividualRegistrationActivity.this, "Please enter your email id !!");
                        }
                    } else {
                        Uitility.showToast(IndividualRegistrationActivity.this, "Please enter your last name !!");
                    }
                } else {
                    Uitility.showToast(IndividualRegistrationActivity.this, "Please enter your first name !!");
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

        rbOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strGender = "Other";
            }
        });

        llDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog();
            }
        });
    }

    private void doRigistrationAPICall() {
        ViewProgressDialog.getInstance().showProgress(this);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email_id", strEmailId);
        jsonObject.addProperty("mobile_no", strMobileNumber);
        jsonObject.addProperty("first_name", strFirstName);
        jsonObject.addProperty("middle_name", strMiddleName);
        jsonObject.addProperty("last_name", strLastName);
        jsonObject.addProperty("password", strPassword);
        jsonObject.addProperty("gender", strGender);
        jsonObject.addProperty("birth_date", strBirthDate);
        jsonObject.addProperty("refferal_code", strRefferalCode);
        jsonObject.addProperty("profile_img", "");
        jsonObject.addProperty("fcm_id", strFCMId);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.doIndividualUserRegistration(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Intent i = new Intent(IndividualRegistrationActivity.this, AddressDetailActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(IndividualRegistrationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void openDatePickerDialog() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog datePickerDialog = new DatePickerDialog(IndividualRegistrationActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        tvBirthDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}
