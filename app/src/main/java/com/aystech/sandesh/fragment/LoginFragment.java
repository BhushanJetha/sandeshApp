package com.aystech.sandesh.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.activity.CorporateRegistrationActivity;
import com.aystech.sandesh.activity.IndividualRegistrationActivity;
import com.aystech.sandesh.activity.LoginActivity;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.R;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private Context context;
    private DashboardFragment dashboardFragment;
    private EditText etOTP;
    private Button btnLogin;
    private TextView tvResendOTP;
    private String strOTP, strMobileNumber;
    private ViewProgressDialog viewProgressDialog;

    public LoginFragment() {
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

        dashboardFragment = (DashboardFragment) Fragment.instantiate(context,
                DashboardFragment.class.getName());

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        btnLogin = view.findViewById(R.id.btnVerify);
        etOTP = view.findViewById(R.id.etOTP);
        tvResendOTP = view.findViewById(R.id.tvResendOTP);

        strMobileNumber = getArguments().getString("mobileNumber");
        viewProgressDialog = ViewProgressDialog.getInstance();
    }

    private void onClickListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strOTP = etOTP.getText().toString();
                if(!strOTP.isEmpty()){

                } else {
                    Toast.makeText(getActivity(),"Please enter valid OTP !!", Toast.LENGTH_SHORT).show();
                }

                Intent i = null;
                if(Constants.userType.equals("individual")){
                    i = new Intent(getActivity(),   IndividualRegistrationActivity.class);
                }else  if(Constants.userType.equals("corporate")) {
                    i = new Intent(getActivity(), CorporateRegistrationActivity.class);
                }
                startActivity(i);
                getActivity().finish();
            }
        });
    }

    private void doVerifyOTPAPICall() {
        ViewProgressDialog.getInstance().showProgress(this.getActivity());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobileNumber",strMobileNumber);
        jsonObject.addProperty("otp",strOTP);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.doOTPVerification(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {

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
        ((MainActivity) context).setUpToolbar(false, false,"", false);
    }
}
