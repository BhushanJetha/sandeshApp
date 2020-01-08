package com.aystech.sandesh.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.aystech.sandesh.R;
import com.aystech.sandesh.fragment.LoginFragment;
import com.aystech.sandesh.utils.FragmentUtil;

public class MainActivity extends AppCompatActivity {

	LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		loginFragment = (LoginFragment) Fragment.instantiate(this, LoginFragment.class.getName());

        FragmentUtil.commonMethodForFragment(getSupportFragmentManager(), loginFragment, R.id.frame_container,
                false);
    }
}
