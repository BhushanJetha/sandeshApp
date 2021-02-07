package com.aystech.sandesh.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.AppController;
import com.aystech.sandesh.utils.Connectivity;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.ImageSelectionMethods;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.UserSession;
import com.aystech.sandesh.utils.ViewProgressDialog;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CorporateRegistrationActivity extends AppCompatActivity {

    private EditText etCompanyName, etBranch, etGSTNo, etAuthorisedPersonName, etDesignation, etAuthorisedPersonMobileNumber, etEmailId,
            etPassword, etReEnteredPassword, etRefferalCode;
    private String strCompanyName, strBranch, strGSTNo, strAuthPersonName, strDesignation, strMobileNumber, strAuthMobileNo, strEmailId,
            strPassword, strReEnteredPassword, strRefferalCode;
    private ImageView imgProfileResult;
    private LinearLayout llProfilePiture;
    private CheckBox cbAccetTermsAndConditions;
    private Button btnSubmit;
    private UserSession userSession;

    Uri picUri;
    Bitmap myBitmap;
    private String filepath;

    private ViewProgressDialog viewProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate_registration);

        if (getIntent() != null)
            strMobileNumber = getIntent().getStringExtra("mobileNumber");

        init();

        onClick();
    }

    private void init() {
        viewProgressDialog = ViewProgressDialog.getInstance();
        userSession = new UserSession(this);

        etCompanyName = findViewById(R.id.etCompanyName);
        etBranch = findViewById(R.id.etBranch);
        etGSTNo = findViewById(R.id.etGSTNo);
        etAuthorisedPersonName = findViewById(R.id.etAuthorisedPersonName);
        etDesignation = findViewById(R.id.etDesignation);
        etAuthorisedPersonMobileNumber = findViewById(R.id.etAuthorisedPersonMobileNumber);
        etEmailId = findViewById(R.id.etEmailId);
        etPassword = findViewById(R.id.etPassword);
        etReEnteredPassword = findViewById(R.id.etReEnteredPassword);
        etRefferalCode = findViewById(R.id.etReferalCode);
        cbAccetTermsAndConditions = findViewById(R.id.cbTermsCondition);
        imgProfileResult = findViewById(R.id.imgProfileResult);
        imgProfileResult.setImageResource(R.drawable.ic_parcel);

        llProfilePiture = findViewById(R.id.llProfilePicture);

        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void onClick() {

        cbAccetTermsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbAccetTermsAndConditions.setChecked(true);
                showTermsConditions();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmailId = etEmailId.getText().toString();
                strAuthMobileNo = etAuthorisedPersonMobileNumber.getText().toString();
                strPassword = etPassword.getText().toString();
                strReEnteredPassword = etReEnteredPassword.getText().toString();
                strRefferalCode = etRefferalCode.getText().toString();
                strCompanyName = etCompanyName.getText().toString();
                strBranch = etBranch.getText().toString();
                strGSTNo = etGSTNo.getText().toString();
                strAuthPersonName = etAuthorisedPersonName.getText().toString();
                strDesignation = etDesignation.getText().toString();

                if (!strCompanyName.isEmpty()) {
                    if (!strBranch.isEmpty()) {
                        if (!strAuthPersonName.isEmpty()) {
                            if (!strDesignation.isEmpty()) {
                                if (!strEmailId.isEmpty()) {
                                    if (Uitility.isValidEmailId(strEmailId)) {
                                        if (!strPassword.isEmpty()) {
                                            if (!strReEnteredPassword.isEmpty()) {
                                                if (strPassword.equals(strReEnteredPassword)) {
                                                    if (cbAccetTermsAndConditions.isChecked()) {
                                                        if (Connectivity.isConnected(CorporateRegistrationActivity.this)) {
                                                            //TODO API Call
                                                            doRigistrationAPICall();
                                                        }
                                                    } else {
                                                        Uitility.showToast(CorporateRegistrationActivity.this, "Please accept terms and condition!");
                                                    }
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

        llProfilePiture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSelectPicture();
            }
        });
    }

    private void showTermsConditions() {
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.dialog_terms_condition, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final AlertDialog dialog = alert.create();
        dialog.show();

        WebView wvTermsConditions = alertLayout.findViewById(R.id.wvTermsCondition);
        // displaying content in WebView from html file that stored in assets folder
        wvTermsConditions.getSettings().setJavaScriptEnabled(true);
        if (AppController.isBaseUrl) {
            wvTermsConditions.loadUrl(AppController.devURL + AppController.terms_and_conditions);
        } else {
            wvTermsConditions.loadUrl(AppController.prodURL + AppController.terms_and_conditions);
        }

        TextView tvOk = alertLayout.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void doRigistrationAPICall() {
        viewProgressDialog.showProgress(this);

        RequestBody emailIdPart = RequestBody.create(MultipartBody.FORM, strEmailId);
        RequestBody mobileNoPart = RequestBody.create(MultipartBody.FORM, strMobileNumber);
        RequestBody authMobileNo = RequestBody.create(MultipartBody.FORM, strAuthMobileNo);
        RequestBody passwordPart = RequestBody.create(MultipartBody.FORM, strPassword);
        RequestBody refferalCodePart = RequestBody.create(MultipartBody.FORM, strRefferalCode);
        RequestBody companyNamePart = RequestBody.create(MultipartBody.FORM, strCompanyName);
        RequestBody branchPart = RequestBody.create(MultipartBody.FORM, strBranch);
        RequestBody gstNoPart = RequestBody.create(MultipartBody.FORM, strGSTNo);
        RequestBody authPersonNamePart = RequestBody.create(MultipartBody.FORM, strAuthPersonName);
        RequestBody designationPart = RequestBody.create(MultipartBody.FORM, strDesignation);

        MultipartBody.Part body;
        if (filepath != null && !filepath.equals("")) {
            File file = new File(filepath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("profile_pic", file.getName(), requestBody);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            body = MultipartBody.Part.createFormData("profile_pic", "", requestBody);
        }

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.doCorporateUserRegistration(
                emailIdPart,
                mobileNoPart,
                authMobileNo,
                passwordPart,
                refferalCodePart,
                companyNamePart,
                branchPart,
                gstNoPart,
                authPersonNamePart,
                designationPart,
                body
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Toast.makeText(CorporateRegistrationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Constants.fragmentType = "Dashboard";
                        userSession.setFirstTimeUserStatus("Yes");
                        Intent i = new Intent(CorporateRegistrationActivity.this, LoginActivity.class);
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

    private void gotoSelectPicture() {
        startActivityForResult(ImageSelectionMethods.getPickImageChooserIntent(this, "corporate"), 200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (ImageSelectionMethods.getPickImageResultUri(this, data, "corporate") != null) {
                picUri = ImageSelectionMethods.getPickImageResultUri(this, data, "corporate");
                filepath = ImageSelectionMethods.getPath(getApplicationContext(), picUri);

                if (filepath.equals("Not found")) {
                    filepath = picUri.getPath();
                }

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    myBitmap = ImageSelectionMethods.getResizedBitmap(myBitmap, 500);

                    imgProfileResult.setImageBitmap(myBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
