package com.aystech.sandesh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.model.NotificationModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context context;
    private List<NotificationModel> notificationResponseModels;

    public NotificationAdapter(List<NotificationModel> data) {
        this.context = context;
        this.notificationResponseModels = data;
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
        myViewHolder.tvNotificationTitle.setText(notificationResponseModels.get(i).getTitle());
        myViewHolder.tvNotificationMsg.setText(notificationResponseModels.get(i).getBody());
    }

    @Override
    public int getItemCount() {
        return notificationResponseModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvNotificationTitle;
        TextView tvNotificationMsg;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNotificationTitle = itemView.findViewById(R.id.tvNotificationTitle);
            tvNotificationMsg = itemView.findViewById(R.id.tvNotificationMsg);
        }
    }
}
