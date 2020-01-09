package com.aystech.sandesh.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aystech.sandesh.R;
import com.aystech.sandesh.fragment.LoginFragment;
import com.aystech.sandesh.utils.FragmentUtil;

public class MainActivity extends AppCompatActivity {

    Toolbar toolBar;
    ConstraintLayout clDashboard, clOther;
    ConstraintLayout bottomBar;

    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        loginFragment = (LoginFragment) Fragment.instantiate(this, LoginFragment.class.getName());

        FragmentUtil.commonMethodForFragment(getSupportFragmentManager(), loginFragment, R.id.frame_container,
                false);
    }

    private void initView() {
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        clDashboard = findViewById(R.id.clDashboard);
        clOther = findViewById(R.id.clOther);
        bottomBar = findViewById(R.id.bottomBar);
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
}
