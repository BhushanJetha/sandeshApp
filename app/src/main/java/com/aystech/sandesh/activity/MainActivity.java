package com.aystech.sandesh.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.fragment.ContactUsFragment;
import com.aystech.sandesh.fragment.DashboardFragment;
import com.aystech.sandesh.fragment.FAQFragment;
import com.aystech.sandesh.fragment.HistoryFragment;
import com.aystech.sandesh.fragment.MobileVerificationFragment;
import com.aystech.sandesh.fragment.NotificationFragment;
import com.aystech.sandesh.fragment.ReferFriendFragment;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.FragmentUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgNotification, imgOtherNotification, imgLogout, imgOtherLogout, imgOtherHome;
    Toolbar toolBar;
    ConstraintLayout clDashboard, clOther;
    ConstraintLayout bottomBar;
    Group historyGroup, contactUsGroup, faqGroup, referFriendGroup;
    ImageView imgHistory, imgContactUs, imgFAQ, imgReferFriend, imgNominatePerson;
    TextView tvHistory, tvContactUs, tvFAQ, tvReferFriend, tvNominatePerson;

    MobileVerificationFragment mobileVerificationFragment;
    DashboardFragment dashboardFragment;
    FAQFragment faqFragment;
    NotificationFragment notificationFragment;
    HistoryFragment historyFragment;
    ContactUsFragment contactUsFragment;
    ReferFriendFragment referFriendFragment;

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        mobileVerificationFragment = (MobileVerificationFragment) Fragment.instantiate(this, MobileVerificationFragment.class.getName());
        dashboardFragment = (DashboardFragment) Fragment.instantiate(this, DashboardFragment.class.getName());
        faqFragment = (FAQFragment) Fragment.instantiate(this, FAQFragment.class.getName());
        notificationFragment = (NotificationFragment) Fragment.instantiate(this, NotificationFragment.class.getName());
        historyFragment = (HistoryFragment) Fragment.instantiate(this, HistoryFragment.class.getName());
        contactUsFragment = (ContactUsFragment) Fragment.instantiate(this, ContactUsFragment.class.getName());
        referFriendFragment = (ReferFriendFragment) Fragment.instantiate(this, ReferFriendFragment.class.getName());

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
        imgNotification = findViewById(R.id.imgNotification);
        imgOtherNotification = findViewById(R.id.imgOtherNotification);
        imgLogout = findViewById(R.id.imgLogout);
        imgOtherLogout = findViewById(R.id.imgOtherLogout);
        imgOtherHome = findViewById(R.id.imgOtherHome);

        clDashboard = findViewById(R.id.clDashboard);
        clOther = findViewById(R.id.clOther);
        bottomBar = findViewById(R.id.bottomBar);

        historyGroup = findViewById(R.id.historyGroup);
        imgHistory = findViewById(R.id.imgHistory);
        tvHistory = findViewById(R.id.tvHistory);
        contactUsGroup = findViewById(R.id.contactUsGroup);
        imgContactUs = findViewById(R.id.imgContactUs);
        tvContactUs = findViewById(R.id.tvContactUs);
        faqGroup = findViewById(R.id.faqGroup);
        imgFAQ = findViewById(R.id.imgFAQ);
        tvFAQ = findViewById(R.id.tvFAQ);
        referFriendGroup = findViewById(R.id.referFriendGroup);
        imgReferFriend = findViewById(R.id.imgReferFriend);
        tvReferFriend = findViewById(R.id.tvReferFriend);
    }

    private void onClickListener() {
        imgNotification.setOnClickListener(this);
        imgOtherNotification.setOnClickListener(this);
        imgLogout.setOnClickListener(this);
        imgOtherLogout.setOnClickListener(this);
        imgOtherHome.setOnClickListener(this);

        historyGroup.setOnClickListener(this);
        imgHistory.setOnClickListener(this);
        tvHistory.setOnClickListener(this);
        contactUsGroup.setOnClickListener(this);
        imgContactUs.setOnClickListener(this);
        tvContactUs.setOnClickListener(this);
        faqGroup.setOnClickListener(this);
        imgFAQ.setOnClickListener(this);
        tvFAQ.setOnClickListener(this);
        referFriendGroup.setOnClickListener(this);
        imgReferFriend.setOnClickListener(this);
        tvReferFriend.setOnClickListener(this);
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
            case R.id.imgNotification:
            case R.id.imgOtherNotification:
                FragmentUtil.commonMethodForFragment(getSupportFragmentManager(), notificationFragment, R.id.frame_container,
                        true);
                break;

            case R.id.imgLogout:
            case R.id.imgOtherLogout:
                openLogoutDialog();
                break;

            case R.id.imgOtherHome:
                FragmentUtil.commonMethodForFragment(getSupportFragmentManager(), dashboardFragment, R.id.frame_container,
                        false);
                break;

            case R.id.historyGroup:
            case R.id.imgHistory:
            case R.id.tvHistory:
                FragmentUtil.commonMethodForFragment(getSupportFragmentManager(), historyFragment, R.id.frame_container,
                        true);
                break;

            case R.id.contactUsGroup:
            case R.id.imgContactUs:
            case R.id.tvContactUs:
                FragmentUtil.commonMethodForFragment(getSupportFragmentManager(), contactUsFragment, R.id.frame_container,
                        true);
                break;

            case R.id.faqGroup:
            case R.id.imgFAQ:
            case R.id.tvFAQ:
                FragmentUtil.commonMethodForFragment(getSupportFragmentManager(), faqFragment, R.id.frame_container,
                        true);
                break;

            case R.id.referFriendGroup:
            case R.id.imgReferFriend:
            case R.id.tvReferFriend:
                FragmentUtil.commonMethodForFragment(getSupportFragmentManager(), referFriendFragment, R.id.frame_container,
                        true);
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

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        counter++;
        boolean isBack = true;
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (f != null) {
            if ("DashboardFragment".equals(f.getClass().getSimpleName())) {
                isBack = false;
                if (counter == 1) {
                    Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show();
                } else if (counter > 1) {
                    counter = 0;
                    finish();
                }
            } else {
                counter--;
            }
        }
        if (isBack) {
            super.onBackPressed();
        }
    }
}
