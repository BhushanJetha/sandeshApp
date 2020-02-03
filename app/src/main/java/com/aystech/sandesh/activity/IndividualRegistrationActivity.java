package com.aystech.sandesh.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndividualRegistrationActivity extends AppCompatActivity {

    private EditText etFirstName, etMiddleName, etLastName, etEmailId, etPassword, etReEnteredPassword, etRefferalCode;
    private String strFirstName, strMiddleName, strLastName, strEmailId, strPassword, strReEnteredPassword,
            strRefferalCode, strMobileNumber, strFCMId, strGender, strBirthDate;
    private RadioButton rbMale, rbFemale, rbOther;
    private CheckBox cbAccetTermsAndConditions;
    private TextView tvBirthDate, tvAcceptTermsAndCondition;
    private ImageView imgProfileShow, imgProfileResult;
    private Button btnSubmit;
    private LinearLayout llDateOfBirth, llProfilePiture;
    private int mYear, mMonth, mDay;
    Uri picUri;
    Bitmap myBitmap;
    private String strProfileBase64;

    private ViewProgressDialog viewProgressDialog;

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
        imgProfileShow = findViewById(R.id.imgProfileShow);
        imgProfileResult = findViewById(R.id.imgProfileResult);
        btnSubmit = findViewById(R.id.btnSubmit);
        llDateOfBirth = findViewById(R.id.llDateOfBirth);
        tvAcceptTermsAndCondition =  findViewById(R.id.tvTermsCondition);

        llProfilePiture = findViewById(R.id.llProfilePicture);

        strMobileNumber = getIntent().getStringExtra("mobileNumber");

        SpannableString ssAcceptTermsAndCondition = new SpannableString(getResources().getString(R.string.accept_terms_amp_conditions));
        ssAcceptTermsAndCondition.setSpan(new UnderlineSpan(), 0, ssAcceptTermsAndCondition.length(), 0);
        tvAcceptTermsAndCondition.setText(ssAcceptTermsAndCondition);
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
                                                doRegistrationAPICall();
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

    private void doRegistrationAPICall() {
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
        jsonObject.addProperty("profile_img", strProfileBase64);
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
                        Constants.fragmentType = "Dashboard";
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

                        if(String.valueOf(monthOfYear +1).length() ==  1){
                            strBirthDate = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }else {
                            strBirthDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        }

                        tvBirthDate.setText(strBirthDate);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void gotoSelectPicture() {
        startActivityForResult(getPickImageChooserIntent(), 200);
    }

    private Intent getPickImageChooserIntent() {
        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = getPackageManager().queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = getPackageManager().queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (Objects.requireNonNull(intent.getComponent()).getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {
            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    myBitmap = getResizedBitmap(myBitmap, 500);

                    strProfileBase64 = getStringImage(myBitmap);

                    imgProfileShow.setVisibility(View.GONE);
                    imgProfileResult.setVisibility(View.VISIBLE);
                    imgProfileResult.setImageBitmap(myBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = (Bitmap) data.getExtras().get("data");
                myBitmap = bitmap;

                strProfileBase64 = getStringImage(myBitmap);

                imgProfileShow.setVisibility(View.GONE);
                imgProfileResult.setVisibility(View.VISIBLE);
                imgProfileResult.setImageBitmap(myBitmap);
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.<br />
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }

        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
