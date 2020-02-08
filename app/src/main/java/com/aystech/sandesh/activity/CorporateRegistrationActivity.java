package com.aystech.sandesh.activity;

import android.app.Activity;
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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CorporateRegistrationActivity extends AppCompatActivity {

    private EditText etCompanyName, etBranch, etAuthorisedPersonName, etDesignation, etMobileNumber, etEmailId,
            etPassword, etReEnteredPassword, etRefferalCode;
    private String strCompanyName, strBranch, strAuthPersonName, strDesignation, strMobileNumber, strEmailId,
            strPassword, strReEnteredPassword, strRefferalCode, strFCMId = "dfjdkfjdlfkdjfdlkfj";
    private ImageView imgProfileResult;
    private LinearLayout llProfilePiture;
    private CheckBox cbAccetTermsAndConditions;
    private Button btnSubmit;

    Uri picUri;
    Bitmap myBitmap;
    private String filepath;

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
        cbAccetTermsAndConditions = findViewById(R.id.cbTermsCondition);
        imgProfileResult = findViewById(R.id.imgProfileResult);
        imgProfileResult.setImageResource(R.drawable.ic_parcel);

        llProfilePiture = findViewById(R.id.llProfilePicture);

        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void onClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmailId = etEmailId.getText().toString();
                strMobileNumber = etMobileNumber.getText().toString();
                strPassword = etPassword.getText().toString();
                strReEnteredPassword = etReEnteredPassword.getText().toString();
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
                                    if (Uitility.isValidEmailId(strEmailId)) {
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

    private void doRigistrationAPICall() {
        ViewProgressDialog.getInstance().showProgress(this);

        RequestBody emailIdPart = RequestBody.create(MultipartBody.FORM, strEmailId);
        RequestBody mobileNoPart = RequestBody.create(MultipartBody.FORM, strMobileNumber);
        RequestBody passwordPart = RequestBody.create(MultipartBody.FORM, strPassword);
        RequestBody refferalCodePart = RequestBody.create(MultipartBody.FORM, strRefferalCode);
        RequestBody fcmIdPart = RequestBody.create(MultipartBody.FORM, strFCMId);
        RequestBody companyNamePart = RequestBody.create(MultipartBody.FORM, strCompanyName);
        RequestBody branchPart = RequestBody.create(MultipartBody.FORM, strBranch);
        RequestBody authPersonNamePart = RequestBody.create(MultipartBody.FORM, strAuthPersonName);
        RequestBody designationPart = RequestBody.create(MultipartBody.FORM, strDesignation);

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
        Call<CommonResponse> call = apiInterface.doCorporateUserRegistration(
                emailIdPart,
                mobileNoPart,
                passwordPart,
                refferalCodePart,
                fcmIdPart,
                companyNamePart,
                branchPart,
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
                        Constants.fragmentType = "Dashboard";
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
