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
        bottomBar = findViewById(R.id.bottomBar);
    }

    public void setUpToolbar(boolean isToolBar, String toolbarTitle,
                             boolean isBottomBarShow) {
        if (isToolBar) {
            if (isBottomBarShow) {
                toolBar.setVisibility(View.VISIBLE);
                bottomBar.setVisibility(View.VISIBLE);
            } else {
                toolBar.setVisibility(View.VISIBLE);
                bottomBar.setVisibility(View.GONE);
            }
        } else {
            toolBar.setVisibility(View.GONE);
            bottomBar.setVisibility(View.GONE);
        }
    }
}
