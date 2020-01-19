package com.aystech.sandesh.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.fragment.DashboardFragment;
import com.aystech.sandesh.fragment.LoginFragment;
import com.aystech.sandesh.fragment.MobileVerificationFragment;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.FragmentUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgLogout;
    Toolbar toolBar;
    ConstraintLayout clDashboard, clOther;
    ConstraintLayout bottomBar;

    MobileVerificationFragment mobileVerificationFragment;
    DashboardFragment dashboardFragment;
    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        mobileVerificationFragment = (MobileVerificationFragment) Fragment.instantiate(this, MobileVerificationFragment.class.getName());
        dashboardFragment = (DashboardFragment) Fragment.instantiate(this, DashboardFragment.class.getName());
        loginFragment = (LoginFragment) Fragment.instantiate(this, LoginFragment.class.getName());

        if (Constants.fragmentType.equals("Dashboard")) {
            FragmentUtil.commonMethodForFragment(getSupportFragmentManager(), dashboardFragment, R.id.frame_container,
                    false);
        } else {
            FragmentUtil.commonMethodForFragment(getSupportFragmentManager(), mobileVerificationFragment, R.id.frame_container,
                    false);
        }

        onClickListener();
    }

    private void initView() {
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        imgLogout = findViewById(R.id.imgLogout);

        clDashboard = findViewById(R.id.clDashboard);
        clOther = findViewById(R.id.clOther);
        bottomBar = findViewById(R.id.bottomBar);
    }

    private void onClickListener() {
        imgLogout.setOnClickListener(this);
    }

    public void setUpToolbar(boolean isToolBar, boolean isDashboard, String toolbarTitle,
                             boolean isBottomBarShow) {
        if (isToolBar) {
            if (isBottomBarShow && isDashboard) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(false);

                toolBar.setVisibility(View.VISIBLE);
                clDashboard.setVisibility(View.VISIBLE);
                clOther.setVisibility(View.GONE);
                bottomBar.setVisibility(View.VISIBLE);
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

                toolBar.setVisibility(View.VISIBLE);
                clDashboard.setVisibility(View.GONE);
                clOther.setVisibility(View.VISIBLE);
                bottomBar.setVisibility(View.GONE);
            }
        } else {
            toolBar.setVisibility(View.GONE);
            bottomBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgLogout:
                openLogoutDialog();
                break;
        }
    }

    private void openLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Do you want logout?")
                .setMessage("Are you sure, do you want to logout?")
                .setCancelable(true)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        FragmentUtil.commonMethodForFragment(getSupportFragmentManager(), loginFragment, R.id.frame_container,
                                false);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
