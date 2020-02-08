package com.aystech.sandesh.fragment;

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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.UserModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.AppController;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.bumptech.glide.Glide;

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

public class UpdateUserProfileFragment extends Fragment implements View.OnClickListener {

    private Context context;
    View view;

    private UserModel userModel;

    private ImageView imgUserProfile, imgUserProfileCamera;
    private RadioGroup rgGender;
    private RadioButton rbGender;
    private EditText etFirstName, etMiddleName, etLastName, etEmialId;
    private RadioButton rbMale, rbFemale, rbOther;
    private TextView tvBirthDate;
    private LinearLayout llDateOfBirth;
    private Button btnUpdate;

    private String strFirstName, strMiddleName, strLastName, strEmailId, strGender, strDateOfBirth;
    private int mYear, mMonth, mDay;

    Uri picUri;
    Bitmap myBitmap;
    private String filepath;

    private ViewProgressDialog viewProgressDialog;

    public UpdateUserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_user_profile, container, false);

        if (getArguments() != null)
            userModel = getArguments().getParcelable("userModel");

        initView(view);

        setDataToUI();

        onClick();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        imgUserProfile = view.findViewById(R.id.imgUserProfile);
        imgUserProfileCamera = view.findViewById(R.id.imgUserProfileCamera);
        etFirstName = view.findViewById(R.id.etFirstName);
        etMiddleName = view.findViewById(R.id.etMiddleName);
        etLastName = view.findViewById(R.id.etLastName);
        etEmialId = view.findViewById(R.id.etEmailId);
        rgGender = view.findViewById(R.id.rgGender);
        rbGender = view.findViewById(R.id.rbGender);
        rbMale = view.findViewById(R.id.rbMale);
        rbFemale = view.findViewById(R.id.rbFemale);
        rbOther = view.findViewById(R.id.rbOther);
        tvBirthDate = view.findViewById(R.id.tvBirthDate);
        llDateOfBirth = view.findViewById(R.id.llDateOfBirth);

        btnUpdate = view.findViewById(R.id.btnUpdate);
    }

    private void setDataToUI() {
        //this is for profile
        Glide.with(context)
                .load(AppController.imageURL + userModel.getProfileImg())
                .error(R.drawable.ic_logo_sandesh)
                .into(imgUserProfile);

        if (userModel.getFirstName() != null
                && !userModel.getFirstName().equals("")) {
            etFirstName.setText(userModel.getFirstName());
        }
        strFirstName = userModel.getFirstName();

        if ((userModel.getMiddleName() != null &&
                !userModel.getMiddleName().equals(""))) {
            etMiddleName.setText(userModel.getMiddleName());
        }
        strMiddleName = userModel.getMiddleName();

        if ((userModel.getLastName() != null &&
                !userModel.getLastName().equals(""))) {
            etLastName.setText(userModel.getLastName());
        }
        strLastName = userModel.getLastName();

        etEmialId.setText(userModel.getEmailId());
        strEmailId = userModel.getEmailId();

        tvBirthDate.setText(userModel.getBirthDate());
        if (userModel.getGender().equals("Male")) {
            rbMale.setChecked(true);
        }
        if (userModel.getGender().equals("Female")) {
            rbFemale.setChecked(true);
        }
        if (userModel.getGender().equals("Other")) {
            rbOther.setChecked(true);
        }
        strGender = userModel.getGender();
        strDateOfBirth = userModel.getBirthDate();
    }

    private void onClick() {
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // get selected radio button from radioGroup
                int selectedId = group.getCheckedRadioButtonId();

                if (selectedId != -1) {
                    // find the radiobutton by returned id
                    rbGender = view.findViewById(selectedId);

                    strGender = rbGender.getText().toString();
                }
            }
        });

        btnUpdate.setOnClickListener(this);
        llDateOfBirth.setOnClickListener(this);
        imgUserProfile.setOnClickListener(this);
        imgUserProfileCamera.setOnClickListener(this);
    }

    private void openDatePickerDialog() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        strDateOfBirth = Uitility.dateFormat(year, monthOfYear + 1, dayOfMonth);
                        tvBirthDate.setText(strDateOfBirth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void updateProfile() {
        ViewProgressDialog.getInstance().showProgress(context);

        RequestBody emailIdPart = RequestBody.create(MultipartBody.FORM, strEmailId);
        RequestBody firstNamePart = RequestBody.create(MultipartBody.FORM, strFirstName);
        RequestBody middleNamePart = RequestBody.create(MultipartBody.FORM, strMiddleName);
        RequestBody lastNamePart = RequestBody.create(MultipartBody.FORM, strLastName);
        RequestBody genderPart = RequestBody.create(MultipartBody.FORM, strGender);
        RequestBody birthDatePart = RequestBody.create(MultipartBody.FORM, strDateOfBirth);

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
        Call<CommonResponse> call = apiInterface.updateUserProfile(
                emailIdPart,
                firstNamePart,
                middleNamePart,
                lastNamePart,
                genderPart,
                birthDatePart,
                body
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        ((MainActivity) context).getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                strFirstName = etFirstName.getText().toString();
                strMiddleName = etMiddleName.getText().toString();
                strLastName = etLastName.getText().toString();
                strEmailId = etEmialId.getText().toString();

                //TODO API Call
                updateProfile();
                break;

            case R.id.llDateOfBirth:
                openDatePickerDialog();
                break;

            case R.id.imgUserProfile:
            case R.id.imgUserProfileCamera:
                gotoSelectPicture();
                break;
        }
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
        List<ResolveInfo> listCam = context.getPackageManager().queryIntentActivities(captureIntent, 0);
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
        List<ResolveInfo> listGallery = context.getPackageManager().queryIntentActivities(galleryIntent, 0);
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
        File getImage = context.getExternalCacheDir();
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
                filepath = getPath(context, picUri);

                if (filepath.equals("Not found")) {
                    filepath = picUri.getPath();
                }

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), picUri);
                    myBitmap = getResizedBitmap(myBitmap, 500);

                    Glide.with(imgUserProfile.getContext())
                            .asBitmap()
                            .load(myBitmap)
                            .into(imgUserProfile);
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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
