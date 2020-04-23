package com.aystech.sandesh.fragment;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.AddressModel;
import com.aystech.sandesh.model.CityModel;
import com.aystech.sandesh.model.CityResponseModel;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.StateModel;
import com.aystech.sandesh.model.StateResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAddressFragment extends Fragment {

    private Context context;

    private AddressModel addressModel;

    private StateResponseModel stateResponseModel;
    private CityResponseModel cityResponseModel;

    private Spinner spinnerState, spinnerCity;
    private EditText etAddressLine1, etAddressLine2, etLandmark, etPincode;
    private Button btnUpdateAddress;

    private int stateId, cityId;
    private String strAddressLine1, strAddressLine2, strLandmark, strPincode;

    private ViewProgressDialog viewProgressDialog;

    public UpdateAddressFragment() {
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
        View view = inflater.inflate(R.layout.fragment_update_address, container, false);

        if (getArguments() != null)
            addressModel = getArguments().getParcelable("addressModel");

        initView(view);

        setDataToUI();

        onClick();

        //TODO API Call
        getState();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();
        spinnerState = view.findViewById(R.id.spinnerState);
        spinnerCity = view.findViewById(R.id.spinnerCity);

        etAddressLine1 = view.findViewById(R.id.etAddressLine1);
        etAddressLine2 = view.findViewById(R.id.etAddressLine2);
        etLandmark = view.findViewById(R.id.etLandmark);
        etPincode = view.findViewById(R.id.etPincode);

        btnUpdateAddress = view.findViewById(R.id.btnUpdateAddress);
    }

    private void setDataToUI() {
        etAddressLine1.setText(addressModel.getAddressLine1());
        if (addressModel.getAddressLine2() != null &&
                !addressModel.getAddressLine2().equals("")) {
            etAddressLine2.setText(addressModel.getAddressLine2());
        }
        etLandmark.setText(addressModel.getLandmark());
        etPincode.setText("" + addressModel.getPincode());
    }

    private void onClick() {
        btnUpdateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strAddressLine1 = etAddressLine1.getText().toString();
                strAddressLine2 = etAddressLine2.getText().toString();
                strLandmark = etLandmark.getText().toString();
                strPincode = etPincode.getText().toString();

                //TODO API Call
                updateAddress();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
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

        spinnerState.setAdapter(adapter);
        if (addressModel.getState() != null) {
            int spinnerPosition = adapter.getPosition(addressModel.getState());
            spinnerState.setSelection(spinnerPosition);
        }

        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    stateId = stateResponseModel.getStateId(selectedItem);
                    getCity(stateId);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getCity(int strStateId) {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("state_id", strStateId);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CityResponseModel> call = apiInterface.getCity(jsonObject);
        call.enqueue(new Callback<CityResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<CityResponseModel> call, @NonNull Response<CityResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        cityResponseModel = response.body();
                        bindCityDataToUI(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CityResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindCityDataToUI(List<CityModel> data) {
        ArrayList<String> manufactureArrayList = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            manufactureArrayList.add(data.get(i).getCityName());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, manufactureArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCity.setAdapter(adapter);
        if (addressModel.getCity() != null) {
            int spinnerPosition = adapter.getPosition(addressModel.getCity());
            spinnerCity.setSelection(spinnerPosition);
        }

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    cityId = cityResponseModel.getCityId(selectedItem);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void updateAddress() {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("state_id", stateId);
        jsonObject.addProperty("city_id", cityId);
        jsonObject.addProperty("address_line1", strAddressLine1);
        jsonObject.addProperty("address_line2", strAddressLine2);
        jsonObject.addProperty("pincode", strPincode);
        jsonObject.addProperty("landmark", strLandmark);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.updateAddress(jsonObject);
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

}
