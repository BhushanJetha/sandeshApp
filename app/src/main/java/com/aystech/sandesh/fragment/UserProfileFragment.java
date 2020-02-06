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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.CityModel;
import com.aystech.sandesh.model.CityResponseModel;
import com.aystech.sandesh.model.ShowHistoryResponseModel;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {

    private Context context;
    private ViewProgressDialog viewProgressDialog;
    private TextView tvFullName, tvEmailID, tvMobileNumber, tvDateOfBirth, tvGender, tvAddresLine1, tvAddresLine2, tvLandmark,
            tvState, tvCity;

    public UserProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        tvFullName = view.findViewById(R.id.tvFullName);
        tvMobileNumber = view.findViewById(R.id.tvMobileNumber);
        tvEmailID = view.findViewById(R.id.tvEmailId);
        tvGender = view.findViewById(R.id.tvGender);
        tvDateOfBirth = view.findViewById(R.id.tvDateOfBirth);
        tvAddresLine1 = view.findViewById(R.id.tvAddressLine1);
        tvAddresLine2 = view.findViewById(R.id.tvAddressLine2);
        tvLandmark = view.findViewById(R.id.tvLandmark);
        tvState = view.findViewById(R.id.tvState);
        tvCity = view.findViewById(R.id.tvCity);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }

    private void getProfile() {
        ViewProgressDialog.getInstance().showProgress(context);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<ShowHistoryResponseModel> call = apiInterface.getProfile();
        call.enqueue(new Callback<ShowHistoryResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ShowHistoryResponseModel> call, @NonNull Response<ShowHistoryResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        //showHistoryInnerModel = response.body().getData();
                        //bindDataToRV("travel");
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShowHistoryResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

}
