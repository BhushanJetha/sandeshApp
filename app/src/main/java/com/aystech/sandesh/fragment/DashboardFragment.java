package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.UserSession;

public class DashboardFragment extends Fragment {

    Context context;

    Button btnNext;

    private ImageView imgKYC, imgMyProfile, imgSearchOrders, imgSearchTravelers, imgMyWallet, imgPlanTravel,
            imgSendParcel, imgTrackYourParcel, imgStartJourney, imgEndJourney, imgComplaintDispute, imgTermsAndCondition;

    private SearchOrderFragment searchOrderFragment;
    private MyWalletFragmentOne myWalletFragment;
    private PlanTravelFragment planTravelFragment;
    private SearchTravelerFragment searchTravelerFragment;
    private SendParcelFragment sendParcelFragment;
    private TrackYourParcelFragment trackYourParcelFragment;
    private StartJourneyFragment startJourneyFragment;
    private EndJourneyFragment endJourneyFragment;
    private ComplaintDisputeFragment complaintDisputeFragment;
    private TermsAndConditionFragment termsAndConditionFragment;
    private UserProfileFragment userProfileFragment;
    private CompanyProfileFragment companyProfileFragment;

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
        trackYourParcelFragment = (TrackYourParcelFragment)
                Fragment.instantiate(context, TrackYourParcelFragment.class.getName());
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

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        userSession = new UserSession(context);

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
                        trackYourParcelFragment, R.id.frame_container, true);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, true, "", true);
    }
}
