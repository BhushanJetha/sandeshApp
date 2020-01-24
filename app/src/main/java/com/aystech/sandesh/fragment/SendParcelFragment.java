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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class SendParcelFragment extends Fragment implements View.OnClickListener {

    Context context;

    EditText etFrom, etFromPincode, etTo, etToPincode, etGoodsDescription,
            etGoodsValue, etReceiverName, etReceiverMobileNo;
    ImageView ingStartDate, ingStartTime;
    EditText etStartDate, etStartTime;
    ImageView ingEndDate, ingEndTime;
    EditText etEndDate, etEndTime;
    Spinner spinnerDeliveryOption, spinnerNatureGoods, spinnerQuality, spinnerWeight, spinnerPackaging,
            spinnerProhibited;
    RadioGroup rgOwnership;
    Button btnSubmit;
    TextView btnCancel;

    String deliveryOption, natureOfGoods, quality, weight, packaging, prohibited, ownership;
    private int mYear, mMonth, mDay, mHour, mMinute;

    UserSession userSession;
    ViewProgressDialog viewProgressDialog;

    public SendParcelFragment() {
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
        View view = inflater.inflate(R.layout.fragment_send_parcel, container, false);

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
        ingStartDate = view.findViewById(R.id.ingStartDate);
        ingStartTime = view.findViewById(R.id.ingStartTime);
        etStartDate = view.findViewById(R.id.etStartDate);
        etStartTime = view.findViewById(R.id.etStartTime);
        ingEndDate = view.findViewById(R.id.ingEndDate);
        ingEndTime = view.findViewById(R.id.ingEndTime);
        etEndDate = view.findViewById(R.id.etEndDate);
        etEndTime = view.findViewById(R.id.etEndTime);
        spinnerDeliveryOption = view.findViewById(R.id.spinnerDeliveryOption);
        spinnerNatureGoods = view.findViewById(R.id.spinnerNatureGoods);
        spinnerQuality = view.findViewById(R.id.spinnerQuality);
        spinnerWeight = view.findViewById(R.id.spinnerWeight);
        spinnerPackaging = view.findViewById(R.id.spinnerPackaging);
        spinnerProhibited = view.findViewById(R.id.spinnerProhibited);
        etGoodsDescription = view.findViewById(R.id.etGoodsDescription);
        etGoodsValue = view.findViewById(R.id.etGoodsValue);
        rgOwnership = view.findViewById(R.id.rgOwnership);
        etReceiverName = view.findViewById(R.id.etReceiverName);
        etReceiverMobileNo = view.findViewById(R.id.etReceiverMobileNo);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnCancel = view.findViewById(R.id.btnCancel);
    }

    private void onClickListener() {
        ingStartDate.setOnClickListener(this);
        ingStartTime.setOnClickListener(this);
        ingEndDate.setOnClickListener(this);
        ingEndTime.setOnClickListener(this);
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

        spinnerNatureGoods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    natureOfGoods = selectedItem;
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerQuality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    quality = selectedItem;
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    weight = selectedItem;
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPackaging.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    packaging = selectedItem;
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerProhibited.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    prohibited = selectedItem;
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // This overrides the radiogroup onCheckListener
        rgOwnership.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {
                    ownership = checkedRadioButton.getText().toString();
                }
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
                sendParcel();
                break;
        }
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

    private void sendParcel() {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("user_id", userSession.getUSER_ID());
        jsonObject.addProperty("user_type", userSession.getUSER_TYPE());
        jsonObject.addProperty("from_city_id", "");
        jsonObject.addProperty("from_pincode", etFromPincode.getText().toString());
        jsonObject.addProperty("to_city_id", "");
        jsonObject.addProperty("to_pincode", etToPincode.getText().toString());
        jsonObject.addProperty("start_date", etStartDate.getText().toString());
        jsonObject.addProperty("start_time", etStartTime.getText().toString());
        jsonObject.addProperty("end_date", etEndDate.getText().toString());
        jsonObject.addProperty("end_time", etEndTime.getText().toString());
        jsonObject.addProperty("delivery_option", deliveryOption);
        jsonObject.addProperty("nature_of_goods", natureOfGoods);
        jsonObject.addProperty("good_description", etGoodsDescription.getText().toString());
        jsonObject.addProperty("quality", quality);
        jsonObject.addProperty("weight", weight);
        jsonObject.addProperty("packaging", packaging);
        jsonObject.addProperty("isProhibited", prohibited);
        jsonObject.addProperty("value_of_goods", etGoodsValue.getText().toString());
        jsonObject.addProperty("ownership", ownership);
        jsonObject.addProperty("invoice_pic", "");
        jsonObject.addProperty("parcel_pic", "");
        jsonObject.addProperty("receiver_name", etReceiverName.getText().toString());
        jsonObject.addProperty("receiver_mobile_no", etReceiverMobileNo.getText().toString());

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.sendParcel(
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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
