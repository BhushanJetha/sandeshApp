package com.aystech.sandesh.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.aystech.sandesh.model.TravelDetailModel;
import com.aystech.sandesh.model.WeightResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.AppController;
import com.aystech.sandesh.utils.ImageSelectionMethods;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendParcelFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private TravelDetailModel travelDetailModel;
    private WeightResponseModel weightResponseModel;
    private StateResponseModel stateResponseModel;
    private CityResponseModel cityResponseModel;

    private Spinner spinnerFromState, spinnerFromCity, spinnerToState, spinnerToCity;
    private EditText etFromPincode, etToPincode, etGoodsDescription,
            etGoodsValue, etReceiverName, etReceiverMobileNo, etReceiverAddress,
            etStartDate, etStartTime, etEndDate, etEndTime;
    private ImageView ingStartDate, ingStartTime;
    private ImageView ingEndDate, ingEndTime;
    private Group gpInvoice, gpParcel;
    private ImageView imgInvoice, imgParcel;
    private Spinner spinnerDeliveryOption, spinnerNatureGoods, spinnerQuality, spinnerWeight, spinnerPackaging;
    private RadioGroup rgOwnership, rgHazardous, rgProhibited, rgFraglle, rgFlamableToxicExplosive;
    private RadioButton rbCommercial, rbNonCommercial, rbHazardousYes, rbHazardousNo, rbProhibitedYes,
            rbProhibitedNo, rbFraglleYes, rbFraglleNo, rbFlamableToxicExplosiveYes, rbFlamableToxicExplosiveNo;
    private Button btnSubmit;
    private TextView btnCancel;
    private CheckBox cbTermsCondition;

    private ArrayAdapter<String> adapterDeliveryOption;
    private ArrayAdapter<String> adapterNatureOfGoods;
    private ArrayAdapter<String> adapterQuality;
    private ArrayAdapter<String> adapterTypeOfPkg;

    private String deliveryOption, natureOfGoods, strQuality, strPackaging, strOwnership, strFromPincode, strtoPincode,
            strStartDate, strStartTime, strEndDate, strEndTime, strGoodsDescription, strValueOgGood,
            strReceiverName, strReceiverMobileNo, strReceiverAddress, rgStrHazardous, rgStrProhibited,
            rgStrFraglle, rgStrFlamableToxicExplosive, strWeight;

    private String tag, edit;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int fromStateId, fromCityId, toStateId, toCityId, weightId;
    private boolean onceClicked = false;

    private Uri picUri;
    private Bitmap myBitmap;
    private String strInvoiceFilePath, strParcelFilePath;

    private ViewProgressDialog viewProgressDialog;

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

        if (getArguments() != null) {
            travelDetailModel = getArguments().getParcelable("order_detail");
            edit = getArguments().getString("tag");
        }

        initView(view);

        onClickListener();

        if (edit != null && !edit.equals("")) {
            if (edit.equals("edit"))
                editSendParcel();
        }

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        gpInvoice = view.findViewById(R.id.gpInvoice);
        gpParcel = view.findViewById(R.id.gpParcel);
        imgInvoice = view.findViewById(R.id.imgInvoice);
        imgInvoice.setImageResource(R.drawable.ic_invoice);
        imgParcel = view.findViewById(R.id.imgParcel);
        imgParcel.setImageResource(R.drawable.ic_parcel);
        spinnerFromState = view.findViewById(R.id.spinnerFromState);
        spinnerFromCity = view.findViewById(R.id.spinnerFromCity);
        etFromPincode = view.findViewById(R.id.etFromPincode);
        spinnerToState = view.findViewById(R.id.spinnerToState);
        spinnerToCity = view.findViewById(R.id.spinnerToCity);
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
        etGoodsDescription = view.findViewById(R.id.etGoodsDescription);
        etGoodsValue = view.findViewById(R.id.etGoodsValue);
        rgOwnership = view.findViewById(R.id.rgOwnership);
        rgHazardous = view.findViewById(R.id.rgHazardous);
        rgProhibited = view.findViewById(R.id.rgProhibited);
        rgFraglle = view.findViewById(R.id.rgFraglle);
        rgFlamableToxicExplosive = view.findViewById(R.id.rgFlamableToxicExplosive);
        etReceiverName = view.findViewById(R.id.etReceiverName);
        etReceiverMobileNo = view.findViewById(R.id.etReceiverMobileNo);
        etReceiverAddress = view.findViewById(R.id.etReceiverAddress);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnCancel = view.findViewById(R.id.btnCancel);
        cbTermsCondition = view.findViewById(R.id.cbTermsCondition);
        rbCommercial = view.findViewById(R.id.rbCommercial);
        rbNonCommercial = view.findViewById(R.id.rbNonCommercial);
        rbHazardousYes = view.findViewById(R.id.rbHazardousYes);
        rbHazardousNo = view.findViewById(R.id.rbHazardousNo);
        rbProhibitedYes = view.findViewById(R.id.rbProhibitedYes);
        rbProhibitedNo = view.findViewById(R.id.rbProhibitedNo);
        rbFraglleYes = view.findViewById(R.id.rbFraglleYes);
        rbFraglleNo = view.findViewById(R.id.rbFraglleNo);
        rbFlamableToxicExplosiveYes = view.findViewById(R.id.rbFlamableToxicExplosiveYes);
        rbFlamableToxicExplosiveNo = view.findViewById(R.id.rbFlamableToxicExplosiveNo);

        String[] delivery_option_array = getResources().getStringArray(R.array.delivery_option_array);
        adapterDeliveryOption =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, delivery_option_array);
        adapterDeliveryOption.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDeliveryOption.setAdapter(adapterDeliveryOption);

        String[] nature_of_good = getResources().getStringArray(R.array.nature_of_good);
        adapterNatureOfGoods =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, nature_of_good);
        adapterNatureOfGoods.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerNatureGoods.setAdapter(adapterNatureOfGoods);

        String[] quality = getResources().getStringArray(R.array.nature_of_good);
        adapterQuality =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, quality);
        adapterNatureOfGoods.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerQuality.setAdapter(adapterQuality);

        String[] type_of_pkg = getResources().getStringArray(R.array.type_of_pkg);
        adapterTypeOfPkg =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, type_of_pkg);
        adapterNatureOfGoods.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerPackaging.setAdapter(adapterTypeOfPkg);
    }

    private void editSendParcel() {
        etToPincode.setText(travelDetailModel.getParcelData().getToPincode());
        etFromPincode.setText(travelDetailModel.getParcelData().getFromPincode());
        etStartDate.setText(travelDetailModel.getParcelData().getStartDate());
        etStartTime.setText(travelDetailModel.getParcelData().getStartTime());
        etEndDate.setText(travelDetailModel.getParcelData().getEndDate());
        etEndTime.setText(travelDetailModel.getParcelData().getEndTime());
        etGoodsDescription.setText(travelDetailModel.getParcelData().getGoodDescription());
        etGoodsValue.setText(travelDetailModel.getParcelData().getValueOfGoods());
        etReceiverName.setText(travelDetailModel.getParcelData().getReceiverName());
        etReceiverMobileNo.setText(travelDetailModel.getParcelData().getReceiverMobileNo());
        etReceiverAddress.setText(travelDetailModel.getParcelData().getReceiverAddressDetail());

        Glide.with(context)
                .load(AppController.imageURL + travelDetailModel.getParcelData().getInvoicePic())
                .error(R.drawable.ic_logo_sandesh)
                .into(imgInvoice);

        Glide.with(context)
                .load(AppController.imageURL + travelDetailModel.getParcelData().getParcelPic())
                .error(R.drawable.ic_logo_sandesh)
                .into(imgParcel);

        if (rbCommercial.getText().toString().equals(travelDetailModel.getParcelData().getOwnership())) {
            rbCommercial.setChecked(true);
            rbNonCommercial.setChecked(false);
        } else if (rbNonCommercial.getText().toString().equals(travelDetailModel.getParcelData().getOwnership())) {
            rbCommercial.setChecked(false);
            rbNonCommercial.setChecked(true);
        }
        if (rbHazardousYes.getText().toString().equals(travelDetailModel.getParcelData().getIsHazardous())) {
            rbHazardousYes.setChecked(true);
            rbHazardousNo.setChecked(false);
        } else if (rbHazardousNo.getText().toString().equals(travelDetailModel.getParcelData().getIsHazardous())) {
            rbHazardousYes.setChecked(false);
            rbHazardousNo.setChecked(true);
        }
        if (rbProhibitedYes.getText().toString().equals(travelDetailModel.getParcelData().getIsProhibited())) {
            rbProhibitedYes.setChecked(true);
            rbProhibitedNo.setChecked(false);
        } else if (rbProhibitedNo.getText().toString().equals(travelDetailModel.getParcelData().getIsProhibited())) {
            rbProhibitedYes.setChecked(false);
            rbProhibitedNo.setChecked(true);
        }
        if (rbFraglleYes.getText().toString().equals(travelDetailModel.getParcelData().getIsFragile())) {
            rbFraglleYes.setChecked(true);
            rbFraglleNo.setChecked(false);
        } else if (rbFraglleNo.getText().toString().equals(travelDetailModel.getParcelData().getIsFragile())) {
            rbFraglleYes.setChecked(false);
            rbFraglleNo.setChecked(true);
        }
        if (rbFlamableToxicExplosiveYes.getText().toString().equals(travelDetailModel.getParcelData().getIsFlamable())) {
            rbFlamableToxicExplosiveYes.setChecked(true);
            rbFlamableToxicExplosiveNo.setChecked(false);
        } else if (rbFlamableToxicExplosiveNo.getText().toString().equals(travelDetailModel.getParcelData().getIsFlamable())) {
            rbFlamableToxicExplosiveYes.setChecked(false);
            rbFlamableToxicExplosiveNo.setChecked(true);
        }
        deliveryOption = travelDetailModel.getParcelData().getDeliveryOption();
        natureOfGoods = travelDetailModel.getParcelData().getNatureOfGoods();
        strQuality = travelDetailModel.getParcelData().getQuality();
        strPackaging = travelDetailModel.getParcelData().getPackaging();
        strWeight = travelDetailModel.getParcelData().getWeight();
        if (deliveryOption != null) {
            int spinnerPosition = adapterDeliveryOption.getPosition(deliveryOption);
            spinnerDeliveryOption.setSelection(spinnerPosition);
        }
        if (natureOfGoods != null) {
            int spinnerPosition = adapterNatureOfGoods.getPosition(natureOfGoods);
            spinnerNatureGoods.setSelection(spinnerPosition);
        }
        if (strQuality != null) {
            int spinnerPosition = adapterQuality.getPosition(strQuality);
            spinnerQuality.setSelection(spinnerPosition);
        }
        if (strPackaging != null) {
            int spinnerPosition = adapterTypeOfPkg.getPosition(strPackaging);
            spinnerPackaging.setSelection(spinnerPosition);
        }
        onceClicked = true;
        cbTermsCondition.setChecked(true);

        btnSubmit.setText("Update");
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strtoPincode = etToPincode.getText().toString();
                strFromPincode = etFromPincode.getText().toString();
                strStartDate = etStartDate.getText().toString();
                strStartTime = etStartTime.getText().toString();
                strEndDate = etEndDate.getText().toString();
                strEndTime = etEndTime.getText().toString();
                strGoodsDescription = etGoodsDescription.getText().toString();
                strValueOgGood = etGoodsValue.getText().toString();
                strReceiverName = etReceiverName.getText().toString();
                strReceiverMobileNo = etReceiverMobileNo.getText().toString();
                strReceiverAddress = etReceiverAddress.getText().toString();

                //TODO API Call
                updateMyParcel(travelDetailModel.getParcelData().getParcelId());
            }
        });
    }

    private void onClickListener() {
        gpInvoice.setOnClickListener(this);
        gpParcel.setOnClickListener(this);
        ingStartDate.setOnClickListener(this);
        ingStartTime.setOnClickListener(this);
        ingEndDate.setOnClickListener(this);
        ingEndTime.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        cbTermsCondition.setOnClickListener(this);

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
                    strQuality = selectedItem;
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPackaging.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    strPackaging = selectedItem;
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
                    strOwnership = checkedRadioButton.getText().toString();
                }
            }
        });

        // This overrides the radiogroup onCheckListener
        rgHazardous.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {
                    rgStrHazardous = checkedRadioButton.getText().toString();
                }
            }
        });

        // This overrides the radiogroup onCheckListener
        rgProhibited.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {
                    rgStrProhibited = checkedRadioButton.getText().toString();
                }
            }
        });

        // This overrides the radiogroup onCheckListener
        rgFraglle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {
                    rgStrFraglle = checkedRadioButton.getText().toString();
                }
            }
        });

        // This overrides the radiogroup onCheckListener
        rgFlamableToxicExplosive.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {
                    rgStrFlamableToxicExplosive = checkedRadioButton.getText().toString();
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
                strtoPincode = etToPincode.getText().toString();
                strFromPincode = etFromPincode.getText().toString();
                strGoodsDescription = etGoodsDescription.getText().toString();
                strValueOgGood = etGoodsValue.getText().toString();
                strReceiverName = etReceiverName.getText().toString();
                strReceiverMobileNo = etReceiverMobileNo.getText().toString();
                strReceiverAddress = etReceiverAddress.getText().toString();

                //TODO API Call
                sendParcel();
                break;
            case R.id.gpInvoice:
                gotoSelectPicture("invoice");
                break;
            case R.id.gpParcel:
                gotoSelectPicture("parcel");
                break;
            case R.id.btnCancel:
                ((MainActivity) context).getSupportFragmentManager().popBackStack();
                break;
            case R.id.cbTermsCondition:
                if (!onceClicked) {
                    cbTermsCondition.setClickable(false);
                    showTermsConditions();
                }
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
        wvTermsConditions.loadUrl("file:///android_res/raw/" + "terms_and_condition.html");

        TextView tvOk = alertLayout.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onceClicked = true;
                dialog.dismiss();
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

    private void sendParcel() {
        ViewProgressDialog.getInstance().showProgress(context);

        RequestBody from_city_id = RequestBody.create(MultipartBody.FORM, String.valueOf(fromCityId));
        RequestBody from_pincode = RequestBody.create(MultipartBody.FORM, strFromPincode);
        RequestBody to_city_id = RequestBody.create(MultipartBody.FORM, String.valueOf(toCityId));
        RequestBody to_pincode = RequestBody.create(MultipartBody.FORM, strtoPincode);
        RequestBody start_date = RequestBody.create(MultipartBody.FORM, strStartDate);
        RequestBody start_time = RequestBody.create(MultipartBody.FORM, strStartTime);
        RequestBody end_date = RequestBody.create(MultipartBody.FORM, strEndDate);
        RequestBody end_time = RequestBody.create(MultipartBody.FORM, strEndTime);
        RequestBody delivery_option = RequestBody.create(MultipartBody.FORM, deliveryOption);
        RequestBody nature_of_goods = RequestBody.create(MultipartBody.FORM, natureOfGoods);
        RequestBody good_description = RequestBody.create(MultipartBody.FORM, strGoodsDescription);
        RequestBody quality = RequestBody.create(MultipartBody.FORM, strQuality);
        RequestBody weight_id = RequestBody.create(MultipartBody.FORM, String.valueOf(weightId));
        RequestBody packaging = RequestBody.create(MultipartBody.FORM, strPackaging);
        RequestBody isHazardous = RequestBody.create(MultipartBody.FORM, rgStrHazardous);
        RequestBody isProhibited = RequestBody.create(MultipartBody.FORM, rgStrProhibited);
        RequestBody isFragile = RequestBody.create(MultipartBody.FORM, rgStrFraglle);
        RequestBody isFlamable = RequestBody.create(MultipartBody.FORM, rgStrFlamableToxicExplosive);
        RequestBody value_of_goods = RequestBody.create(MultipartBody.FORM, strValueOgGood);
        RequestBody ownership = RequestBody.create(MultipartBody.FORM, strOwnership);

        MultipartBody.Part parcel_pic_body;
        if (strParcelFilePath != null && !strParcelFilePath.equals("")) {
            File file = new File(strParcelFilePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            parcel_pic_body = MultipartBody.Part.createFormData("parcel_pic", file.getName(), requestBody);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            parcel_pic_body = MultipartBody.Part.createFormData("parcel_pic", "", requestBody);
        }

        MultipartBody.Part invoice_pic_body;
        if (strInvoiceFilePath != null && !strInvoiceFilePath.equals("")) {
            File file = new File(strInvoiceFilePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            invoice_pic_body = MultipartBody.Part.createFormData("invoice_pic", file.getName(), requestBody);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            invoice_pic_body = MultipartBody.Part.createFormData("invoice_pic", "", requestBody);
        }

        RequestBody receiver_name = RequestBody.create(MultipartBody.FORM, strReceiverName);
        RequestBody receiver_mobile_no = RequestBody.create(MultipartBody.FORM, strReceiverMobileNo);
        RequestBody receiver_address_detail = RequestBody.create(MultipartBody.FORM, strReceiverAddress);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.sendParcel(
                from_city_id,
                from_pincode,
                to_city_id,
                to_pincode,
                start_date,
                start_time,
                end_date,
                end_time,
                delivery_option,
                nature_of_goods,
                good_description,
                quality,
                weight_id,
                packaging,
                isHazardous,
                isProhibited,
                isFragile,
                isFlamable,
                value_of_goods,
                ownership,
                parcel_pic_body,
                invoice_pic_body,
                receiver_name,
                receiver_mobile_no,
                receiver_address_detail
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

    private void updateMyParcel(Integer parcelId) {
        ViewProgressDialog.getInstance().showProgress(context);

        RequestBody parcel_id = RequestBody.create(MultipartBody.FORM, String.valueOf(parcelId));
        RequestBody from_city_id = RequestBody.create(MultipartBody.FORM, String.valueOf(fromCityId));
        RequestBody from_pincode = RequestBody.create(MultipartBody.FORM, strFromPincode);
        RequestBody to_city_id = RequestBody.create(MultipartBody.FORM, String.valueOf(toCityId));
        RequestBody to_pincode = RequestBody.create(MultipartBody.FORM, strtoPincode);
        RequestBody start_date = RequestBody.create(MultipartBody.FORM, strStartDate);
        RequestBody start_time = RequestBody.create(MultipartBody.FORM, strStartTime);
        RequestBody end_date = RequestBody.create(MultipartBody.FORM, strEndDate);
        RequestBody end_time = RequestBody.create(MultipartBody.FORM, strEndTime);
        RequestBody delivery_option = RequestBody.create(MultipartBody.FORM, deliveryOption);
        RequestBody nature_of_goods = RequestBody.create(MultipartBody.FORM, natureOfGoods);
        RequestBody good_description = RequestBody.create(MultipartBody.FORM, strGoodsDescription);
        RequestBody quality = RequestBody.create(MultipartBody.FORM, strQuality);
        RequestBody weight_id = RequestBody.create(MultipartBody.FORM, String.valueOf(weightId));
        RequestBody packaging = RequestBody.create(MultipartBody.FORM, strPackaging);
        RequestBody isHazardous = RequestBody.create(MultipartBody.FORM, rgStrHazardous);
        RequestBody isProhibited = RequestBody.create(MultipartBody.FORM, rgStrProhibited);
        RequestBody isFragile = RequestBody.create(MultipartBody.FORM, rgStrFraglle);
        RequestBody isFlamable = RequestBody.create(MultipartBody.FORM, rgStrFlamableToxicExplosive);
        RequestBody value_of_goods = RequestBody.create(MultipartBody.FORM, strValueOgGood);
        RequestBody ownership = RequestBody.create(MultipartBody.FORM, strOwnership);

        MultipartBody.Part parcel_pic_body;
        if (strParcelFilePath != null && !strParcelFilePath.equals("")) {
            File file = new File(strParcelFilePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            parcel_pic_body = MultipartBody.Part.createFormData("parcel_pic", file.getName(), requestBody);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            parcel_pic_body = MultipartBody.Part.createFormData("parcel_pic", "", requestBody);
        }

        MultipartBody.Part invoice_pic_body;
        if (strInvoiceFilePath != null && !strInvoiceFilePath.equals("")) {
            File file = new File(strInvoiceFilePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            invoice_pic_body = MultipartBody.Part.createFormData("invoice_pic", file.getName(), requestBody);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            invoice_pic_body = MultipartBody.Part.createFormData("invoice_pic", "", requestBody);
        }

        RequestBody receiver_name = RequestBody.create(MultipartBody.FORM, strReceiverName);
        RequestBody receiver_mobile_no = RequestBody.create(MultipartBody.FORM, strReceiverMobileNo);
        RequestBody receiver_address_detail = RequestBody.create(MultipartBody.FORM, strReceiverAddress);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.updateMyParcel(
                parcel_id,
                from_city_id,
                from_pincode,
                to_city_id,
                to_pincode,
                start_date,
                start_time,
                end_date,
                end_time,
                delivery_option,
                nature_of_goods,
                good_description,
                quality,
                weight_id,
                packaging,
                isHazardous,
                isProhibited,
                isFragile,
                isFlamable,
                value_of_goods,
                ownership,
                parcel_pic_body,
                invoice_pic_body,
                receiver_name,
                receiver_mobile_no,
                receiver_address_detail
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

                        ArrayList<String> weightArrayList = new ArrayList<>();
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            weightArrayList.add(response.body().getData().get(i).getWeight());
                            bindStateDataToSpinner(weightArrayList);
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

    private void bindStateDataToSpinner(ArrayList<String> data) {
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> aa = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
                data);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerWeight.setAdapter(aa);

        if (edit != null && !edit.equals("")) {
            if (edit.equals("edit")) {
                if (strWeight != null) {
                    int spinnerPosition = aa.getPosition(strWeight);
                    spinnerWeight.setSelection(spinnerPosition);
                }
            }
        }
        spinnerWeight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    weightId = weightResponseModel.getWeightId(selectedItem);
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

    private void gotoSelectPicture(String type) {
        tag = type;
        startActivityForResult(ImageSelectionMethods.getPickImageChooserIntent(context), 200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (ImageSelectionMethods.getPickImageResultUri(context, data) != null) {
                picUri = ImageSelectionMethods.getPickImageResultUri(context, data);
                if (tag.equals("invoice")) {
                    strInvoiceFilePath = ImageSelectionMethods.getPath(context, picUri);

                    if (strInvoiceFilePath.equals("Not found")) {
                        strInvoiceFilePath = picUri.getPath();
                    }
                } else if (tag.equals("parcel")) {
                    strParcelFilePath = ImageSelectionMethods.getPath(context, picUri);

                    if (strParcelFilePath.equals("Not found")) {
                        strParcelFilePath = picUri.getPath();
                    }
                }

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), picUri);
                    myBitmap = ImageSelectionMethods.getResizedBitmap(myBitmap, 500);

                    if (tag.equals("invoice")) {
                        imgInvoice.setImageBitmap(myBitmap);
                    } else if (tag.equals("parcel")) {
                        imgParcel.setImageBitmap(myBitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
