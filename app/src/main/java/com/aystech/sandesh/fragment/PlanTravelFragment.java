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
import android.widget.ArrayAdapter;
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
import com.aystech.sandesh.model.CityModel;
import com.aystech.sandesh.model.CityResponseModel;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.StateModel;
import com.aystech.sandesh.model.StateResponseModel;
import com.aystech.sandesh.model.WeightModel;
import com.aystech.sandesh.model.WeightResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.UserSession;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanTravelFragment extends Fragment implements View.OnClickListener {

    Context context;

    WeightResponseModel weightResponseModel;
    StateResponseModel stateResponseModel;
    CityResponseModel cityResponseModel;

    private Spinner spinnerFromState, spinnerFromCity, spinnerToState, spinnerToCity;
    ImageView ingStartDate, ingStartTime;
    EditText etStartDate, etStartTime;
    ImageView ingEndDate, ingEndTime;
    EditText etEndDate, etEndTime;
    EditText etFromPincode, etToPincode, etAcceptableLength, etAcceptableBreadth, etAcceptableHeight,
            etVehicleTrainNo, etOtherDetails;
    Spinner spinnerDeliveryOption, spinnerPreferredWeight, spinnerVehicleType;
    Button btnSubmit;
    TextView btnCancel;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private int fromStateId, fromCityId, toStateId, toCityId, weight_id;

    UserSession userSession;
    ViewProgressDialog viewProgressDialog;

    String deliveryOption, modeOfTravel, strStartTime, strEndTime, strStartDate, strEndDate, strFromPincode,
            strToPincode, strAcceptableLength, strAcceptableBreadth, strAcceptableHeight, strVehicleNo, strOtherDetail;

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

        spinnerFromState = view.findViewById(R.id.spinnerFromState);
        spinnerFromCity = view.findViewById(R.id.spinnerFromCity);
        etFromPincode = view.findViewById(R.id.etFromPincode);
        spinnerToState = view.findViewById(R.id.spinnerToState);
        spinnerToCity = view.findViewById(R.id.spinnerToCity);
        etToPincode = view.findViewById(R.id.etToPincode);
        etAcceptableLength = view.findViewById(R.id.etAcceptableLength);
        etAcceptableBreadth = view.findViewById(R.id.etAcceptableBreadth);
        etAcceptableHeight = view.findViewById(R.id.etAcceptableHeight);
        etVehicleTrainNo = view.findViewById(R.id.etVehicleTrainNo);
        etOtherDetails = view.findViewById(R.id.etOtherDetails);
        spinnerDeliveryOption = view.findViewById(R.id.spinnerDeliveryOption);
        spinnerPreferredWeight = view.findViewById(R.id.spinnerPreferredWeight);
        spinnerVehicleType = view.findViewById(R.id.spinnerVehicleType);
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

        spinnerVehicleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    modeOfTravel = selectedItem;
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
                strFromPincode = etFromPincode.getText().toString();
                strToPincode = etToPincode.getText().toString();
                strAcceptableLength = etAcceptableLength.getText().toString();
                strAcceptableBreadth = etAcceptableBreadth.getText().toString();
                strAcceptableHeight = etAcceptableHeight.getText().toString();
                strVehicleNo = etVehicleTrainNo.getText().toString();
                strOtherDetail = etOtherDetails.getText().toString();

                planTravel();
                break;

            case R.id.btnCancel:
                break;
        }
    }

    private void planTravel() {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("from_city_id", fromCityId);
        jsonObject.addProperty("from_pincode", strFromPincode);
        jsonObject.addProperty("to_city_id", toCityId);
        jsonObject.addProperty("to_pincode", strToPincode);
        jsonObject.addProperty("start_date", strStartDate);
        jsonObject.addProperty("start_time", strStartTime);
        jsonObject.addProperty("end_date", strEndDate);
        jsonObject.addProperty("end_time", strEndTime);
        jsonObject.addProperty("delivery_option", deliveryOption);
        jsonObject.addProperty("preferred_weight", weight_id);
        jsonObject.addProperty("acceptable_volume_length", strAcceptableLength);
        jsonObject.addProperty("acceptable_volume_breadth", strAcceptableBreadth);
        jsonObject.addProperty("acceptable_volume_width", strAcceptableHeight);
        jsonObject.addProperty("mode_of_travel", modeOfTravel);
        jsonObject.addProperty("vehicle_number", strVehicleNo);
        jsonObject.addProperty("other_detail", strOtherDetail);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.planTravel(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        ((MainActivity) context).getSupportFragmentManager().popBackStack();
                    } else
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
                            strStartDate = Uitility.dateFormat(year, monthOfYear + 1, dayOfMonth);
                            etStartDate.setText(strStartDate);
                        } else if (tag.equals("end_date")) {
                            strEndDate = Uitility.dateFormat(year, monthOfYear + 1, dayOfMonth);
                            etEndDate.setText(strEndDate);
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
                            strStartTime = hourOfDay + ":" + minute;
                            etStartTime.setText(strStartTime);
                        } else if (tag.equals("end_time")) {
                            strEndTime = hourOfDay + ":" + minute;
                            etEndTime.setText(strEndTime);
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);

        //TODO API Call
        getWeights();

        //TODO API Call
        getState();
    }

    private void getWeights() {
        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<WeightResponseModel> call = apiInterface.getWeights();
        call.enqueue(new Callback<WeightResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<WeightResponseModel> call, @NonNull Response<WeightResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        weightResponseModel = response.body();
                        bindStateDataToSpinner(response.body().getData());
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeightResponseModel> call, @NonNull Throwable t) {
            }
        });
    }

    private void bindStateDataToSpinner(List<WeightModel> data) {
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<WeightModel> aa = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
                data);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerPreferredWeight.setAdapter(aa);

        spinnerPreferredWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    weight_id = weightResponseModel.getWeightId(selectedItem);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getState() {
        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<StateResponseModel> call = apiInterface.getState();
        call.enqueue(new Callback<StateResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<StateResponseModel> call, @NonNull Response<StateResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        stateResponseModel = response.body();
                        bindStateDataToUI(response.body().getData());
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<StateResponseModel> call, @NonNull Throwable t) {
            }
        });

    }

    private void bindStateDataToUI(List<StateModel> data) {
        ArrayList<String> manufactureArrayList = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            manufactureArrayList.add(data.get(i).getStateName());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, manufactureArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFromState.setAdapter(adapter);
        spinnerToState.setAdapter(adapter);

        spinnerFromState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    fromStateId = stateResponseModel.getStateId(selectedItem);
                    getCity(fromStateId, "from");
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerToState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    toStateId = stateResponseModel.getStateId(selectedItem);
                    getCity(toStateId, "to");
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getCity(int strStateId, final String tag) {
        if (tag.equals("to"))
            ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("state_id", strStateId);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CityResponseModel> call = apiInterface.getCity(jsonObject);
        call.enqueue(new Callback<CityResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<CityResponseModel> call, @NonNull Response<CityResponseModel> response) {
                if (tag.equals("to"))
                    viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        cityResponseModel = response.body();
                        bindCityDataToUI(response.body().getData(), tag);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CityResponseModel> call, @NonNull Throwable t) {
                if (tag.equals("to"))
                    viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindCityDataToUI(List<CityModel> data, String tag) {
        if (tag.equals("from")) {
            ArrayList<String> manufactureArrayList = new ArrayList<>();

            for (int i = 0; i < data.size(); i++) {
                manufactureArrayList.add(data.get(i).getCityName());
            }
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, manufactureArrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerFromCity.setAdapter(adapter);
        }
        if (tag.equals("to")) {
            ArrayList<String> manufactureArrayList = new ArrayList<>();

            for (int i = 0; i < data.size(); i++) {
                manufactureArrayList.add(data.get(i).getCityName());
            }
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, manufactureArrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerToCity.setAdapter(adapter);
        }

        spinnerFromCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    fromCityId = cityResponseModel.getCityId(selectedItem);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerToCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    toCityId = cityResponseModel.getCityId(selectedItem);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}

