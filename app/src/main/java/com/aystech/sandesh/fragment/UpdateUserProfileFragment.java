package com.aystech.sandesh.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserProfileFragment extends Fragment {

    private Context context;
    View view;

    private UserModel userModel;

    private RadioGroup rgGender;
    private RadioButton rbGender;
    private EditText etFirstName, etMiddleName, etLastName, etEmialId;
    private RadioButton rbMale, rbFemale, rbOther;
    private TextView tvBirthDate, tvCancel;
    private LinearLayout llDateOfBirth;
    private String strFirstName, strMiddleName, strLastName, strEmailId, strGender, strDateOfBirth, strProfilePicture;
    private Button btnUpdate;
    private int mYear, mMonth, mDay;

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

        tvCancel = view.findViewById(R.id.btnCancel);
        btnUpdate = view.findViewById(R.id.btnUpdate);
    }

    private void setDataToUI() {
        //this is for profile
        if (userModel.getFirstName() != null
                && !userModel.getFirstName().equals("")) {
            etFirstName.setText(userModel.getFirstName());
        }
        if ((userModel.getMiddleName() != null &&
                !userModel.getMiddleName().equals(""))) {
            etMiddleName.setText(userModel.getMiddleName());
        }
        if ((userModel.getLastName() != null &&
                !userModel.getLastName().equals(""))) {
            etLastName.setText(userModel.getLastName());

        }
        etEmialId.setText(userModel.getEmailId());
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
    }

    private void onClick() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strFirstName = etFirstName.getText().toString();
                strMiddleName = etMiddleName.getText().toString();
                strLastName = etLastName.getText().toString();
                strEmailId = etEmialId.getText().toString();

                //TODO API Call
                updateProfile();
            }
        });

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

        llDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog();
            }
        });
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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }

    private void updateProfile() {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("first_name", strFirstName);
        jsonObject.addProperty("middle_name", strMiddleName);
        jsonObject.addProperty("last_name", strLastName);
        jsonObject.addProperty("gender", strGender);
        jsonObject.addProperty("birth_date", strDateOfBirth);
        jsonObject.addProperty("file", "");

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.updateProfile(jsonObject);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {

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

}
