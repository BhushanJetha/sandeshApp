package com.aystech.sandesh.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aystech.sandesh.activity.CorporateRegistrationActivity;
import com.aystech.sandesh.activity.IndividualRegistrationActivity;
import com.aystech.sandesh.activity.LoginActivity;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.R;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.FragmentUtil;

public class LoginFragment extends Fragment {

    Context context;
    DashboardFragment dashboardFragment;

    Button btnLogin;

    public LoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        dashboardFragment = (DashboardFragment) Fragment.instantiate(context,
                DashboardFragment.class.getName());

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        btnLogin = view.findViewById(R.id.btnLogin);

    }

    private void onClickListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = null;
                if(Constants.userType.equals("Individual")){
                    i = new Intent(getActivity(),   IndividualRegistrationActivity.class);
                }else  if(Constants.userType.equals("Corporate")) {
                    i = new Intent(getActivity(), CorporateRegistrationActivity.class);
                }
                startActivity(i);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(false, false,"", false);
    }
}
