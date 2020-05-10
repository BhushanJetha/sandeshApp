package com.aystech.sandesh.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.AddressDetailActivity;
import com.aystech.sandesh.activity.LoginActivity;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.LoginResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.JWTUtils;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.UserSession;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    Context context;

    Button btnNext;

    private ImageView imgKYC, imgMyProfile, imgSearchOrders, imgSearchTravelers, imgMyWallet, imgPlanTravel,
            imgSendParcel, imgTrackYourParcel, imgStartJourney, imgEndJourney, imgComplaintDispute, imgTermsAndCondition,
            imgMyRequestedOrders;

    private SearchOrderFragment searchOrderFragment;
    private MyWalletFragmentOne myWalletFragment;
    private PlanTravelFragment planTravelFragment;
    private SearchTravelerFragment searchTravelerFragment;
    private SendParcelFragment sendParcelFragment;
    private StartJourneyFragment startJourneyFragment;
    private EndJourneyFragment endJourneyFragment;
    private ComplaintDisputeFragment complaintDisputeFragment;
    private TermsAndConditionFragment termsAndConditionFragment;
    private UserProfileFragment userProfileFragment;
    private CompanyProfileFragment companyProfileFragment;
    private MyRequestedOrderFragments myRequestedOrderFragments;

    ViewProgressDialog viewProgressDialog;

    UserSession userSession;

    public DashboardFragment() {
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
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        searchOrderFragment = (SearchOrderFragment)
                Fragment.instantiate(context, SearchOrderFragment.class.getName());
        myWalletFragment = (MyWalletFragmentOne)
                Fragment.instantiate(context, MyWalletFragmentOne.class.getName());
        planTravelFragment = (PlanTravelFragment)
                Fragment.instantiate(context, PlanTravelFragment.class.getName());
        searchTravelerFragment = (SearchTravelerFragment)
                Fragment.instantiate(context, SearchTravelerFragment.class.getName());
        sendParcelFragment = (SendParcelFragment)
                Fragment.instantiate(context, SendParcelFragment.class.getName());
        startJourneyFragment = (StartJourneyFragment)
                Fragment.instantiate(context, StartJourneyFragment.class.getName());
        endJourneyFragment = (EndJourneyFragment)
                Fragment.instantiate(context, EndJourneyFragment.class.getName());
        complaintDisputeFragment = (ComplaintDisputeFragment)
                Fragment.instantiate(context, ComplaintDisputeFragment.class.getName());
        termsAndConditionFragment = (TermsAndConditionFragment)
                Fragment.instantiate(context, TermsAndConditionFragment.class.getName());
        userProfileFragment = (UserProfileFragment)
                Fragment.instantiate(context, UserProfileFragment.class.getName());
        companyProfileFragment = (CompanyProfileFragment)
                Fragment.instantiate(context, CompanyProfileFragment.class.getName());
        myRequestedOrderFragments = (MyRequestedOrderFragments) Fragment.instantiate(context,
                MyRequestedOrderFragments.class.getName());

        initView(view);

        onClickListener();

        openResetPasswordDialog();

        return view;
    }

    private void initView(View view) {
        userSession = new UserSession(context);
        viewProgressDialog = ViewProgressDialog.getInstance();

        btnNext = view.findViewById(R.id.btnNext);

        imgKYC = view.findViewById(R.id.imgKYC);
        imgMyProfile = view.findViewById(R.id.imgProfile);
        imgSearchOrders = view.findViewById(R.id.imgSendersOrders);
        imgSearchTravelers = view.findViewById(R.id.imgSearchTravellers);
        imgMyWallet = view.findViewById(R.id.imgWallet);
        imgPlanTravel = view.findViewById(R.id.imgPlanTravel);
        imgSendParcel = view.findViewById(R.id.imgSendParcel);
        imgTrackYourParcel = view.findViewById(R.id.imgTrackParcel);
        imgStartJourney = view.findViewById(R.id.imgStartJourney);
        imgEndJourney = view.findViewById(R.id.imgEndJourney);
        imgComplaintDispute = view.findViewById(R.id.imgComplaintDispute);
        imgTermsAndCondition = view.findViewById(R.id.imgTC);
        imgMyRequestedOrders = view.findViewById(R.id.imgMyRequestedOrders);

        String isFirstTimeUser = userSession.getFirstTimeUserStatus();
        if(isFirstTimeUser.equals("Yes")){
            openWelcomeUserDialog();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        userSession.setPreviousOnlineDateTime(currentDate);
    }

    private void onClickListener() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        searchOrderFragment, R.id.frame_container, true);
            }
        });

        imgSearchOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        searchOrderFragment, R.id.frame_container, true);
            }
        });

        imgSearchTravelers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        searchTravelerFragment, R.id.frame_container, true);
            }
        });

        imgMyWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        myWalletFragment, R.id.frame_container, true);
            }
        });

        imgPlanTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        planTravelFragment, R.id.frame_container, true);
            }
        });

        imgSendParcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        sendParcelFragment, R.id.frame_container, true);
            }
        });

        imgTrackYourParcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        startJourneyFragment, R.id.frame_container, true);
                Bundle bundle = new Bundle();
                bundle.putString("tag","track_parcel");
                startJourneyFragment.setArguments(bundle);
            }
        });

        imgStartJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        startJourneyFragment, R.id.frame_container, true);
            }
        });

        imgEndJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        endJourneyFragment, R.id.frame_container, true);
            }
        });

        imgComplaintDispute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        complaintDisputeFragment, R.id.frame_container, true);
            }
        });

        imgTermsAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        termsAndConditionFragment, R.id.frame_container, true);
            }
        });

        imgMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userSession.getUSER_TYPE().equals("individual")) {
                    FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                            userProfileFragment, R.id.frame_container, true);
                } else {
                    FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                            companyProfileFragment, R.id.frame_container, true);
                }
            }
        });

        imgMyRequestedOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        myRequestedOrderFragments, R.id.frame_container, true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, true, "", true);
    }

    private void openWelcomeUserDialog() {
        userSession.setFirstTimeUserStatus("No");
        new AlertDialog.Builder(getActivity())
                .setTitle("Sandesh")
                .setMessage("Welcome in Sandesh application!")
                .setCancelable(true)
                .setPositiveButton("close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                       userSession.setFirstTimeUserStatus("No");

                    }
                })

                .show();
    }


    //Reset Password
    private void openResetPasswordDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.reset_password_dialog, null);

        final EditText etOldPassword = alertLayout.findViewById(R.id.et_OldPassword);
        final EditText etNewPassword = alertLayout.findViewById(R.id.et_NewPassword);
        final EditText etReEnteredPassword = alertLayout.findViewById(R.id.et_ReEnterNewPassword);
        Button btnResetPassword = alertLayout.findViewById(R.id.btnResetPassword);

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final AlertDialog dialog = alert.create();
        dialog.show();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = etOldPassword.getText().toString();
                String newPasssword = etNewPassword.getText().toString();
                String reEnterPassword = etReEnteredPassword.getText().toString();

                if(!oldPassword.isEmpty()){
                    if(!newPasssword.isEmpty()){
                        if(!reEnterPassword.isEmpty()){
                            if(newPasssword.equals(reEnterPassword)){
                                //TODO API Call
                                doResetPasswordAPICall(oldPassword, newPasssword);

                                dialog.dismiss();
                            }else {
                                Uitility.showToast(getActivity(), "New password and re-entered password are not matching !");
                            }
                        }else {
                            Uitility.showToast(getActivity(), "Please re-enter your password !");
                        }
                    }else {
                        Uitility.showToast(getActivity(), "Please enter new password !");
                    }
                }else {
                    Uitility.showToast(getActivity(), "Please enter OTP !");
                }


            }
        });
    }


    //Reset Password API call
    private void doResetPasswordAPICall(String oldPassword,String newPassword) {
        ViewProgressDialog.getInstance().showProgress(getActivity());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("oldPassword", oldPassword);
        jsonObject.addProperty("newPassword", newPassword);


        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.resetPassword(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        //Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } //else
                        //Toast.makeText(LoginActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }
}
