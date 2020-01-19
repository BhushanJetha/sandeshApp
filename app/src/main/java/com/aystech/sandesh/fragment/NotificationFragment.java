package com.aystech.sandesh.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.adapter.NotificationAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    Context context;

    RecyclerView rvNotificationList;
    NotificationAdapter notificationAdapter;

    public NotificationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        initView(view);

        bindDataToUI();

        return view;
    }

    private void initView(View view) {
        rvNotificationList = view.findViewById(R.id.rvNotification);
    }

    private void bindDataToUI() {
        notificationAdapter=new NotificationAdapter();
        rvNotificationList.setAdapter(notificationAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
