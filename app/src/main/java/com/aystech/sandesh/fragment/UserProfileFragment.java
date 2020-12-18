package com.aystech.sandesh.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.aystech.sandesh.utils.AppController;
import com.aystech.sandesh.utils.Connectivity;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "UserProfileFragment";
    private Context context;

    private UpdateUserProfileFragment updateUserProfileFragment;
    private UpdateAddressFragment updateAddressFragment;
    private StartJourneyFragment startJourneyFragment;
    private OrderListFragment orderListFragment;

    private UserModel userModel;
    private AddressModel addressModel;

    private ImageView imgUserProfile;
    private TextView tvFullName, tvEmailID, tvMobileNumber, tvDateOfBirth, tvGender,
            tvAddresLine1, tvAddresLine2, tvLandmark, tvState, tvCity, tvPincode;
    private Button btnMyRide, btnMyOrders, btnUpcomingRides, btnUpcomingOrders;
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
        startJourneyFragment = (StartJourneyFragment)
                Fragment.instantiate(context, StartJourneyFragment.class.getName());
        orderListFragment = (OrderListFragment) Fragment.instantiate(context,
                OrderListFragment.class.getName());

        initView(view);

        onCLickListener();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        editPersonalDetail = view.findViewById(R.id.editPersonalDetail);
        editAddressDetail = view.findViewById(R.id.editAddressDetail);
        imgUserProfile = view.findViewById(R.id.imgUserProfile);
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
        tvPincode = view.findViewById(R.id.tvPincode);
        btnMyRide = view.findViewById(R.id.btnMyRide);
        btnMyOrders = view.findViewById(R.id.btnMyOrders);
        btnUpcomingOrders = view.findViewById(R.id.btnUpcomingOrders);
        btnUpcomingRides = view.findViewById(R.id.btnUpcomingRides);
    }

    private void onCLickListener() {
        editPersonalDetail.setOnClickListener(this);
        editAddressDetail.setOnClickListener(this);
        imgUserProfile.setOnClickListener(this);
        btnMyRide.setOnClickListener(this);
        btnMyOrders.setOnClickListener(this);
        btnUpcomingOrders.setOnClickListener(this);
        btnUpcomingRides.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);

        if (Connectivity.isConnected(context)) {
            //TODO API Call
            getProfile();
        }
    }

    private void getProfile() {
        viewProgressDialog.showProgress(context);

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
                                .load(AppController.isBaseUrl ? AppController.devURL + AppController.imageURL + response.body().getData().getUserData().getProfileImg() : AppController.prodURL + AppController.imageURL + response.body().getData().getUserData().getProfileImg())
                                .error(R.drawable.ic_logo_sandesh)
                                .into(imgUserProfile);

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
                        String strAddressLine1, strAddressLine2, strLandmark, strState, strCity, strPincode;

                        strAddressLine1 = response.body().getData().getAddress().getAddressLine1();
                        strAddressLine2 = response.body().getData().getAddress().getAddressLine2();
                        strLandmark = response.body().getData().getAddress().getLandmark();
                        strState = response.body().getData().getAddress().getState();
                        strCity = response.body().getData().getAddress().getCity();
                        strPincode = String.valueOf(response.body().getData().getAddress().getPincode());


                        if (strAddressLine1 != null && !strAddressLine1.equals("")) {
                            tvAddresLine1.setText(strAddressLine1);
                        } else {
                            tvAddresLine1.setText("-");
                        }

                        if (strAddressLine2 != null && !strAddressLine2.equals("")) {
                            tvAddresLine2.setText(strAddressLine2);
                        } else {
                            tvAddresLine2.setText("-");
                        }

                        if (strLandmark != null && !strLandmark.equals("")) {
                            tvLandmark.setText(strLandmark);
                        } else {
                            tvLandmark.setText("-");
                        }

                        if (strState != null && !strState.equals("")) {
                            tvState.setText(strState);
                        } else {
                            tvState.setText("-");
                        }

                        if (strCity != null && !strCity.equals("")) {
                            tvCity.setText(strCity);
                        } else {
                            tvCity.setText("-");
                        }

                        if (strPincode != null && !strPincode.equals("")) {
                            if (strPincode.equals("null")) {
                                tvPincode.setText("-");
                            } else {
                                tvPincode.setText(strPincode);
                            }
                        } else {
                            tvPincode.setText("-");
                        }

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

            case R.id.btnMyRide:
                commonRedirect("my_rides", bundle, startJourneyFragment);
                break;

            case R.id.btnMyOrders:
                Uitility.journey = "MyOrdersFromProfile";
                commonRedirect("my_orders", bundle, startJourneyFragment);
                break;

            case R.id.btnUpcomingOrders:
                commonRedirect("upcoming_orders", bundle, orderListFragment);
                break;

            case R.id.btnUpcomingRides:
                commonRedirect("upcoming_rides", bundle, orderListFragment);
                break;
        }
    }

    private void commonRedirect(String tag, Bundle bundle, Fragment fragment) {
        FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                fragment, R.id.frame_container, true);
        bundle.putString("tag", tag);
        fragment.setArguments(bundle);
    }
}
