package com.aystech.sandesh.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndividualRegistrationActivity extends AppCompatActivity {

    private EditText etFirstName, etMiddleName, etLastName, etEmailId, etPassword, etReEnteredPassword, etRefferalCode;
    private String strFirstName, strMiddleName, strLastName, strEmailId, strPassword, strReEnteredPassword,
            strRefferalCode, strMobileNumber, strFCMId = "kdgjflgfjlfjglfgjflgj", strGender, strBirthDate;
    private RadioButton rbMale, rbFemale, rbOther;
    private CheckBox cbAccetTermsAndConditions;
    private TextView tvBirthDate, tvAcceptTermsAndCondition;
    private ImageView imgProfileResult;
    private Button btnSubmit;
    private LinearLayout llDateOfBirth, llProfilePiture;

    private int mYear, mMonth, mDay;

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

        RequestBody emailIdPart = RequestBody.create(MultipartBody.FORM, strEmailId);
        RequestBody mobileNoPart = RequestBody.create(MultipartBody.FORM, strMobileNumber);
        RequestBody firstNamePart = RequestBody.create(MultipartBody.FORM, strFirstName);
        RequestBody middleNamePart = RequestBody.create(MultipartBody.FORM, strMiddleName);
        RequestBody lastNamePart = RequestBody.create(MultipartBody.FORM, strLastName);
        RequestBody passwordPart = RequestBody.create(MultipartBody.FORM, strPassword);
        RequestBody genderPart = RequestBody.create(MultipartBody.FORM, strGender);
        RequestBody birthDatePart = RequestBody.create(MultipartBody.FORM, strBirthDate);
        RequestBody refferalCodePart = RequestBody.create(MultipartBody.FORM, strRefferalCode);
        RequestBody fcmIdPart = RequestBody.create(MultipartBody.FORM, strFCMId);

        MultipartBody.Part body;
        if (filepath != null && !filepath.equals("")) {
            File file = new File(filepath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            body = MultipartBody.Part.createFormData("file", "", requestBody);
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
                fcmIdPart,
                body
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

                        if (String.valueOf(monthOfYear + 1).length() == 1) {
                            strBirthDate = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                        } else {
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

        if (resultCode == Activity.RESULT_OK) {
            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);
                filepath = getPath(getApplicationContext(), picUri);

                if (filepath.equals("Not found")) {
                    filepath = picUri.getPath();
                }

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri);
                    myBitmap = getResizedBitmap(myBitmap, 500);

                    imgProfileResult.setImageBitmap(myBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
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
}
