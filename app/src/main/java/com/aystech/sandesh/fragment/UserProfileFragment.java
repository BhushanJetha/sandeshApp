package com.aystech.sandesh.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private UpdateUserProfileFragment updateUserProfileFragment;
    private UpdateAddressFragment updateAddressFragment;

    private UserModel userModel;
    private AddressModel addressModel;

    private TextView tvFullName, tvEmailID, tvMobileNumber, tvDateOfBirth, tvGender,
            tvAddresLine1, tvAddresLine2, tvLandmark,
            tvState, tvCity;
    private LinearLayout editPersonalDetail;
    private LinearLayout editAddressDetail;

    private ViewProgressDialog viewProgressDialog;

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

        updateUserProfileFragment = (UpdateUserProfileFragment)
                Fragment.instantiate(context, UpdateUserProfileFragment.class.getName());
        updateAddressFragment = (UpdateAddressFragment)
                Fragment.instantiate(context, UpdateAddressFragment.class.getName());

        initView(view);

        onCLickListener();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        editPersonalDetail = view.findViewById(R.id.editPersonalDetail);
        editAddressDetail = view.findViewById(R.id.editAddressDetail);
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

    private void onCLickListener() {
        editPersonalDetail.setOnClickListener(this);
        editAddressDetail.setOnClickListener(this);
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
                        if (response.body().getData().getUserData().getFirstName() != null
                                && !response.body().getData().getUserData().getFirstName().equals("")) {
                            tvFullName.setText(response.body().getData().getUserData().getFirstName());
                            if ((response.body().getData().getUserData().getFirstName() != null &&
                                    !response.body().getData().getUserData().getFirstName().equals("")) &&
                                    (response.body().getData().getUserData().getLastName() != null &&
                                            !response.body().getData().getUserData().getLastName().equals(""))) {
                                tvFullName.setText(response.body().getData().getUserData().getFirstName() + " " +
                                        response.body().getData().getUserData().getLastName());
                                if ((response.body().getData().getUserData().getFirstName() != null &&
                                        !response.body().getData().getUserData().getFirstName().equals("")) &&
                                        (response.body().getData().getUserData().getMiddleName() != null &&
                                                !response.body().getData().getUserData().getMiddleName().equals("")) &&
                                        (response.body().getData().getUserData().getLastName() != null &&
                                                !response.body().getData().getUserData().getLastName().equals(""))) {
                                    tvFullName.setText(response.body().getData().getUserData().getFirstName() + " " +
                                            response.body().getData().getUserData().getMiddleName() + " " +
                                            response.body().getData().getUserData().getLastName());
                                }
                            }
                        }
                        tvEmailID.setText(response.body().getData().getUserData().getEmailId());
                        tvMobileNumber.setText(response.body().getData().getUserData().getMobileNo());
                        tvDateOfBirth.setText(response.body().getData().getUserData().getBirthDate());
                        tvGender.setText(response.body().getData().getUserData().getGender());

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

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.editPersonalDetail:
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        updateUserProfileFragment, R.id.frame_container, true);
                bundle.putParcelable("userModel", userModel);
                updateUserProfileFragment.setArguments(bundle);
                break;

            case R.id.editAddressDetail:
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        updateAddressFragment, R.id.frame_container, true);
                bundle.putParcelable("addressModel", addressModel);
                updateAddressFragment.setArguments(bundle);
                break;
        }
    }
}
