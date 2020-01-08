package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.R;
import com.aystech.sandesh.utils.FragmentUtil;

public class MobileVerificationFragment extends Fragment {

    Context context;
    DashboardFragment dashboardFragment;

    Button btnSubmit;

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

        dashboardFragment = (DashboardFragment) Fragment.instantiate(context, DashboardFragment.class.getName());

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        btnSubmit = view.findViewById(R.id.btnSubmit);
    }

    private void onClickListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        dashboardFragment, R.id.frame_container, true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(false, "", false);
    }
}
