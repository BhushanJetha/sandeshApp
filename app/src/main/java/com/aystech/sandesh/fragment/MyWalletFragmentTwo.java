package com.aystech.sandesh.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.adapter.MyWalletTabLayoutAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyWalletFragmentTwo extends Fragment {

    Context context;

    TabLayout walletTabLayout;
    ViewPager viewPager;

    public MyWalletFragmentTwo() {
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
        View view = inflater.inflate(R.layout.fragment_my_wallet_fragment_two, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        walletTabLayout = view.findViewById(R.id.walletTabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        walletTabLayout.addTab(walletTabLayout.newTab().setText("Statement"));
        walletTabLayout.addTab(walletTabLayout.newTab().setText("Invoice"));
        walletTabLayout.addTab(walletTabLayout.newTab().setText("Royalty"));

        walletTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        MyWalletTabLayoutAdapter adapter = new MyWalletTabLayoutAdapter(context, ((MainActivity) context).getSupportFragmentManager(),
                walletTabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(walletTabLayout));

        walletTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
