package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.GenerateOTPResponseModel;
import com.aystech.sandesh.model.SubmitOTPResponseModel;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Connectivity;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KYCFragment extends Fragment {

    private Context context;

    String clientId, aadhaarNo, fullName, gender;

    private DashboardFragment dashboardFragment;

    private ConstraintLayout clAadhaarOTP, clAadhaarVerification;
    private EditText etAadhaarNo, etOTP;
    private Button btnSendOTP, btnVerifyOTP;

    private ViewProgressDialog viewProgressDialog;

    public KYCFragment() {
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
        View view = inflater.inflate(R.layout.fragment_k_y_c, container, false);

        dashboardFragment = (DashboardFragment) Fragment.instantiate(context,
                DashboardFragment.class.getName());

        initView(view);

        onClickView();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        clAadhaarOTP = view.findViewById(R.id.clAadhaarOTP);
        clAadhaarVerification = view.findViewById(R.id.clAadhaarVerification);
        etAadhaarNo = view.findViewById(R.id.etAadhaarNo);
        btnSendOTP = view.findViewById(R.id.btnSendOTP);
        etOTP = view.findViewById(R.id.etOTP);
        btnVerifyOTP = view.findViewById(R.id.btnVerifyOTP);
    }

    private void onClickView() {
        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etAadhaarNo.getText().toString())) {
                    etAadhaarNo.setError("Please enter your Aadhaar No");
                    etAadhaarNo.requestFocus();
                } else {
                    etAadhaarNo.setError(null);

                    if (Connectivity.isConnected(context)) {
                        //TODO API Call here
                        generateOTP();
                    }
                }
            }
        });
    }

    private void generateOTP() {
        viewProgressDialog.showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id_number", etAadhaarNo.getText().toString().trim());

        RetrofitInstance.getKYCClient().generateOTP(jsonObject).enqueue(new Callback<GenerateOTPResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<GenerateOTPResponseModel> call, @NonNull Response<GenerateOTPResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        clientId = response.body().getData().getClientId();

                        clAadhaarOTP.setVisibility(View.GONE);
                        clAadhaarVerification.setVisibility(View.VISIBLE);

                        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (TextUtils.isEmpty(etOTP.getText().toString())) {
                                    etOTP.setError("Please enter correct OTP");
                                    etOTP.requestFocus();
                                } else {
                                    etOTP.setError(null);

                                    if (Connectivity.isConnected(context)) {
                                        //TODO API Call here
                                        submitOTP(clientId);
                                    }
                                }
                            }
                        });
                    } else {
                        Toast.makeText(context, "Something went wrong, please try again!!!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<GenerateOTPResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void submitOTP(String clientId) {
        viewProgressDialog.showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("client_id", clientId);
        jsonObject.addProperty("otp", etOTP.getText().toString().trim());

        RetrofitInstance.getKYCClient().submitOTP(jsonObject).enqueue(new Callback<SubmitOTPResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SubmitOTPResponseModel> call, @NonNull Response<SubmitOTPResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getSuccess()) {
                        aadhaarNo = response.body().getData().getAadhaarNumber();
                        fullName = response.body().getData().getFullName();
                        gender = response.body().getData().getGender();

                        //TODO API Call here
                        addKYC(aadhaarNo, fullName, gender);
                    } else {
                        Toast.makeText(context, "Verification failed, please try again!!!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SubmitOTPResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void addKYC(String aadhaarNo, String fullName, String gender) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("aadhaar_number", aadhaarNo);
        jsonObject.addProperty("full_name", fullName);
        jsonObject.addProperty("gender", gender);

        RetrofitInstance.getClient().addKYC(jsonObject).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_LONG).show();

                    FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(), dashboardFragment, R.id.frame_container,
                            false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }
}