package com.aystech.sandesh.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.AadhaarDetailsResponseModel;
import com.aystech.sandesh.model.CityModel;
import com.aystech.sandesh.model.CityResponseModel;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.DeliveryOptionResponseModel;
import com.aystech.sandesh.model.StateModel;
import com.aystech.sandesh.model.StateResponseModel;
import com.aystech.sandesh.model.TravelDetailModel;
import com.aystech.sandesh.model.VehicleResponseModel;
import com.aystech.sandesh.model.WeightResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Connectivity;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.UserSession;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanTravelFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private DeliveryOptionResponseModel deliveryOptionResponseModel;
    private VehicleResponseModel vehicleResponseModel;
    private WeightResponseModel weightResponseModel;
    private StateResponseModel stateResponseModel;
    private CityResponseModel cityResponseModel;
    private TravelDetailModel travelDetailModel;

    private DashboardFragment dashboardFragment;

    private Spinner spinnerFromState, spinnerFromCity, spinnerToState, spinnerToCity;
    private ImageView ingStartDate, ingStartTime;
    private EditText etStartDate, etStartTime;
    private ImageView ingEndDate, ingEndTime;
    private EditText etEndDate, etEndTime;
    private EditText etFromPincode, etToPincode, etAcceptableLength, etAcceptableBreadth, etAcceptableHeight,
            etVehicleTrainNo, etOtherDetails;
    private Spinner spinnerDeliveryOption, spinnerPreferredWeight, spinnerVehicleType;
    private Button btnSubmit;
    private TextView btnCancel;
    private CheckBox cbTermsCondition;

    private String tag, edit, fromState, fromCity, toState, toCity;
    private int mHour, mMinute;
    private int travelId, fromStateId, fromCityId, toStateId, toCityId, weight_id;

    final Calendar myCalendar = Calendar.getInstance();

    private ViewProgressDialog viewProgressDialog;
    private UserSession userSession;

    private String deliveryOption, modeOfTravel, strStartTime = "", strEndTime = "", strStartDate = "", strEndDate = "",
            strFromPincode, strToPincode, strAcceptableLength, strAcceptableBreadth, strAcceptableHeight, strVehicleNo,
            strOtherDetail, strWeight;
    private int startYear, startMonth, startDay;

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

        dashboardFragment = (DashboardFragment) Fragment.instantiate(context,
                DashboardFragment.class.getName());

        if (getArguments() != null) {
            travelDetailModel = getArguments().getParcelable("travel_detail");
            edit = getArguments().getString("tag");
        }

        initView(view);

        onClickListener();

        if (edit != null && !edit.equals("")) {
            if (edit.equals("edit"))
                editPlanTravel();
        }

        String weight = userSession.getWeight();
        if (weight.length() > 0) {
            Gson gson = new Gson();
            weightResponseModel = gson.fromJson(weight, WeightResponseModel.class);
            ArrayList<String> weightArrayList = new ArrayList<>();
            weightArrayList.add(0, "Select Weight");
            for (int i = 0; i < weightResponseModel.getData().size(); i++) {
                weightArrayList.add(weightResponseModel.getData().get(i).getWeight());
                bindWeightDataToSpinner(weightArrayList);
            }
        } else {
            //TODO API Call
            getWeights();
        }

        String fromState = userSession.getFromState();
        if (fromState.length() > 0) {
            Gson gson = new Gson();
            stateResponseModel = gson.fromJson(fromState, StateResponseModel.class);
            bindStateDataToUI(stateResponseModel.getData());
        } else {
            //TODO API Call
            getState();
        }

        String deliveryOption = userSession.getDeliveryOption();
        if (deliveryOption.length() > 0) {
            Gson gson = new Gson();
            deliveryOptionResponseModel = gson.fromJson(deliveryOption, DeliveryOptionResponseModel.class);
            ArrayList<String> deliveryOptionArrayList = new ArrayList<>();
            deliveryOptionArrayList.add(0, "Select Delivery Option");
            for (int i = 0; i < deliveryOptionResponseModel.getData().size(); i++) {
                deliveryOptionArrayList.add(deliveryOptionResponseModel.getData().get(i).getDeliveryOption());
                bindDeliveryOptionDataToSpinner(deliveryOptionArrayList);
            }
        } else {
            //TODO API Call
            getDeliveryOption();
        }

        String vehicleType = userSession.getVehicleType();
        if (vehicleType.length() > 0) {
            Gson gson = new Gson();
            vehicleResponseModel = gson.fromJson(vehicleType, VehicleResponseModel.class);
            ArrayList<String> vehicleTypeArrayList = new ArrayList<>();
            vehicleTypeArrayList.add(0, "Select Vehicle Type");
            for (int i = 0; i < vehicleResponseModel.getData().size(); i++) {
                vehicleTypeArrayList.add(vehicleResponseModel.getData().get(i).getVehicleType());
                bindVehicleTypeDataToSpinner(vehicleTypeArrayList);
            }
        } else {
            //TODO API Call
            getVehicleType();
        }

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();
        userSession = new UserSession(context);

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
        cbTermsCondition = view.findViewById(R.id.cbTermsCondition);
    }

    private void editPlanTravel() {
        fromState = travelDetailModel.getTravelPlan().getFrom_state();
        fromCity = travelDetailModel.getTravelPlan().getFromCity();
        toState = travelDetailModel.getTravelPlan().getTo_state();
        toCity = travelDetailModel.getTravelPlan().getToCity();
        etToPincode.setText(travelDetailModel.getTravelPlan().getToPincode());
        etFromPincode.setText(travelDetailModel.getTravelPlan().getFromPincode());
        etStartDate.setText(travelDetailModel.getTravelPlan().getStartDate());
        etStartTime.setText(travelDetailModel.getTravelPlan().getStartTime());
        etEndDate.setText(travelDetailModel.getTravelPlan().getEndDate());
        etEndTime.setText(travelDetailModel.getTravelPlan().getEndTime());
        etAcceptableLength.setText(travelDetailModel.getTravelPlan().getAcceptableVolumeLength());
        etAcceptableBreadth.setText(travelDetailModel.getTravelPlan().getAcceptableVolumeBreadth());
        etAcceptableHeight.setText(travelDetailModel.getTravelPlan().getAcceptableVolumeWidth());
        etVehicleTrainNo.setText(travelDetailModel.getTravelPlan().getVehicleTrainNumber());
        etOtherDetails.setText(travelDetailModel.getTravelPlan().getOtherDetail());
        deliveryOption = travelDetailModel.getTravelPlan().getDeliveryOption();
        strWeight = travelDetailModel.getTravelPlan().getPreferredWeight();
        modeOfTravel = travelDetailModel.getTravelPlan().getModeOfTravel();

        cbTermsCondition.setChecked(true);

        btnSubmit.setText("Update");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strFromPincode = etFromPincode.getText().toString();
                strToPincode = etToPincode.getText().toString();
                strStartDate = etStartDate.getText().toString();
                strStartTime = etStartTime.getText().toString();
                strEndDate = etEndDate.getText().toString();
                strEndTime = etEndTime.getText().toString();
                strAcceptableLength = etAcceptableLength.getText().toString();
                strAcceptableBreadth = etAcceptableBreadth.getText().toString();
                strAcceptableHeight = etAcceptableHeight.getText().toString();
                strVehicleNo = etVehicleTrainNo.getText().toString();
                strOtherDetail = etOtherDetails.getText().toString();

                if (fromCityId != 0) {
                    if (!strFromPincode.isEmpty()) {
                        if (strFromPincode.length() == 6) {
                            if (toCityId != 0) {
                                if (!strToPincode.isEmpty()) {
                                    if (strToPincode.length() == 6) {
                                        if (!strStartDate.isEmpty()) {
                                            if (!strEndDate.isEmpty()) {
                                                if (!strStartTime.isEmpty()) {
                                                    if (!strEndTime.isEmpty()) {
                                                        if (!deliveryOption.equals("Select Delivery Option")) {
                                                            if (weight_id != 0) {
                                                                if (!strAcceptableLength.isEmpty()) {
                                                                    if (!strAcceptableBreadth.isEmpty()) {
                                                                        if (!strAcceptableHeight.isEmpty()) {
                                                                            if (!modeOfTravel.equals("Select Vehicle Type")) {
                                                                                if (!strVehicleNo.isEmpty()) {
                                                                                    if (cbTermsCondition.isChecked()) {
                                                                                        travelId = travelDetailModel.getTravelPlan().getTravelId();
                                                                                        if (Connectivity.isConnected(context)) {
                                                                                            //TODO API Call
                                                                                            updateMyTravel();
                                                                                        }
                                                                                    } else {
                                                                                        Uitility.showToast(context, "Please accept terms and condition!");
                                                                                    }
                                                                                } else {
                                                                                    Uitility.showToast(context, "Please enter vehicle number!");
                                                                                }
                                                                            } else {
                                                                                Uitility.showToast(context, "Please select vehicle type!");
                                                                            }
                                                                        } else {
                                                                            Uitility.showToast(context, "Please enter height of parcel!");
                                                                        }
                                                                    } else {
                                                                        Uitility.showToast(context, "Please enter breadth of parcel!");
                                                                    }
                                                                } else {
                                                                    Uitility.showToast(context, "Please enter length of parcel!");
                                                                }
                                                            } else {
                                                                Uitility.showToast(context, "Please select Preferred weight option!");
                                                            }

                                                        } else {
                                                            Uitility.showToast(context, "Please select delivery option!");
                                                        }
                                                    } else {
                                                        Uitility.showToast(context, "Please select end time!");
                                                    }
                                                } else {
                                                    Uitility.showToast(context, "Please select start time!");
                                                }
                                            } else {
                                                Uitility.showToast(context, "Please select end date!");
                                            }
                                        } else {
                                            Uitility.showToast(context, "Please select start date!");
                                        }
                                    } else {
                                        Uitility.showToast(context, "Please enter 6 digit pin code!");
                                    }
                                } else {
                                    Uitility.showToast(context, "Please enter to city pin code!");
                                }
                            } else {
                                Uitility.showToast(context, "Please select to city!");
                            }
                        } else {
                            Uitility.showToast(context, "Please enter 6 digit pin code!");
                        }
                    } else {
                        Uitility.showToast(context, "Please enter from city pin code!");
                    }
                } else {
                    Uitility.showToast(context, "Please select from city!");
                }
            }
        });
    }

    private void onClickListener() {
        ingStartDate.setOnClickListener(this);
        ingStartTime.setOnClickListener(this);
        ingEndDate.setOnClickListener(this);
        ingEndTime.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        cbTermsCondition.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ingStartDate:
                tag = "start_date";
                openDatePickerDialog();
                break;
            case R.id.ingStartTime:
                openTimePickerDialog("start_time");
                break;
            case R.id.ingEndDate:
                tag = "end_date";
                openDatePickerDialog();
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

                if (fromCityId != 0) {
                    if (!strFromPincode.isEmpty()) {
                        if (strFromPincode.length() == 6) {
                            if (toCityId != 0) {
                                if (!strToPincode.isEmpty()) {
                                    if (strToPincode.length() == 6) {
                                        if (!strStartDate.isEmpty()) {
                                            if (!strEndDate.isEmpty()) {
                                                if (!strStartTime.isEmpty()) {
                                                    if (!strEndTime.isEmpty()) {
                                                        if (!deliveryOption.equals("Select Delivery Option")) {
                                                            if (weight_id != 0) {
                                                                if (!strAcceptableLength.isEmpty()) {
                                                                    if (!strAcceptableBreadth.isEmpty()) {
                                                                        if (!strAcceptableHeight.isEmpty()) {
                                                                            if (!modeOfTravel.equals("Select Vehicle Type")) {
                                                                                if (!strVehicleNo.isEmpty()) {
                                                                                    if (cbTermsCondition.isChecked()) {
                                                                                        if (Connectivity.isConnected(context)) {
                                                                                            //TODO API Call
                                                                                            planTravel();
                                                                                        }
                                                                                    } else {
                                                                                        Uitility.showToast(context, "Please accept terms and condition!");
                                                                                    }
                                                                                } else {
                                                                                    Uitility.showToast(context, "Please enter vehicle number!");
                                                                                }
                                                                            } else {
                                                                                Uitility.showToast(context, "Please select vehicle type!");
                                                                            }
                                                                        } else {
                                                                            Uitility.showToast(context, "Please enter height of parcel!");
                                                                        }
                                                                    } else {
                                                                        Uitility.showToast(context, "Please enter breadth of parcel!");
                                                                    }
                                                                } else {
                                                                    Uitility.showToast(context, "Please enter length of parcel!");
                                                                }
                                                            } else {
                                                                Uitility.showToast(context, "Please select Preferred weight option!");
                                                            }

                                                        } else {
                                                            Uitility.showToast(context, "Please select delivery option!");
                                                        }
                                                    } else {
                                                        Uitility.showToast(context, "Please select end time!");
                                                    }
                                                } else {
                                                    Uitility.showToast(context, "Please select start time!");
                                                }
                                            } else {
                                                Uitility.showToast(context, "Please select end date!");
                                            }
                                        } else {
                                            Uitility.showToast(context, "Please select start date!");
                                        }
                                    } else {
                                        Uitility.showToast(context, "Please enter 6 digit pin code!");
                                    }
                                } else {
                                    Uitility.showToast(context, "Please enter to city pin code!");
                                }
                            } else {
                                Uitility.showToast(context, "Please select to city!");
                            }
                        } else {
                            Uitility.showToast(context, "Please enter 6 digit pin code!");
                        }
                    } else {
                        Uitility.showToast(context, "Please enter from city pin code!");
                    }
                } else {
                    Uitility.showToast(context, "Please select from city!");
                }

                break;

            case R.id.btnCancel:
                //((MainActivity) context).getSupportFragmentManager().popBackStack();
                etToPincode.setText("");
                etFromPincode.setText("");
                etStartDate.setText("");
                etEndDate.setText("");
                etStartTime.setText("");
                etEndTime.setText("");
                etAcceptableLength.setText("");
                etAcceptableHeight.setText("");
                etAcceptableBreadth.setText("");
                etVehicleTrainNo.setText("");
                etOtherDetails.setText("");
                spinnerFromState.setSelection(0);
                spinnerFromCity.setSelection(0);
                spinnerToState.setSelection(0);
                spinnerToCity.setSelection(0);
                spinnerDeliveryOption.setSelection(0);
                spinnerPreferredWeight.setSelection(0);
                spinnerVehicleType.setSelection(0);
                cbTermsCondition.setChecked(false);
                break;

            case R.id.cbTermsCondition:
                cbTermsCondition.setChecked(true);
                showTermsConditions();
                break;
        }
    }

    private void showTermsConditions() {
        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.dialog_terms_condition, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final AlertDialog dialog = alert.create();
        dialog.show();

        WebView wvTermsConditions = alertLayout.findViewById(R.id.wvTermsCondition);
        // displaying content in WebView from html file that stored in assets folder
        wvTermsConditions.getSettings().setJavaScriptEnabled(true);
        wvTermsConditions.loadUrl("http://app.avantikasandesh.com/api/document/terms_and_conditions.html");

        TextView tvOk = alertLayout.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void planTravel() {
        //TODO API Call
        isVerifyAlready("new");
    }

    private void updateMyTravel() {
        //TODO API Call
        isVerifyAlready("edit");
    }

    private void isVerifyAlready(final String tag) {
        RetrofitInstance.getClient().isVerifyKYC().enqueue(new Callback<AadhaarDetailsResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<AadhaarDetailsResponseModel> call, @NonNull Response<AadhaarDetailsResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getIs_verified()) {
                        if (tag.equals("new")) {
                            viewProgressDialog.showProgress(context);
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

                            RetrofitInstance.getClient().planTravel(jsonObject).enqueue(new Callback<CommonResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                                    viewProgressDialog.hideDialog();

                                    if (response.body() != null) {
                                        if (response.body().getStatus()) {
                                            Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                            commonRedirect(); //isVerifyAlready in if
                                        } else
                                            Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                                    viewProgressDialog.hideDialog();
                                }
                            });
                        } else {
                            viewProgressDialog.showProgress(context);
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("travel_id", travelId);
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

                            RetrofitInstance.getClient().updateMyTravel(jsonObject).enqueue(new Callback<CommonResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                                    viewProgressDialog.hideDialog();

                                    if (response.body() != null) {
                                        if (response.body().getStatus()) {
                                            Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                            commonRedirect(); //isVerifyAlready in else
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
                    } else {
                        Toast.makeText(context, "Please complete your KYC first!!!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AadhaarDetailsResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void commonRedirect() {
        FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                dashboardFragment, R.id.frame_container, false);
    }

    private void openDatePickerDialog() {
        DatePickerDialog mDate = new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        mDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        Uitility.showDatePickerWithConditionalDateForOrders(mDate,tag,getActivity(),strStartDate,startYear,
                startMonth,startDay);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            view.setMinDate(System.currentTimeMillis() - 1000);

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (tag.equals("start_date")) {
                startYear = year;
                startMonth = monthOfYear;
                startDay = dayOfMonth;
                strStartDate = Uitility.dateFormat(year, monthOfYear, dayOfMonth); //PlanTravelFragment
                etStartDate.setText(strStartDate);
            } else if (tag.equals("end_date")) {
                strEndDate = Uitility.dateFormat(year, monthOfYear, dayOfMonth); //PlanTravelFragment
                etEndDate.setText(strEndDate);
            }
        }

    };

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

                        String strMinute = "";
                        if(minute < 9){
                            strMinute = "0" + minute;
                        }else
                            strMinute = String.valueOf(minute);

                        if (tag.equals("start_time")) {
                            strStartTime = hourOfDay + ":" + strMinute;
                            etStartTime.setText(strStartTime);
                        } else if (tag.equals("end_time")) {
                            strEndTime = hourOfDay + ":" + strMinute;
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
    }

    private void getWeights() {
        RetrofitInstance.getClient().getWeights().enqueue(new Callback<WeightResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<WeightResponseModel> call, @NonNull Response<WeightResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        weightResponseModel = response.body();

                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        userSession.setWeight(json);

                        ArrayList<String> weightArrayList = new ArrayList<>();
                        weightArrayList.add(0, "Select Weight");
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            weightArrayList.add(response.body().getData().get(i).getWeight());
                            bindWeightDataToSpinner(weightArrayList);
                        }
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

    private void bindWeightDataToSpinner(ArrayList<String> data) {
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> aa = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
                data);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerPreferredWeight.setAdapter(aa);

        if (edit != null && !edit.equals("")) {
            if (edit.equals("edit")) {
                if (strWeight != null) {
                    int spinnerPosition = aa.getPosition(strWeight);
                    spinnerPreferredWeight.setSelection(spinnerPosition);
                }
            }
        }
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

    private void getDeliveryOption() {
        RetrofitInstance.getClient().getDeliveryOption().enqueue(new Callback<DeliveryOptionResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<DeliveryOptionResponseModel> call, @NonNull Response<DeliveryOptionResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        deliveryOptionResponseModel = response.body();

                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        userSession.setDeliveryOption(json);

                        ArrayList<String> deliveryOptionArrayList = new ArrayList<>();
                        deliveryOptionArrayList.add(0, "Select Delivery Option");
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            deliveryOptionArrayList.add(response.body().getData().get(i).getDeliveryOption());
                            bindDeliveryOptionDataToSpinner(deliveryOptionArrayList);
                        }
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<DeliveryOptionResponseModel> call, @NonNull Throwable t) {
            }
        });
    }

    private void bindDeliveryOptionDataToSpinner(ArrayList<String> data) {
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> aa = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
                data);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerDeliveryOption.setAdapter(aa);

        if (edit != null && !edit.equals("")) {
            if (edit.equals("edit")) {
                if (deliveryOption != null) {
                    int spinnerPosition = aa.getPosition(deliveryOption);
                    spinnerDeliveryOption.setSelection(spinnerPosition);
                }
            }
        }
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
    }

    private void getState() {
        RetrofitInstance.getClient().getState().enqueue(new Callback<StateResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<StateResponseModel> call, @NonNull Response<StateResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        stateResponseModel = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        userSession.setFromState(json);
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

        manufactureArrayList.add(0, "Select State");
        for (int i = 0; i < data.size(); i++) {
            manufactureArrayList.add(data.get(i).getStateName());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, manufactureArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFromState.setAdapter(adapter);
        spinnerToState.setAdapter(adapter);

        if (edit != null && !edit.equals("")) {
            if (edit.equals("edit")) {
                if (fromState != null) {
                    int spinnerPosition = adapter.getPosition(fromState);
                    spinnerFromState.setSelection(spinnerPosition);
                }
                if (toState != null) {
                    int spinnerPosition = adapter.getPosition(toState);
                    spinnerToState.setSelection(spinnerPosition);
                }
            }
        }

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

    private void getVehicleType() {
        RetrofitInstance.getClient().getVehicleType().enqueue(new Callback<VehicleResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<VehicleResponseModel> call, @NonNull Response<VehicleResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        vehicleResponseModel = response.body();

                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        userSession.setVehicleType(json);

                        ArrayList<String> vehicleTypeArrayList = new ArrayList<>();
                        vehicleTypeArrayList.add(0, "Select Vehicle Type");
                        for (int i = 0; i < vehicleResponseModel.getData().size(); i++) {
                            vehicleTypeArrayList.add(vehicleResponseModel.getData().get(i).getVehicleType());
                            bindVehicleTypeDataToSpinner(vehicleTypeArrayList);
                        }
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<VehicleResponseModel> call, @NonNull Throwable t) {
            }
        });
    }

    private void bindVehicleTypeDataToSpinner(ArrayList<String> data) {
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> aa = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
                data);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerVehicleType.setAdapter(aa);

        if (edit != null && !edit.equals("")) {
            if (edit.equals("edit")) {
                if (modeOfTravel != null) {
                    int spinnerPosition = aa.getPosition(modeOfTravel);
                    spinnerVehicleType.setSelection(spinnerPosition);
                }
            }
        }
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

    private void getCity(int strStateId, final String tag) {
        if (tag.equals("to"))
            viewProgressDialog.showProgress(context);

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

            manufactureArrayList.add(0, "Select City");
            for (int i = 0; i < data.size(); i++) {
                manufactureArrayList.add(data.get(i).getCityName());
            }
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, manufactureArrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerFromCity.setAdapter(adapter);
            if (edit != null && !edit.equals("")) {
                if (edit.equals("edit")) {
                    if (fromCity != null) {
                        int spinnerPosition = adapter.getPosition(fromCity);
                        spinnerFromCity.setSelection(spinnerPosition);
                    }
                }
            }
        }
        if (tag.equals("to")) {
            ArrayList<String> manufactureArrayList = new ArrayList<>();

            manufactureArrayList.add(0, "Select City");
            for (int i = 0; i < data.size(); i++) {
                manufactureArrayList.add(data.get(i).getCityName());
            }
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, manufactureArrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerToCity.setAdapter(adapter);
            if (edit != null && !edit.equals("")) {
                if (edit.equals("edit")) {
                    if (toCity != null) {
                        int spinnerPosition = adapter.getPosition(toCity);
                        spinnerToCity.setSelection(spinnerPosition);
                    }
                }
            }
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

