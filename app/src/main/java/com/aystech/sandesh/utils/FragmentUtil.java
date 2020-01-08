package com.aystech.sandesh.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtil {

    public static void commonMethodForFragment(FragmentManager myFragmentManager, Fragment fragment,
                                               int frame_container, boolean addToBackStack) {

        String backStateName = fragment.getClass().getName();
        boolean fragmentPopped = myFragmentManager.popBackStackImmediate(backStateName, 0);

        FragmentTransaction myFragmentTransaction = myFragmentManager.beginTransaction();

        if (!fragmentPopped && myFragmentManager.findFragmentByTag(backStateName) == null) {
            if (addToBackStack) {
                //fragment not in back stack, create it.
                myFragmentTransaction.replace(frame_container, fragment, backStateName);
                myFragmentTransaction.addToBackStack(backStateName);
                myFragmentTransaction.commitAllowingStateLoss();
            } else {
                myFragmentTransaction.replace(frame_container, fragment, backStateName);
                myFragmentTransaction.commitAllowingStateLoss();
            }
        } else {
            myFragmentTransaction.replace(frame_container, fragment);
            myFragmentTransaction.addToBackStack(null);
            myFragmentTransaction.commitAllowingStateLoss();
        }
    }
}
