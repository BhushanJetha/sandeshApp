package com.aystech.sandesh.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.CorporateRegistrationActivity;
import com.aystech.sandesh.activity.IndividualRegistrationActivity;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOTPFragment extends Fragment {

    private Context context;
    private DashboardFragment dashboardFragment;
    private EditText etOTP;
    private Button btnLogin;
    private TextView tvResendOTP;
    private String strOTP, strMobileNumber;
    private ViewProgressDialog viewProgressDialog;

    public VerifyOTPFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        if (getArguments() != null)
            strMobileNumber = getArguments().getString("mobileNumber");

        dashboardFragment = (DashboardFragment) Fragment.instantiate(context,
                DashboardFragment.class.getName());

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        btnLogin = view.findViewById(R.id.btnVerify);
        etOTP = view.findViewById(R.id.etOTP);
        tvResendOTP = view.findViewById(R.id.tvResendOTP);

        SpannableString resendOTP = new SpannableString(getResources().getString(R.string.resend_otp));
        resendOTP.setSpan(new UnderlineSpan(), 0, resendOTP.length(), 0);
        tvResendOTP.setText(resendOTP);
    }

    private void onClickListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strOTP = etOTP.getText().toString();
                if (!strOTP.isEmpty()) {
                    doVerifyOTPAPICall();
                } else {
                    Toast.makeText(getActivity(), "Please enter valid OTP !!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOTPAPICall();
            }
        });
    }

    private void getOTPAPICall() {
        ViewProgressDialog.getInstance().showProgress(this.getActivity());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile_no", strMobileNumber);
        jsonObject.addProperty("user_type", Constants.userType);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.getOTP(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Uitility.showToast(getActivity(), "OTP has been resent!");
                    } else {
                        Uitility.showToast(getActivity(), response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void doVerifyOTPAPICall() {
        ViewProgressDialog.getInstance().showProgress(this.getActivity());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile_no", strMobileNumber);
        jsonObject.addProperty("otp", strOTP);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.doOTPVerification(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Uitility.showToast(getActivity(), response.body().getMessage());
                        Intent i = null;
                        if (Constants.userType.equals("individual")) {
                            i = new Intent(getActivity(), IndividualRegistrationActivity.class);
                        } else if (Constants.userType.equals("corporate")) {
                            i = new Intent(getActivity(), CorporateRegistrationActivity.class);
                        }
                        i.putExtra("mobileNumber", strMobileNumber);
                        startActivity(i);
                        getActivity().finish();
                    } else {
                        Uitility.showToast(getActivity(), response.body().getMessage());
                    }
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
        ((MainActivity) context).setUpToolbar(false, false, "", false);
    }
}
