package com.aystech.sandesh.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.adapter.NoDataAdapter;
import com.aystech.sandesh.adapter.NotificationAdapter;
import com.aystech.sandesh.model.NotificationModel;
import com.aystech.sandesh.model.NotificationResponseModel;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.Connectivity;
import com.aystech.sandesh.utils.ViewProgressDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    Context context;

    RecyclerView rvNotificationList;
    NotificationAdapter notificationAdapter;
    private ViewProgressDialog viewProgressDialog;

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

        if (Connectivity.isConnected(context)) {
            //TODO API Call
            getNotifications();
        }

        return view;
    }

    private void getNotifications() {
        viewProgressDialog.showProgress(context);

        RetrofitInstance.getClient().getNotifications().enqueue(new Callback<NotificationResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<NotificationResponseModel> call, @NonNull Response<NotificationResponseModel> response) {

                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        bindDataToUI(response.body().getData());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NotificationResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        rvNotificationList = view.findViewById(R.id.rvNotification);
    }

    private void bindDataToUI(List<NotificationModel> data) {
        if (data.size() > 0) {
            notificationAdapter = new NotificationAdapter(data);
            rvNotificationList.setAdapter(notificationAdapter);
        } else {
            NoDataAdapter noDataAdapter = new NoDataAdapter(context, "No Notification Found!");
            rvNotificationList.setAdapter(noDataAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
