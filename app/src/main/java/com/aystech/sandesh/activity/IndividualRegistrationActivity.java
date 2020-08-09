package com.aystech.sandesh.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
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
import com.aystech.sandesh.utils.Connectivity;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.ImageSelectionMethods;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.UserSession;
import com.aystech.sandesh.utils.ViewProgressDialog;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndividualRegistrationActivity extends AppCompatActivity {

    private EditText etFirstName, etMiddleName, etLastName, etEmailId, etPassword, etReEnteredPassword, etRefferalCode;
    private String strFirstName, strMiddleName, strLastName, strEmailId, strPassword, strReEnteredPassword,
            strRefferalCode, strMobileNumber, strGender, strBirthDate = "";
    private RadioButton rbMale, rbFemale, rbOther;
    private CheckBox cbAccetTermsAndConditions;
    private TextView tvBirthDate, tvAcceptTermsAndCondition;
    private ImageView imgProfileResult;
    private Button btnSubmit;
    private LinearLayout llDateOfBirth, llProfilePiture;
    private UserSession userSession;

    private int age = 0;

    final Calendar myCalendar = Calendar.getInstance();

    Uri picUri;
    Bitmap myBitmap;
    private String filepath;

    private ViewProgressDialog viewProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_registration);

        if (getIntent() != null)
            strMobileNumber = getIntent().getStringExtra("mobileNumber");

        init();

        onClick();
    }

    private void init() {
        viewProgressDialog = ViewProgressDialog.getInstance();
        userSession = new UserSession(this);

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
        imgProfileResult = findViewById(R.id.imgProfileResult);
        imgProfileResult.setImageResource(R.drawable.ic_parcel);

        btnSubmit = findViewById(R.id.btnSubmit);
        llDateOfBirth = findViewById(R.id.llDateOfBirth);
        tvAcceptTermsAndCondition = findViewById(R.id.tvTermsCondition);

        llProfilePiture = findViewById(R.id.llProfilePicture);

        SpannableString ssAcceptTermsAndCondition = new SpannableString(getResources().getString(R.string.accept_terms_amp_conditions));
        ssAcceptTermsAndCondition.setSpan(new UnderlineSpan(), 0, ssAcceptTermsAndCondition.length(), 0);
        tvAcceptTermsAndCondition.setText(ssAcceptTermsAndCondition);
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
                strFirstName = etFirstName.getText().toString();
                strMiddleName = etMiddleName.getText().toString();
                strLastName = etLastName.getText().toString();
                strEmailId = etEmailId.getText().toString();
                strPassword = etPassword.getText().toString();
                strReEnteredPassword = etReEnteredPassword.getText().toString();
                strRefferalCode = etRefferalCode.getText().toString();

                if (!strFirstName.isEmpty()) {
                    if (!strLastName.isEmpty()) {
                        if (!strEmailId.isEmpty()) {
                            if (Uitility.isValidEmailId(strEmailId)) {
                                if (!strGender.isEmpty()) {
                                    if (!strPassword.isEmpty()) {
                                        if (!strReEnteredPassword.isEmpty()) {
                                            if (strPassword.equals(strReEnteredPassword)) {
                                                if (cbAccetTermsAndConditions.isChecked()) {
                                                    if(!strBirthDate.isEmpty()){
                                                        if(age >= 18){
                                                            if(Connectivity.isConnected(IndividualRegistrationActivity.this)) {
                                                                //TODO API Call
                                                                doRegistrationAPICall();
                                                            }
                                                        }else {
                                                            Uitility.showToast(IndividualRegistrationActivity.this, "Sorry you are not able to register, your age in below 18 !");
                                                        }
                                                    }else {
                                                        Uitility.showToast(IndividualRegistrationActivity.this, "Please select your date of birth !");
                                                    }
                                                } else {
                                                    Uitility.showToast(IndividualRegistrationActivity.this, "Please accept terms and condition!");
                                                }
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
        wvTermsConditions.loadUrl("file:///android_res/raw/" + "terms_and_condition.html");

        TextView tvOk = alertLayout.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void doRegistrationAPICall() {
        viewProgressDialog.showProgress(this);

        RequestBody emailIdPart = RequestBody.create(MultipartBody.FORM, strEmailId);
        RequestBody mobileNoPart = RequestBody.create(MultipartBody.FORM, strMobileNumber);
        RequestBody firstNamePart = RequestBody.create(MultipartBody.FORM, strFirstName);
        RequestBody middleNamePart = RequestBody.create(MultipartBody.FORM, strMiddleName);
        RequestBody lastNamePart = RequestBody.create(MultipartBody.FORM, strLastName);
        RequestBody passwordPart = RequestBody.create(MultipartBody.FORM, strPassword);
        RequestBody genderPart = RequestBody.create(MultipartBody.FORM, strGender);
        RequestBody birthDatePart = RequestBody.create(MultipartBody.FORM, strBirthDate);
        RequestBody refferalCodePart = RequestBody.create(MultipartBody.FORM, strRefferalCode);

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
        Call<CommonResponse> call = apiInterface.doIndividualUserRegistration(
                emailIdPart,
                mobileNoPart,
                firstNamePart,
                middleNamePart,
                lastNamePart,
                passwordPart,
                genderPart,
                birthDatePart,
                refferalCodePart,
                body
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Toast.makeText(IndividualRegistrationActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        Constants.fragmentType = "Dashboard";
                        userSession.setFirstTimeUserStatus("Yes");
                        Intent i = new Intent(IndividualRegistrationActivity.this, LoginActivity.class);
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
        DatePickerDialog mDate = new DatePickerDialog(this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        mDate.getDatePicker().setMaxDate(System.currentTimeMillis());
        mDate.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            strBirthDate = Uitility.dateFormat(year, monthOfYear, dayOfMonth); //IndividualRegistrationActivity
            age = Integer.parseInt(Uitility.getAge(year, monthOfYear, dayOfMonth));
            Log.d("age-->", String.valueOf(age));
            tvBirthDate.setText(strBirthDate);
        }

    };

    private void gotoSelectPicture() {
        startActivityForResult(ImageSelectionMethods.getPickImageChooserIntent(this), 200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (ImageSelectionMethods.getPickImageResultUri(this, data) != null) {
                picUri = ImageSelectionMethods.getPickImageResultUri(this, data);
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
