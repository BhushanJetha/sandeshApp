package com.aystech.sandesh.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
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
import com.aystech.sandesh.activity.IndividualRegistrationActivity;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.UserModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.AppController;
import com.aystech.sandesh.utils.Connectivity;
import com.aystech.sandesh.utils.ImageSelectionMethods;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    private ConstraintLayout clProfileImage;
    private ImageView imgUserProfile, imgUserProfileCamera;
    private RadioGroup rgGender;
    private RadioButton rbGender;
    private EditText etFirstName, etMiddleName, etLastName, etEmialId;
    private RadioButton rbMale, rbFemale, rbOther;
    private TextView tvBirthDate;
    private LinearLayout llDateOfBirth;
    private Button btnUpdate;

    private String strFirstName, strMiddleName, strLastName, strEmailId, strGender, strDateOfBirth;

    final Calendar myCalendar = Calendar.getInstance();

    private int age = 0;

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

        clProfileImage = view.findViewById(R.id.clProfileImage);
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

        //to find age
        findOutYearDayMonth(strDateOfBirth);
    }

    private void findOutYearDayMonth(String strDateOfBirth) {
        int month = 0, dd = 0, year = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(strDateOfBirth);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            month = Integer.parseInt(checkDigit(cal.get(Calendar.MONTH)+1));
            dd = Integer.parseInt(checkDigit(cal.get(Calendar.DATE)));
            year = Integer.parseInt(checkDigit(cal.get(Calendar.YEAR)));

            age = Integer.parseInt(Uitility.getAge(year, month, dd));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String checkDigit (int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
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
        clProfileImage.setOnClickListener(this);
        imgUserProfile.setOnClickListener(this);
        imgUserProfileCamera.setOnClickListener(this);
    }

    private void openDatePickerDialog() {
        DatePickerDialog mDate = new DatePickerDialog(context, date, myCalendar
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

            strDateOfBirth = Uitility.dateFormat(year, monthOfYear, dayOfMonth); //UpdateUserProfileFragment
            age = Integer.parseInt(Uitility.getAge(year, monthOfYear, dayOfMonth));
            tvBirthDate.setText(strDateOfBirth);
        }

    };

    private void updateProfile() {
        viewProgressDialog.showProgress(context);

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
            body = MultipartBody.Part.createFormData("profile_pic", file.getName(), requestBody);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            body = MultipartBody.Part.createFormData("profile_pic", "", requestBody);
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

                if(!strFirstName.isEmpty()){
                    if(!strLastName.isEmpty()){
                        if(!strEmailId.isEmpty()){
                            if (Uitility.isValidEmailId(strEmailId)){
                                if(!strDateOfBirth.isEmpty()){
                                    if(age >= 18){
                                        if(Connectivity.isConnected(context)) {
                                            //TODO API Call
                                            updateProfile();
                                        }
                                    }else {
                                        Uitility.showToast(getActivity(), "Sorry you are not able to register, your age is below 18 !");
                                    }
                                }else {
                                    Uitility.showToast(getActivity(), "Please select your date of birth !");
                                }
                            }else {
                                Uitility.showToast(getActivity(), "Please enter valid email id  !");
                            }
                        }else {
                            Uitility.showToast(getActivity(), "Please enter your email Id !");
                        }
                    }else {
                        Uitility.showToast(getActivity(), "Please enter your last name !");
                    }
                }else {
                    Uitility.showToast(getActivity(), "Please enter your first name !");
                }

                break;

            case R.id.llDateOfBirth:
                openDatePickerDialog();
                break;

            case R.id.clProfileImage:
            case R.id.imgUserProfile:
            case R.id.imgUserProfileCamera:
                gotoSelectPicture();
                break;
        }
    }

    private void gotoSelectPicture() {
        startActivityForResult(ImageSelectionMethods.getPickImageChooserIntent(context), 200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (ImageSelectionMethods.getPickImageResultUri(context, data) != null) {
                picUri = ImageSelectionMethods.getPickImageResultUri(context, data);
                filepath = ImageSelectionMethods.getPath(context, picUri);

                if (filepath.equals("Not found")) {
                    filepath = picUri.getPath();
                }

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), picUri);
                    myBitmap = ImageSelectionMethods.getResizedBitmap(myBitmap, 500);

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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
