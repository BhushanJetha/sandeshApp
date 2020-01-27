package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.R;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MobileVerificationFragment extends Fragment {

    Context context;
    private VerifyOTPFragment verifyOTPFragment;
    private Button btnSubmit;
    private RadioButton rbIndividual, rbCorporate;
    private EditText etMobileNumber;
    private String strMobileNumber, strUserType;
    private ViewProgressDialog viewProgressDialog;

    public MobileVerificationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_mobile_verification, container, false);

        verifyOTPFragment = (VerifyOTPFragment) Fragment.instantiate(context, VerifyOTPFragment.class.getName());

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        btnSubmit = view.findViewById(R.id.btnSubmit);
        rbIndividual = view.findViewById(R.id.rbIndividual);
        rbCorporate = view.findViewById(R.id.rbCorporate);
        etMobileNumber = view.findViewById(R.id.etMobileNumber);

        viewProgressDialog = ViewProgressDialog.getInstance();
    }

    private void onClickListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                strMobileNumber = etMobileNumber.getText().toString();

                if(!Constants.userType.isEmpty()){
                    if(!strMobileNumber.isEmpty() && strMobileNumber.length() == 10){
                        doVerifyOTPAPICall();
                    } else {
                        Toast.makeText(getActivity(),"Please enter valid mobile number !!", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getActivity(),"Please select your registration type !!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        rbIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.userType = "individual";
            }
        });

        rbCorporate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.userType = "corporate";
            }
        });
    }

    private void doVerifyOTPAPICall() {
        ViewProgressDialog.getInstance().showProgress(this.getActivity());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("mobile_no",strMobileNumber);
        jsonObject.addProperty("user_type",Constants.userType);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.getOTP(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if(response.body().getStatus()){
                        FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                                verifyOTPFragment, R.id.frame_container, true);
                        Bundle bundle = new Bundle();
                        bundle.putString("mobileNumber", strMobileNumber);
                        verifyOTPFragment.setArguments(bundle);
                    }else {
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
        ((MainActivity) context).setUpToolbar(false, false,"", false);
    }
}
