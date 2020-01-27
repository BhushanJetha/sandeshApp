package com.aystech.sandesh.fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.UserSession;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanTravelFragment extends Fragment implements View.OnClickListener {

    Context context;

    ImageView ingStartDate, ingStartTime;
    EditText etStartDate, etStartTime;
    ImageView ingEndDate, ingEndTime;
    EditText etEndDate, etEndTime;
    EditText etFrom, etFromPincode, etTo, etToPincode, etVehicleTrainNo, etOtherDetails;
    Spinner spinnerDeliveryOption, spinnerPreferredWeight;
    Button btnSubmit;
    TextView btnCancel;

    private int mYear, mMonth, mDay, mHour, mMinute;

    UserSession userSession;
    ViewProgressDialog viewProgressDialog;

    String deliveryOption, preferredWeight;

    public PlanTravelFragment() {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan_travel, container, false);

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        userSession = new UserSession(context);
        viewProgressDialog = ViewProgressDialog.getInstance();

        etFrom = view.findViewById(R.id.etFrom);
        etFromPincode = view.findViewById(R.id.etFromPincode);
        etTo = view.findViewById(R.id.etTo);
        etToPincode = view.findViewById(R.id.etToPincode);
        etVehicleTrainNo = view.findViewById(R.id.etVehicleTrainNo);
        etOtherDetails = view.findViewById(R.id.etOtherDetails);
        spinnerDeliveryOption = view.findViewById(R.id.spinnerDeliveryOption);
        spinnerPreferredWeight = view.findViewById(R.id.spinnerPreferredWeight);
        ingStartDate = view.findViewById(R.id.ingStartDate);
        ingStartTime = view.findViewById(R.id.ingStartTime);
        etStartDate = view.findViewById(R.id.etStartDate);
        etStartTime = view.findViewById(R.id.etStartTime);
        ingEndDate = view.findViewById(R.id.ingEndDate);
        ingEndTime = view.findViewById(R.id.ingEndTime);
        etEndDate = view.findViewById(R.id.etEndDate);
        etEndTime = view.findViewById(R.id.etEndTime);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnSubmit = view.findViewById(R.id.btnSubmit);
    }

    private void onClickListener() {
        ingStartDate.setOnClickListener(this);
        ingStartTime.setOnClickListener(this);
        ingEndDate.setOnClickListener(this);
        ingEndTime.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        spinnerDeliveryOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    deliveryOption = selectedItem;
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPreferredWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    preferredWeight = selectedItem;
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ingStartDate:
                openDatePickerDialog("start_date");
                break;
            case R.id.ingStartTime:
                openTimePickerDialog("start_time");
                break;
            case R.id.ingEndDate:
                openDatePickerDialog("end_date");
                break;
            case R.id.ingEndTime:
                openTimePickerDialog("end_time");
                break;

            case R.id.btnSubmit:
                //TODO API Call
                planTravel();
                break;

            case R.id.btnCancel:
                break;
        }
    }

    private void planTravel() {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("from_city_id", "1");
        jsonObject.addProperty("from_pincode", etFromPincode.getText().toString());
        jsonObject.addProperty("to_city_id", "1");
        jsonObject.addProperty("to_pincode", etToPincode.getText().toString());
        jsonObject.addProperty("start_date", etStartDate.getText().toString());
        jsonObject.addProperty("start_time", etStartTime.getText().toString());
        jsonObject.addProperty("end_date", etEndDate.getText().toString());
        jsonObject.addProperty("end_time", etEndTime.getText().toString());
        jsonObject.addProperty("delivery_option", deliveryOption);
        jsonObject.addProperty("preferred_weight", preferredWeight);
        jsonObject.addProperty("acceptable_volume_length", "");
        jsonObject.addProperty("acceptable_volume_breadth", "");
        jsonObject.addProperty("acceptable_volume_width", "");
        jsonObject.addProperty("mode_of_travel", "");
        jsonObject.addProperty("vehicle_train_number", etVehicleTrainNo.getText().toString());
        jsonObject.addProperty("other_detail", etOtherDetails.getText().toString());

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.planTravel(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus())
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void openDatePickerDialog(final String tag) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (tag.equals("start_date")) {
                            etStartDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        } else if (tag.equals("end_date")) {
                            etEndDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void openTimePickerDialog(final String tag) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (tag.equals("start_time")) {
                            etStartTime.setText(hourOfDay + ":" + minute);
                        } else if (tag.equals("end_time")) {
                            etEndTime.setText(hourOfDay + ":" + minute);
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
