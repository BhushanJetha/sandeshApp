package com.aystech.sandesh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.model.NotificationListModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context context;
    private List<NotificationListModel> notificationListModels;

    public NotificationAdapter(Context context, List<NotificationListModel> notificationListModels) {
        this.context = context;
        this.notificationListModels = notificationListModels;
    }

    public NotificationAdapter() {

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_notification,
                parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvNotificationMsg.setText("Notification " + (i + 1));
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvNotificationMsg;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNotificationMsg = itemView.findViewById(R.id.tvNotificationMsg);
        }
    }
}
