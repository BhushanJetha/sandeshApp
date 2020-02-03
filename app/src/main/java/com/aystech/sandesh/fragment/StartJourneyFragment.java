package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;

public class StartJourneyFragment extends Fragment implements View.OnClickListener {

    Context context;

    Group grParcelPhoto;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_journey, container, false);

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        grParcelPhoto = view.findViewById(R.id.grParcelPhoto);
    }

    private void onClickListener() {
        grParcelPhoto.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.grParcelPhoto:

                break;
        }
    }
}
