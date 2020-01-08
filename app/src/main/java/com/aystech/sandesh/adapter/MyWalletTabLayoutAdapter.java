package com.aystech.sandesh.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.aystech.sandesh.fragment.InvoiceFragment;
import com.aystech.sandesh.fragment.RoyaltyFragment;
import com.aystech.sandesh.fragment.StatementFragment;

public class MyWalletTabLayoutAdapter extends FragmentPagerAdapter {

    private Context myContext;
    private int totalTabs;

    public MyWalletTabLayoutAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new StatementFragment();
            case 1:
                return new InvoiceFragment();
            case 2:
                return new RoyaltyFragment();
            default:
                return null;
        }
    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
