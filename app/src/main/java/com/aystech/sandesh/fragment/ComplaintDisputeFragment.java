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
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.IndividualRegistrationActivity;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintDisputeFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private EditText etName, etEmail, etMessage;
    private Button btnSubmit;
    private String strName, strEmailId, strMessage;
    private ViewProgressDialog viewProgressDialog;

    public ComplaintDisputeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_complaint_dispute, container, false);

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etMessage = view.findViewById(R.id.etMessage);
        btnSubmit = view.findViewById(R.id.btnSubmit);
    }

    private void onClickListener() {
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:

                strName = etName.getText().toString().trim();
                strEmailId = etEmail.getText().toString().trim();
                strMessage = etMessage.getText().toString().trim();

                if(!strName.isEmpty()){
                    if(!strEmailId.isEmpty()){
                        if(Uitility.isValidEmailId(strEmailId)){
                            if(!strMessage.isEmpty()){
                                //TODO API Call
                                sendComplaint();
                            }else {
                                Uitility.showToast(getActivity(), "Please enter your message !!");
                            }
                        }else {
                            Uitility.showToast(getActivity(), "Please enter valid email id !!");
                        }
                    }else {
                        Uitility.showToast(getActivity(), "Please enter email Id !!");
                    }
                }else {
                    Uitility.showToast(getActivity(), "Please enter your name !!");
                }

                break;
        }
    }

    private void sendComplaint() {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", strName);
        jsonObject.addProperty("email_id", strEmailId);
        jsonObject.addProperty("message", strMessage);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.sendComplaint(
                jsonObject
        );
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
