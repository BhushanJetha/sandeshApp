package com.aystech.sandesh.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aystech.sandesh.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanTravelFragment extends Fragment {

    Context context;

    public PlanTravelFragment() {
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
        View view = inflater.inflate(R.layout.fragment_plan_travel, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {

    }

}
