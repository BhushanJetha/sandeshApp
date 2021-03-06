package com.aystech.sandesh.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {

    Context context;

    public ContactUsFragment() {
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
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
