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
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.aystech.sandesh.model.DeliveryOptionResponseModel;
import com.aystech.sandesh.model.NatureOfGoodsResponseModel;
import com.aystech.sandesh.model.PackagingResponseModel;
import com.aystech.sandesh.model.QualityResponseModel;
import com.aystech.sandesh.model.StateModel;
import com.aystech.sandesh.model.StateResponseModel;
import com.aystech.sandesh.model.TravelDetailModel;
import com.aystech.sandesh.model.WeightResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.AppController;
import com.aystech.sandesh.utils.Connectivity;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.ImageSelectionMethods;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.UserSession;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
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

    private static final String TAG = "SendParcelFragment";
    private Context context;

    private DeliveryOptionResponseModel deliveryOptionResponseModel;
    private TravelDetailModel travelDetailModel;
    private WeightResponseModel weightResponseModel;
    private StateResponseModel stateResponseModel;
    private CityResponseModel cityResponseModel;
    private NatureOfGoodsResponseModel natureOfGoodsResponseModel;
    private PackagingResponseModel packagingResponseModel;
    private QualityResponseModel qualityResponseModel;

    private DashboardFragment dashboardFragment;

    private Spinner spinnerFromState, spinnerFromCity, spinnerToState, spinnerToCity;
    private EditText etFromPincode, etToPincode, etGoodsDescription,
            etGoodsValue, etReceiverName, etReceiverMobileNo, etReceiverAddress,
            etStartDate, etStartTime, etEndDate, etEndTime;
    private ImageView ingStartDate, ingStartTime;
    private ImageView ingEndDate, ingEndTime;
    private ConstraintLayout gpInvoice, gpParcel;
    private ImageView imgInvoice, imgInvoiceCamera, imgParcel, imgParcelCamera;
    private TextView tvParcel, tvInvoice;
    private Spinner spinnerDeliveryOption, spinnerNatureGoods, spinnerQuality, spinnerWeight, spinnerPackaging;
    private RadioGroup rgOwnership, rgHazardous, rgProhibited, rgFraglle, rgFlamableToxicExplosive;
    private RadioButton rbCommercial, rbNonCommercial, rbHazardousYes, rbHazardousNo, rbProhibitedYes,
            rbProhibitedNo, rbFraglleYes, rbFraglleNo, rbFlamableToxicExplosiveYes, rbFlamableToxicExplosiveNo;
    private Button btnSubmit;
    private TextView btnCancel;
    private CheckBox cbTermsCondition, cbPricingPolicy;

    private String deliveryOption = "", natureOfGoods = "", strQuality = "", strPackaging = "", strOwnership = "", strFromPincode, strtoPincode,
            strStartDate = "", strStartTime = "", strEndDate = "", strEndTime = "", strGoodsDescription, strValueOgGood,
            strReceiverName, strReceiverMobileNo, strReceiverAddress, rgStrHazardous = "", rgStrProhibited = "",
            rgStrFraglle = "", rgStrFlamableToxicExplosive = "", strWeight = "";

    private String tag, edit, fromState, fromCity, toState, toCity;
    private int mHour, mMinute;
    private int fromStateId, fromCityId, toStateId, toCityId, weightId;

    private Uri picUri;
    private Bitmap myBitmap;
    private String strInvoiceFilePath, strParcelFilePath = "";

    final Calendar myCalendar = Calendar.getInstance();

    private ViewProgressDialog viewProgressDialog;
    private UserSession userSession;

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

        dashboardFragment = (DashboardFragment) Fragment.instantiate(context,
                DashboardFragment.class.getName());

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

        String natureOfGoods = userSession.getNatureOfGoods();
        if (natureOfGoods.length() > 0) {
            Gson gson = new Gson();
            natureOfGoodsResponseModel = gson.fromJson(natureOfGoods, NatureOfGoodsResponseModel.class);
            ArrayList<String> natureOfGoodsArrayList = new ArrayList<>();
            natureOfGoodsArrayList.add(0, "Select Nature Of Goods");
            for (int i = 0; i < natureOfGoodsResponseModel.getData().size(); i++) {
                natureOfGoodsArrayList.add(natureOfGoodsResponseModel.getData().get(i).getNatureOfGoods());
                bindNatureOfGoodsDataToSpinner(natureOfGoodsArrayList);
            }
        } else {
            //TODO API Call
            getNatureOfGoods();
        }

        String quality = userSession.getQuality();
        if (quality.length() > 0) {
            Gson gson = new Gson();
            qualityResponseModel = gson.fromJson(quality, QualityResponseModel.class);
            ArrayList<String> qualityArrayList = new ArrayList<>();
            qualityArrayList.add(0, "Select Quality");
            for (int i = 0; i < qualityResponseModel.getData().size(); i++) {
                qualityArrayList.add(qualityResponseModel.getData().get(i).getQuality());
                bindQualityDataToSpinner(qualityArrayList);
            }
        } else {
            //TODO API Call
            getQuality();
        }

        String packaging = userSession.getPackaging();
        if (packaging.length() > 0) {
            Gson gson = new Gson();
            packagingResponseModel = gson.fromJson(packaging, PackagingResponseModel.class);
            ArrayList<String> packagingArrayList = new ArrayList<>();
            packagingArrayList.add(0, "Select Packaging");
            for (int i = 0; i < packagingResponseModel.getData().size(); i++) {
                packagingArrayList.add(packagingResponseModel.getData().get(i).getPackaging());
                bindPackagingDataToSpinner(packagingArrayList);
            }
        } else {
            //TODO API Call
            getPackaging();
        }

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();
        userSession = new UserSession(context);

        gpInvoice = view.findViewById(R.id.gpInvoice);
        gpParcel = view.findViewById(R.id.gpParcel);
        imgInvoice = view.findViewById(R.id.imgInvoice);
        imgInvoice.setImageResource(R.drawable.ic_invoice);
        imgParcel = view.findViewById(R.id.imgParcel);
        imgParcel.setImageResource(R.drawable.ic_parcel);
        imgInvoiceCamera = view.findViewById(R.id.imgInvoiceCamera);
        imgParcelCamera = view.findViewById(R.id.imgParcelCamera);
        tvParcel = view.findViewById(R.id.tvParcel);
        tvInvoice = view.findViewById(R.id.tvInvoice);
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
        cbPricingPolicy = view.findViewById(R.id.cbPricingPolicy);
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
    }

    private void editSendParcel() {
        fromState = travelDetailModel.getParcelData().getFrom_state();
        fromCity = travelDetailModel.getParcelData().getFromCity();
        toState = travelDetailModel.getParcelData().getTo_state();
        toCity = travelDetailModel.getParcelData().getToCity();
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

        cbPricingPolicy.setChecked(true);
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


                if (fromCityId != 0) {
                    if (!strFromPincode.isEmpty()) {
                        if (strFromPincode.length() == 6) {
                            if (toCityId != 0) {
                                if (!strtoPincode.isEmpty()) {
                                    if (strtoPincode.length() == 6) {
                                        if (!strStartDate.isEmpty()) {
                                            if (!strEndDate.isEmpty()) {
                                                if (!strStartTime.isEmpty()) {
                                                    if (!strEndTime.isEmpty()) {
                                                        if (!deliveryOption.equals("Select Delivery Option")) {
                                                            if (!natureOfGoods.equals("Select Nature Of Goods")) {
                                                                if (!strGoodsDescription.isEmpty()) {
                                                                    if (!strQuality.equals("Select Quality")) {
                                                                        if (weightId != 0) {
                                                                            if (!strPackaging.equals("Select Packaging")) {
                                                                                if (!strValueOgGood.isEmpty()) {
                                                                                    if (!strOwnership.isEmpty()) {
                                                                                        if (!rgStrHazardous.isEmpty()) {
                                                                                            if (!rgStrProhibited.isEmpty()) {
                                                                                                if (!rgStrFraglle.isEmpty()) {
                                                                                                    if (!rgStrFlamableToxicExplosive.isEmpty()) {
                                                                                                        if (rgStrFlamableToxicExplosive.equals("No")) {
                                                                                                            if (rgStrFraglle.equals("No")) {
                                                                                                                if (rgStrProhibited.equals("No")) {
                                                                                                                    if (rgStrHazardous.equals("No")) {
                                                                                                                        if (!strReceiverName.isEmpty()) {
                                                                                                                            if (!strReceiverMobileNo.isEmpty()) {
                                                                                                                                if (!strReceiverAddress.isEmpty()) {
                                                                                                                                    if (cbPricingPolicy.isChecked()) {
                                                                                                                                        if (cbTermsCondition.isChecked()) {
                                                                                                                                            if (Integer.parseInt(strValueOgGood) <= 50000) {
                                                                                                                                                if (strOwnership.equals("Commercial")) {
                                                                                                                                                    if (Connectivity.isConnected(context)) {
                                                                                                                                                        //TODO API Call
                                                                                                                                                        updateMyParcel(travelDetailModel.getParcelData().getParcelId());
                                                                                                                                                    }
                                                                                                                                                } else {
                                                                                                                                                    if (Connectivity.isConnected(context)) {
                                                                                                                                                        //TODO API Call
                                                                                                                                                        updateMyParcel(travelDetailModel.getParcelData().getParcelId());
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            } else {
                                                                                                                                                Uitility.showToast(context, "We deliver order below amount 50,000 !");
                                                                                                                                            }
                                                                                                                                        } else {
                                                                                                                                            Uitility.showToast(context, "Please accept terms and condition!");
                                                                                                                                        }
                                                                                                                                    } else {
                                                                                                                                        Uitility.showToast(context, "Please accept pricing policy!");
                                                                                                                                    }
                                                                                                                                } else {
                                                                                                                                    Uitility.showToast(context, "Please enter receiver address!");
                                                                                                                                }
                                                                                                                            } else {
                                                                                                                                Uitility.showToast(context, "Please enter receiver mobile number!");
                                                                                                                            }
                                                                                                                        } else {
                                                                                                                            Uitility.showToast(context, "Please enter receiver name!");
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        Uitility.showToast(context, "Please check Hazardous, Prohibited, Fragile and Flammable goods are not eligible for delivery!");
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    Uitility.showToast(context, "Please check Hazardous, Prohibited, Fragile and Flammable goods are not eligible for delivery!");
                                                                                                                }
                                                                                                            } else {
                                                                                                                Uitility.showToast(context, "Please check Hazardous, Prohibited, Fragile and Flammable goods are not eligible for delivery!");
                                                                                                            }
                                                                                                        } else {
                                                                                                            Uitility.showToast(context, "Please check Hazardous, Prohibited, Fragile and Flammable goods are not eligible for delivery!");
                                                                                                        }
                                                                                                    } else {
                                                                                                        Uitility.showToast(context, "Please select Flammable Toxic Explosive type!");
                                                                                                    }
                                                                                                } else {
                                                                                                    Uitility.showToast(context, "Please select Fragile type!");
                                                                                                }
                                                                                            } else {
                                                                                                Uitility.showToast(context, "Please select Prohibited type!");
                                                                                            }
                                                                                        } else {
                                                                                            Uitility.showToast(context, "Please select Hazardous type!");
                                                                                        }
                                                                                    } else {
                                                                                        Uitility.showToast(context, "Please select ownership type!");
                                                                                    }
                                                                                } else {
                                                                                    Uitility.showToast(context, "Please enter goods values!");
                                                                                }
                                                                            } else {
                                                                                Uitility.showToast(context, "Please select Packaging!");
                                                                            }
                                                                        } else {
                                                                            Uitility.showToast(context, "Please select Weight!");
                                                                        }
                                                                    } else {
                                                                        Uitility.showToast(context, "Please select quality!");
                                                                    }
                                                                } else {
                                                                    Uitility.showToast(context, "Please enter goods description!");
                                                                }
                                                            } else {
                                                                Uitility.showToast(context, "Please select Nature of goods!");
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
        gpInvoice.setOnClickListener(this);
        gpParcel.setOnClickListener(this);
        imgParcel.setOnClickListener(this);
        imgParcelCamera.setOnClickListener(this);
        imgInvoice.setOnClickListener(this);
        tvParcel.setOnClickListener(this);
        tvInvoice.setOnClickListener(this);
        imgInvoiceCamera.setOnClickListener(this);
        ingStartDate.setOnClickListener(this);
        ingStartTime.setOnClickListener(this);
        ingEndDate.setOnClickListener(this);
        ingEndTime.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        cbPricingPolicy.setOnClickListener(this);
        cbTermsCondition.setOnClickListener(this);

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
                strtoPincode = etToPincode.getText().toString();
                strFromPincode = etFromPincode.getText().toString();
                strGoodsDescription = etGoodsDescription.getText().toString();
                strValueOgGood = etGoodsValue.getText().toString();
                strReceiverName = etReceiverName.getText().toString();
                strReceiverMobileNo = etReceiverMobileNo.getText().toString();
                strReceiverAddress = etReceiverAddress.getText().toString();

                if (fromCityId != 0) {
                    if (!strFromPincode.isEmpty()) {
                        if (strFromPincode.length() == 6) {
                            if (toCityId != 0) {
                                if (!strtoPincode.isEmpty()) {
                                    if (strtoPincode.length() == 6) {
                                        if (!strStartDate.isEmpty()) {
                                            if (!strEndDate.isEmpty()) {
                                                if (!strStartTime.isEmpty()) {
                                                    if (!strEndTime.isEmpty()) {
                                                        if (!deliveryOption.equals("Select Delivery Option")) {
                                                            if (!natureOfGoods.equals("Select Nature Of Goods")) {
                                                                if (!strGoodsDescription.isEmpty()) {
                                                                    if (!strQuality.equals("Select Quality")) {
                                                                        if (weightId != 0) {
                                                                            if (!strPackaging.equals("Select Packaging")) {
                                                                                if (!strValueOgGood.isEmpty()) {
                                                                                    if (!strOwnership.isEmpty()) {
                                                                                        if (!rgStrHazardous.isEmpty()) {
                                                                                            if (!rgStrProhibited.isEmpty()) {
                                                                                                if (!rgStrFraglle.isEmpty()) {
                                                                                                    if (!rgStrFlamableToxicExplosive.isEmpty()) {
                                                                                                        if (rgStrFlamableToxicExplosive.equals("No")) {
                                                                                                            if (rgStrFraglle.equals("No")) {
                                                                                                                if (rgStrProhibited.equals("No")) {
                                                                                                                    if (rgStrHazardous.equals("No")) {
                                                                                                                        if (!strReceiverName.isEmpty()) {
                                                                                                                            if (!strReceiverMobileNo.isEmpty()) {
                                                                                                                                if (!strReceiverAddress.isEmpty()) {
                                                                                                                                    if (cbPricingPolicy.isChecked()) {
                                                                                                                                        if (cbTermsCondition.isChecked()) {
                                                                                                                                            if (!strParcelFilePath.isEmpty()) {
                                                                                                                                                if (Integer.parseInt(strValueOgGood) <= 50000) {
                                                                                                                                                    if (strOwnership.equals("Commercial")) {
                                                                                                                                                        if (!strInvoiceFilePath.isEmpty()) {
                                                                                                                                                            if (Connectivity.isConnected(context)) {
                                                                                                                                                                //TODO API Call
                                                                                                                                                                sendParcel();
                                                                                                                                                            }
                                                                                                                                                        } else {
                                                                                                                                                            Uitility.showToast(context, "Please select invoice picture");
                                                                                                                                                        }
                                                                                                                                                    } else {
                                                                                                                                                        if (Connectivity.isConnected(context)) {
                                                                                                                                                            //TODO API Call
                                                                                                                                                            sendParcel();
                                                                                                                                                        }
                                                                                                                                                    }
                                                                                                                                                } else {
                                                                                                                                                    Uitility.showToast(context, "We deliver order below amount 50,000 !");
                                                                                                                                                }
                                                                                                                                            } else {
                                                                                                                                                Uitility.showToast(context, "Please select parcel image!");
                                                                                                                                            }
                                                                                                                                        } else {
                                                                                                                                            Uitility.showToast(context, "Please accept terms and condition!");
                                                                                                                                        }
                                                                                                                                    } else {
                                                                                                                                        Uitility.showToast(context, "Please accept pricing policy!");
                                                                                                                                    }
                                                                                                                                } else {
                                                                                                                                    Uitility.showToast(context, "Please enter receiver address!");
                                                                                                                                }
                                                                                                                            } else {
                                                                                                                                Uitility.showToast(context, "Please enter receiver mobile number!");
                                                                                                                            }
                                                                                                                        } else {
                                                                                                                            Uitility.showToast(context, "Please enter receiver name!");
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        Uitility.showToast(context, "Please check Hazardous, Prohibited, Fragile and Flammable goods are not eligible for delivery!");
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    Uitility.showToast(context, "Please check Hazardous, Prohibited, Fragile and Flammable goods are not eligible for delivery!");
                                                                                                                }
                                                                                                            } else {
                                                                                                                Uitility.showToast(context, "Please check Hazardous, Prohibited, Fragile and Flammable goods are not eligible for delivery!");
                                                                                                            }
                                                                                                        } else {
                                                                                                            Uitility.showToast(context, "Please check Hazardous, Prohibited, Fragile and Flammable goods are not eligible for delivery!");
                                                                                                        }
                                                                                                    } else {
                                                                                                        Uitility.showToast(context, "Please select Flammable Toxic Explosive type!");
                                                                                                    }
                                                                                                } else {
                                                                                                    Uitility.showToast(context, "Please select Fragile type!");
                                                                                                }
                                                                                            } else {
                                                                                                Uitility.showToast(context, "Please select Prohibited type!");
                                                                                            }
                                                                                        } else {
                                                                                            Uitility.showToast(context, "Please select Hazardous type!");
                                                                                        }
                                                                                    } else {
                                                                                        Uitility.showToast(context, "Please select ownership type!");
                                                                                    }
                                                                                } else {
                                                                                    Uitility.showToast(context, "Please enter goods values!");
                                                                                }
                                                                            } else {
                                                                                Uitility.showToast(context, "Please select Packaging!");
                                                                            }
                                                                        } else {
                                                                            Uitility.showToast(context, "Please select Weight!");
                                                                        }
                                                                    } else {
                                                                        Uitility.showToast(context, "Please select quality!");
                                                                    }
                                                                } else {
                                                                    Uitility.showToast(context, "Please enter goods description!");
                                                                }
                                                            } else {
                                                                Uitility.showToast(context, "Please select Nature of goods!");
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
            case R.id.gpInvoice:
            case R.id.imgInvoice:
            case R.id.tvInvoice:
            case R.id.imgInvoiceCamera:
                gotoSelectPicture("invoice");
                break;
            case R.id.gpParcel:
            case R.id.imgParcel:
            case R.id.tvParcel:
            case R.id.imgParcelCamera:
                gotoSelectPicture("parcel");
                break;
            case R.id.btnCancel:
                //((MainActivity) context).getSupportFragmentManager().popBackStack();
                etFromPincode.setText("");
                etToPincode.setText("");
                etStartTime.setText("");
                etEndTime.setText("");
                etStartDate.setText("");
                etEndDate.setText("");
                etGoodsDescription.setText("");
                etGoodsValue.setText("");
                etReceiverName.setText("");
                etReceiverMobileNo.setText("");
                etReceiverAddress.setText("");
                spinnerFromState.setSelection(0);
                spinnerFromCity.setSelection(0);
                spinnerToState.setSelection(0);
                spinnerToCity.setSelection(0);
                spinnerDeliveryOption.setSelection(0);
                spinnerNatureGoods.setSelection(0);
                spinnerQuality.setSelection(0);
                spinnerWeight.setSelection(0);
                spinnerPackaging.setSelection(0);
                rgOwnership.setSelected(false);
                rgHazardous.setSelected(false);
                rgProhibited.setSelected(false);
                rgFraglle.setSelected(false);
                rgFlamableToxicExplosive.setSelected(false);
                cbPricingPolicy.setChecked(false);
                cbTermsCondition.setChecked(false);
                rbCommercial.setChecked(false);
                rbNonCommercial.setChecked(false);
                rbHazardousNo.setChecked(false);
                rbHazardousYes.setChecked(false);
                rbProhibitedNo.setChecked(false);
                rbProhibitedYes.setChecked(false);
                rbFraglleNo.setChecked(false);
                rbFraglleYes.setChecked(false);
                rbFlamableToxicExplosiveNo.setChecked(false);
                rbFlamableToxicExplosiveYes.setChecked(false);

                break;
            case R.id.cbPricingPolicy:
                cbPricingPolicy.setChecked(true);
                showPricingPolicy();
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
        wvTermsConditions.loadUrl("file:///android_res/raw/" + "terms_and_condition.html");

        TextView tvOk = alertLayout.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showPricingPolicy() {
        LayoutInflater inflater = ((AppCompatActivity) context).getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.dialog_pricig_policy, null);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final AlertDialog dialog = alert.create();
        dialog.show();

        TextView tvOk = alertLayout.findViewById(R.id.tvOk);
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void openDatePickerDialog() {
        DatePickerDialog mDate = new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        mDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDate.show();
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
                strStartDate = Uitility.dateFormat(year, monthOfYear, dayOfMonth); //SendParcelFragment
                etStartDate.setText(strStartDate);
            } else if (tag.equals("end_date")) {
                strEndDate = Uitility.dateFormat(year, monthOfYear, dayOfMonth); //SendParcelFragment
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
        viewProgressDialog.showProgress(context);

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
                        Toast.makeText(context, "Send Parcel Data entered Successfully", Toast.LENGTH_SHORT).show();

                        commonRedirect(); //sendParcel
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
        viewProgressDialog.showProgress(context);

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

                        commonRedirect(); //updateMyParcel
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

    private void commonRedirect() {
        FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                dashboardFragment, R.id.frame_container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
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

    private void getNatureOfGoods() {
        RetrofitInstance.getClient().getNatureOfGoods().enqueue(new Callback<NatureOfGoodsResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<NatureOfGoodsResponseModel> call, @NonNull Response<NatureOfGoodsResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        natureOfGoodsResponseModel = response.body();

                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        userSession.setNatureOfGoods(json);

                        ArrayList<String> natureOfGoodsArrayList = new ArrayList<>();
                        natureOfGoodsArrayList.add(0, "Select Nature Of Goods");
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            natureOfGoodsArrayList.add(response.body().getData().get(i).getNatureOfGoods());
                            bindNatureOfGoodsDataToSpinner(natureOfGoodsArrayList);
                        }
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NatureOfGoodsResponseModel> call, @NonNull Throwable t) {
            }
        });
    }

    private void bindNatureOfGoodsDataToSpinner(ArrayList<String> data) {
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> aa = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
                data);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerNatureGoods.setAdapter(aa);

        if (edit != null && !edit.equals("")) {
            if (edit.equals("edit")) {
                if (natureOfGoods != null) {
                    int spinnerPosition = aa.getPosition(natureOfGoods);
                    spinnerNatureGoods.setSelection(spinnerPosition);
                }
            }
        }
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
    }

    private void getQuality() {
        RetrofitInstance.getClient().getQuality().enqueue(new Callback<QualityResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<QualityResponseModel> call, @NonNull Response<QualityResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        qualityResponseModel = response.body();

                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        userSession.setQuality(json);

                        ArrayList<String> qualityArrayList = new ArrayList<>();
                        qualityArrayList.add(0, "Select Quality");
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            qualityArrayList.add(response.body().getData().get(i).getQuality());
                            bindQualityDataToSpinner(qualityArrayList);
                        }
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<QualityResponseModel> call, @NonNull Throwable t) {
            }
        });
    }

    private void bindQualityDataToSpinner(ArrayList<String> data) {
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> aa = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
                data);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerQuality.setAdapter(aa);

        if (edit != null && !edit.equals("")) {
            if (edit.equals("edit")) {
                if (strQuality != null) {
                    int spinnerPosition = aa.getPosition(strQuality);
                    spinnerQuality.setSelection(spinnerPosition);
                }
            }
        }
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
    }

    private void getPackaging() {
        RetrofitInstance.getClient().getPackaging().enqueue(new Callback<PackagingResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<PackagingResponseModel> call, @NonNull Response<PackagingResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        packagingResponseModel = response.body();

                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        userSession.setPackaging(json);

                        ArrayList<String> packagingArrayList = new ArrayList<>();
                        packagingArrayList.add(0, "Select Packaging");
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            packagingArrayList.add(response.body().getData().get(i).getPackaging());
                            bindPackagingDataToSpinner(packagingArrayList);
                        }
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PackagingResponseModel> call, @NonNull Throwable t) {
            }
        });
    }

    private void bindPackagingDataToSpinner(ArrayList<String> data) {
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> aa = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item,
                data);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerPackaging.setAdapter(aa);

        if (edit != null && !edit.equals("")) {
            if (edit.equals("edit")) {
                if (strPackaging != null) {
                    int spinnerPosition = aa.getPosition(strPackaging);
                    spinnerPackaging.setSelection(spinnerPosition);
                }
            }
        }
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
