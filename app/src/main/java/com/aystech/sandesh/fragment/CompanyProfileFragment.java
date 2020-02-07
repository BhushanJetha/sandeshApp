package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.AddressModel;
import com.aystech.sandesh.model.ProfileResponseModel;
import com.aystech.sandesh.model.UserModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyProfileFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private UpdateAddressFragment updateAddressFragment;

    private UserModel userModel;
    private AddressModel addressModel;

    private ImageView imgCompanyProfile;
    private TextView tvCompanyName, tvCompany, tvBranch, tvAuthorisedName, tvDesignation, tvMobileNumber, tvEmailId,
            tvAddresLine1, tvAddresLine2, tvLandmark, tvState, tvCity;
    private LinearLayout editCompanyDetail, editAddressDetail;

    private ViewProgressDialog viewProgressDialog;

    public CompanyProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_company_profile, container, false);

        updateAddressFragment = (UpdateAddressFragment)
                Fragment.instantiate(context, UpdateAddressFragment.class.getName());

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        imgCompanyProfile = view.findViewById(R.id.imgCompanyProfile);
        tvCompanyName = view.findViewById(R.id.tvCompanyName);
        tvCompany = view.findViewById(R.id.tvCompany);
        tvBranch = view.findViewById(R.id.tvBranch);
        tvAuthorisedName = view.findViewById(R.id.tvAuthorisedName);
        tvDesignation = view.findViewById(R.id.tvDesignation);
        tvMobileNumber = view.findViewById(R.id.tvMobileNumber);
        tvEmailId = view.findViewById(R.id.tvEmailId);
        tvAddresLine1 = view.findViewById(R.id.tvAddressLine1);
        tvAddresLine2 = view.findViewById(R.id.tvAddressLine2);
        tvLandmark = view.findViewById(R.id.tvLandmark);
        tvState = view.findViewById(R.id.tvState);
        tvCity = view.findViewById(R.id.tvCity);

        editCompanyDetail = view.findViewById(R.id.editCompanyDetail);
        editAddressDetail = view.findViewById(R.id.editAddressDetail);
    }

    private void onClickListener() {
        editCompanyDetail.setOnClickListener(this);
        editAddressDetail.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.editCompanyDetail:
                break;

            case R.id.editAddressDetail:
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        updateAddressFragment, R.id.frame_container, true);
                bundle.putParcelable("addressModel", addressModel);
                updateAddressFragment.setArguments(bundle);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);

        //TODO API Call
        getProfile();
    }

    private void getProfile() {
        ViewProgressDialog.getInstance().showProgress(context);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<ProfileResponseModel> call = apiInterface.getProfile();
        call.enqueue(new Callback<ProfileResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ProfileResponseModel> call, @NonNull Response<ProfileResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        userModel = response.body().getData().getUserData();
                        addressModel = response.body().getData().getAddress();

                        //this is for profile
                        Glide.with(context)
                                .load(response.body().getData().getUserData().getProfileImg())
                                .error(R.drawable.ic_logo_sandesh)
                                .into(imgCompanyProfile);

                        tvEmailId.setText(response.body().getData().getUserData().getEmailId());
                        tvMobileNumber.setText(response.body().getData().getUserData().getMobileNo());

                        //this is for company information
                        tvCompanyName.setText(response.body().getData().getCorporateData().getCompanyName());
                        tvCompany.setText(response.body().getData().getCorporateData().getCompanyName());
                        tvBranch.setText(response.body().getData().getCorporateData().getBranch());
                        tvAuthorisedName.setText(response.body().getData().getCorporateData().getAuthPersonName());
                        tvDesignation.setText(response.body().getData().getCorporateData().getDesignation());

                        //this is for address
                        tvAddresLine1.setText(response.body().getData().getAddress().getAddressLine1());
                        if (response.body().getData().getAddress().getAddressLine2() != null &&
                                !response.body().getData().getAddress().getAddressLine2().equals("")) {
                            tvAddresLine2.setText(response.body().getData().getAddress().getAddressLine2());
                        } else {
                            tvAddresLine2.setText("-");
                        }
                        tvLandmark.setText(response.body().getData().getAddress().getLandmark());
                        tvState.setText(response.body().getData().getAddress().getState());
                        tvCity.setText(response.body().getData().getAddress().getCity());
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProfileResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }
}
