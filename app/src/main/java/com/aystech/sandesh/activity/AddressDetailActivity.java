package com.aystech.sandesh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.model.CityModel;
import com.aystech.sandesh.model.CityResponseModel;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.StateModel;
import com.aystech.sandesh.model.StateResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.UserSession;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressDetailActivity extends AppCompatActivity {

    StateResponseModel stateResponseModel;
    CityResponseModel cityResponseModel;
    private Button btnSubmit;
    private Spinner spState, spCity;
    private EditText etAddressLine1, etAddressLine2, etLandmark, etPincode;
    private String strAddressLine1, strAddressLine2, strLandmark, strPincode;
    private int strStateId, strCityId;
    private ViewProgressDialog viewProgressDialog;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);

        init();
        onClick();

        String fromState = userSession.getFromState();
        if (fromState.length() > 0) {
            Gson gson = new Gson();
            stateResponseModel = gson.fromJson(fromState, StateResponseModel.class);
            bindStateDataToUI(stateResponseModel.getData());
        } else {
            //TODO API Call
            getState();
        }
    }

    private void init() {
        viewProgressDialog = ViewProgressDialog.getInstance();
        userSession = new UserSession(this);

        spState = findViewById(R.id.spState);
        spCity = findViewById(R.id.spCity);
        etAddressLine1 = findViewById(R.id.etAddressLine1);
        etAddressLine2 = findViewById(R.id.etAddressLine2);
        etLandmark = findViewById(R.id.etLandmark);
        etPincode = findViewById(R.id.etPincode);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void onClick() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strAddressLine1 = etAddressLine1.getText().toString();
                strAddressLine2 = etAddressLine2.getText().toString();
                strLandmark = etLandmark.getText().toString();
                strPincode = etPincode.getText().toString();

                if(strCityId != 0){
                    if (!strAddressLine1.isEmpty()) {
                        if (!strLandmark.isEmpty()) {
                            if (!strPincode.isEmpty()) {
                                if(strPincode.length() < 6){
                                    doRigistrationAPICall();
                                } else {
                                    Uitility.showToast(AddressDetailActivity.this, "Please enter 6 digit pin code !");
                                }
                            } else {
                                Uitility.showToast(AddressDetailActivity.this, "Please enter pin code !");
                            }
                        } else {
                            Uitility.showToast(AddressDetailActivity.this, "Please enter landmark !");
                        }
                    } else {
                        Uitility.showToast(AddressDetailActivity.this, "Please enter address line 1 !");
                    }
                }else {
                    Uitility.showToast(AddressDetailActivity.this, "Please select your city !");
                }
            }
        });
    }

    private void doRigistrationAPICall() {
        ViewProgressDialog.getInstance().showProgress(this);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("state_id", strStateId);
        jsonObject.addProperty("city_id", strCityId);
        jsonObject.addProperty("address_line1", strAddressLine1);
        jsonObject.addProperty("address_line2", strAddressLine2);
        jsonObject.addProperty("landmark", strLandmark);
        jsonObject.addProperty("pincode", strPincode);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.doAddressRegistration(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Constants.fragmentType = "Dashboard";
                        Intent i = new Intent(AddressDetailActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(AddressDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
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
                        Toast.makeText(AddressDetailActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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

        manufactureArrayList.add(0,"Select State");
        for (int i = 0; i < data.size(); i++) {
            manufactureArrayList.add(data.get(i).getStateName());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, manufactureArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spState.setAdapter(adapter);

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    strStateId = stateResponseModel.getStateId(selectedItem);
                    getCity(strStateId);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getCity(int strStateId) {
        ViewProgressDialog.getInstance().showProgress(this);

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

        manufactureArrayList.add(0,"Select City");
        for (int i = 0; i < data.size(); i++) {
            manufactureArrayList.add(data.get(i).getCityName());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, manufactureArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spCity.setAdapter(adapter);

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    strCityId = cityResponseModel.getCityId(selectedItem);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
