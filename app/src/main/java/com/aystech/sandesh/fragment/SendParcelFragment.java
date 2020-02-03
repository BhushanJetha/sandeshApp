package com.aystech.sandesh.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.aystech.sandesh.model.CityModel;
import com.aystech.sandesh.model.CityResponseModel;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.StateModel;
import com.aystech.sandesh.model.StateResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.UserSession;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendParcelFragment extends Fragment implements View.OnClickListener {

    Context context;

    StateResponseModel stateResponseModel;
    CityResponseModel cityResponseModel;

    private Spinner spinnerFromState, spinnerFromCity, spinnerToState, spinnerToCity;
    EditText etFromPincode, etToPincode, etGoodsDescription,
            etGoodsValue, etReceiverName, etReceiverMobileNo;
    ImageView ingStartDate, ingStartTime;
    EditText etStartDate, etStartTime;
    ImageView ingEndDate, ingEndTime;
    Group gpInvoice, gpParcel;
    ImageView imgInvoice, imgParcel;
    EditText etEndDate, etEndTime;
    Spinner spinnerDeliveryOption, spinnerNatureGoods, spinnerQuality, spinnerWeight, spinnerPackaging,
            spinnerProhibited;
    RadioGroup rgOwnership;
    Button btnSubmit;
    TextView btnCancel;

    String deliveryOption, natureOfGoods, quality, weight, packaging, prohibited, ownership;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int fromStateId, fromCityId, toStateId, toCityId;

    Uri picUri;
    Bitmap myBitmap;
    private String strInvoiceBase64, strParcelBase64;
    private String tag;

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
        gpInvoice.setOnClickListener(this);
        gpParcel.setOnClickListener(this);
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
                //sendParcel();
                break;
            case R.id.gpInvoice:
                gotoSelectPicture("invoice");
                break;
            case R.id.gpParcel:
                gotoSelectPicture("parcel");
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
        jsonObject.addProperty("from_city_id", fromCityId);
        jsonObject.addProperty("from_pincode", etFromPincode.getText().toString());
        jsonObject.addProperty("to_city_id", toCityId);
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
        jsonObject.addProperty("invoice_pic", strInvoiceBase64);
        jsonObject.addProperty("parcel_pic", strParcelBase64);
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

        //TODO API Call
        getState();
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

        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {
            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), picUri);
                    myBitmap = getResizedBitmap(myBitmap, 500);

                    if (tag.equals("invoice")) {
                        strInvoiceBase64 = getStringImage(myBitmap);
                        imgInvoice.setImageBitmap(myBitmap);
                    } else if (tag.equals("parcel")) {
                        strParcelBase64 = getStringImage(myBitmap);
                        imgParcel.setImageBitmap(myBitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = (Bitmap) data.getExtras().get("data");
                myBitmap = bitmap;

                if (tag.equals("invoice")) {
                    strInvoiceBase64 = getStringImage(myBitmap);
                    imgInvoice.setImageBitmap(myBitmap);
                } else if (tag.equals("parcel")) {
                    strParcelBase64 = getStringImage(myBitmap);
                    imgParcel.setImageBitmap(myBitmap);
                }
            }
        }
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

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
