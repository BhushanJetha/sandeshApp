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
import com.aystech.sandesh.model.CorporateModel;
import com.aystech.sandesh.model.ProfileResponseModel;
import com.aystech.sandesh.model.UserModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.AppController;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyProfileFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private UpdateCompanyProfileFragment updateCompanyProfileFragment;
    private UpdateAddressFragment updateAddressFragment;
    private StartJourneyFragment startJourneyFragment;
    private OrderListFragment orderListFragment;

    private UserModel userModel;
    private CorporateModel corporateModel;
    private AddressModel addressModel;

    private ImageView imgCompanyProfile;
    private TextView tvCompanyName, tvCompany, tvBranch, tvAuthorisedName, tvDesignation, tvMobileNumber, tvEmailId,
            tvAddresLine1, tvAddresLine2, tvLandmark, tvState, tvCity,tvPincode;
    private Button btnMyRide, btnMyOrders, btnUpcomingRides, btnUpcomingOrders;
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

        updateCompanyProfileFragment = (UpdateCompanyProfileFragment)
                Fragment.instantiate(context, UpdateCompanyProfileFragment.class.getName());
        updateAddressFragment = (UpdateAddressFragment)
                Fragment.instantiate(context, UpdateAddressFragment.class.getName());
        startJourneyFragment = (StartJourneyFragment)
                Fragment.instantiate(context, StartJourneyFragment.class.getName());
        orderListFragment = (OrderListFragment) Fragment.instantiate(context,
                OrderListFragment.class.getName());

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
        tvPincode = view.findViewById(R.id.tvPincode);
        btnMyRide = view.findViewById(R.id.btnMyRide);
        btnMyOrders = view.findViewById(R.id.btnMyOrders);
        btnUpcomingOrders = view.findViewById(R.id.btnUpcomingOrders);
        btnUpcomingRides = view.findViewById(R.id.btnUpcomingRides);
        editCompanyDetail = view.findViewById(R.id.editCompanyDetail);
        editAddressDetail = view.findViewById(R.id.editAddressDetail);
    }

    private void onClickListener() {
        editCompanyDetail.setOnClickListener(this);
        editAddressDetail.setOnClickListener(this);
        btnMyRide.setOnClickListener(this);
        btnMyOrders.setOnClickListener(this);
        btnUpcomingOrders.setOnClickListener(this);
        btnUpcomingRides.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.editCompanyDetail:
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        updateCompanyProfileFragment, R.id.frame_container, true);
                bundle.putParcelable("userModel", userModel);
                bundle.putParcelable("corporateModel", corporateModel);
                updateCompanyProfileFragment.setArguments(bundle);
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
                        corporateModel = response.body().getData().getCorporateData();
                        addressModel = response.body().getData().getAddress();

                        //this is for profile
                        Glide.with(context)
                                .load(AppController.imageURL + response.body().getData().getUserData().getProfileImg())
                                .error(R.drawable.ic_logo_sandesh)
                                .into(imgCompanyProfile);

                        tvEmailId.setText(response.body().getData().getUserData().getEmailId());
                        tvMobileNumber.setText(response.body().getData().getUserData().getMobileNo());

                        //this is for company detail
                        tvCompany.setText(response.body().getData().getCorporateData().getCompanyName());
                        tvCompanyName.setText(response.body().getData().getCorporateData().getCompanyName());
                        tvBranch.setText(response.body().getData().getCorporateData().getBranch());
                        tvAuthorisedName.setText(response.body().getData().getCorporateData().getAuthPersonName());
                        tvDesignation.setText(response.body().getData().getCorporateData().getDesignation());

                        //this is for address
                        String strAddressLine1, strAddressLine2, strLandmark, strState, strCity, strPincode;

                        strAddressLine1 = response.body().getData().getAddress().getAddressLine1();
                        strAddressLine2 = response.body().getData().getAddress().getAddressLine2();
                        strLandmark = response.body().getData().getAddress().getLandmark();
                        strState = response.body().getData().getAddress().getState();
                        strCity = response.body().getData().getAddress().getCity();
                        strPincode = String.valueOf(response.body().getData().getAddress().getPincode());


                        if(strAddressLine1 != null && !strAddressLine1.equals("")){
                            tvAddresLine1.setText(strAddressLine1);
                        }else {
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
                            if(strPincode.equals("null")){
                                tvPincode.setText("-");
                            }else {
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

    private void commonRedirect(String tag, Bundle bundle, Fragment fragment) {
        FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                fragment, R.id.frame_container, true);
        bundle.putString("tag", tag);
        fragment.setArguments(bundle);
    }
}
