package com.aystech.sandesh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.model.SearchOrderModel;
import com.aystech.sandesh.model.SearchTravellerModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private Context context;
    private List<SearchOrderModel> searchOrderModels;
    private List<SearchTravellerModel> searchTravellerModels;
    private String tag;

    public OrderAdapter(Context context, List<SearchOrderModel> data) {
        this.context = context;
        this.searchOrderModels = data;
    }

    public OrderAdapter(Context context, List<SearchTravellerModel> data, String tag) {
        this.context = context;
        this.searchTravellerModels = data;
        this.tag = tag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.row_order_list, viewGroup, false);
        return new MyViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (tag != null && tag.equals("traveller")) {
            myViewHolder.tvName.setText("" /*+ searchTravellerModels.get(i).getReceiverName()*/);
            myViewHolder.tvOrderDate.setText("" + searchTravellerModels.get(i).getStartDate());
            myViewHolder.tvOrderType.setText("" + searchTravellerModels.get(i).getDeliveryOption());
            myViewHolder.tvOrderDistance.setText("" + searchTravellerModels.get(i).getPreferredWeight());
            myViewHolder.tvOrderTypeContent.setText("" + searchTravellerModels.get(i).getModeOfTravel());
        } else {
            myViewHolder.tvName.setText("" + searchOrderModels.get(i).getReceiverName());
            myViewHolder.tvOrderDate.setText("" + searchOrderModels.get(i).getStartDate());
            myViewHolder.tvOrderType.setText("" + searchOrderModels.get(i).getDeliveryOption());
            myViewHolder.tvOrderDistance.setText("" + searchOrderModels.get(i).getWeight());
            myViewHolder.tvOrderTypeContent.setText("" + searchOrderModels.get(i).getNatureOfGoods());
        }
    }

    @Override
    public int getItemCount() {
        if (tag != null && tag.equals("traveller")) {
            return searchTravellerModels.size();
        }
        return searchOrderModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvOrderDate;
        private TextView tvOrderType;
        private TextView tvOrderDistance;
        private TextView tvOrderTypeContent;
        private TextView tvExpectedIncome;
        private RatingBar rbOrder;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderType = itemView.findViewById(R.id.tvOrderType);
            tvOrderDistance = itemView.findViewById(R.id.tvOrderDistance);
            tvOrderTypeContent = itemView.findViewById(R.id.tvOrderTypeContent);
            tvExpectedIncome = itemView.findViewById(R.id.tvExpectedIncome);
            rbOrder = itemView.findViewById(R.id.rbOrder);
        }
    }
}
