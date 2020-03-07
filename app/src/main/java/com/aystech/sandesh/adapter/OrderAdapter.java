package com.aystech.sandesh.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.interfaces.OnItemClickListener;
import com.aystech.sandesh.model.SearchOrderModel;
import com.aystech.sandesh.model.SearchTravellerModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private Context context;
    private List<SearchOrderModel> searchOrderModels;
    private List<SearchTravellerModel> searchTravellerModels;
    private String tag;
    private OnItemClickListener onItemClickListener;

    //this form SearchOrderFragment & SearchTravellerFragment for order/traveller section
    public OrderAdapter(Context context, String tag, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.tag = tag;
        this.onItemClickListener = onItemClickListener;
    }

    //this form HistoryFragment for traveller section
    public OrderAdapter(Context context, List<SearchTravellerModel> data, String tag) {
        this.context = context;
        this.searchTravellerModels = data;
        this.tag = tag;
    }

    //this form HistoryFragment for parcel section
    public OrderAdapter(Context context, List<SearchOrderModel> parcel) {
        this.context = context;
        this.searchOrderModels = parcel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.row_order_list, viewGroup, false);
        return new MyViewHolder(contentView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        if (tag != null && tag.equals("traveller")) {
            myViewHolder.tvName.setText("" /*+ searchTravellerModels.get(i).getReceiverName()*/);
            myViewHolder.tvOrderDate.setText("" + searchTravellerModels.get(i).getStartDate());
            myViewHolder.tvOrderType.setText("" + searchTravellerModels.get(i).getDeliveryOption());
            myViewHolder.tvOrderDistance.setText("" + searchTravellerModels.get(i).getPreferredWeight());
            myViewHolder.tvOrderTypeContent.setText("" + searchTravellerModels.get(i).getModeOfTravel());
            myViewHolder.tvExpectedIncome.setText("Expected income: Rs. " + searchTravellerModels.get(i).getEstimate_amount());
            myViewHolder.tvVolumeInfo.setVisibility(View.VISIBLE);
            myViewHolder.tvVolumeInfo.setText("Vol: " + searchTravellerModels.get(i).getAcceptableVolumeLength() + " * " +
                    searchTravellerModels.get(i).getAcceptableVolumeBreadth() +" * "+ searchTravellerModels.get(i).getAcceptableVolumeWidth());

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(searchTravellerModels.get(i));
                }
            });

        } else if (tag != null && tag.equals("order")) {
            myViewHolder.tvName.setText("" + searchOrderModels.get(i).getFirstName());
            myViewHolder.tvOrderDate.setText("" + searchOrderModels.get(i).getStartDate());
            myViewHolder.tvOrderType.setText("" + searchOrderModels.get(i).getDeliveryOption());
            myViewHolder.tvOrderDistance.setText("" + searchOrderModels.get(i).getWeight());
            myViewHolder.tvOrderTypeContent.setText("" + searchOrderModels.get(i).getNatureOfGoods());
            myViewHolder.tvExpectedIncome.setText("Expected income: Rs. " + searchOrderModels.get(i).getEstimate_amount());

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(searchOrderModels.get(i));
                }
            });
        } else if (tag != null && tag.equals("order_clicked_verify")) {
            myViewHolder.tvName.setText("" + searchOrderModels.get(i).getFirstName());
            myViewHolder.tvOrderDate.setText("" + searchOrderModels.get(i).getStartDate());
            myViewHolder.tvOrderType.setText("" + searchOrderModels.get(i).getDeliveryOption());
            myViewHolder.tvOrderDistance.setText("" + searchOrderModels.get(i).getWeight());
            myViewHolder.tvOrderTypeContent.setText("" + searchOrderModels.get(i).getNatureOfGoods());
            myViewHolder.tvExpectedIncome.setText("Expected income: Rs. " + searchOrderModels.get(i).getEstimate_amount());

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.openOtpDialog();
                }
            });
        } else {
            myViewHolder.tvName.setText("" + searchOrderModels.get(i).getFirstName());
            myViewHolder.tvOrderDate.setText("" + searchOrderModels.get(i).getStartDate());
            myViewHolder.tvOrderType.setText("" + searchOrderModels.get(i).getDeliveryOption());
            myViewHolder.tvOrderDistance.setText("" + searchOrderModels.get(i).getWeight());
            myViewHolder.tvOrderTypeContent.setText("" + searchOrderModels.get(i).getNatureOfGoods());
            myViewHolder.tvExpectedIncome.setText("Expected income: Rs. " + searchOrderModels.get(i).getEstimate_amount());
        }
    }

    @Override
    public int getItemCount() {
        if (tag != null && tag.equals("traveller")) {
            return searchTravellerModels.size();
        }
        return searchOrderModels.size();
    }

    public void addTravellerList(List<SearchTravellerModel> data) {
        this.searchTravellerModels = data;
    }

    public void addOrderList(List<SearchOrderModel> data) {
        this.searchOrderModels = data;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvOrderDate;
        private TextView tvOrderType;
        private TextView tvOrderDistance;
        private TextView tvOrderTypeContent;
        private TextView tvExpectedIncome;
        private TextView tvVolumeInfo;
        private RatingBar rbOrder;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderType = itemView.findViewById(R.id.tvOrderType);
            tvOrderDistance = itemView.findViewById(R.id.tvOrderDistance);
            tvOrderTypeContent = itemView.findViewById(R.id.tvOrderTypeContent);
            tvExpectedIncome = itemView.findViewById(R.id.tvExpectedIncome);
            tvVolumeInfo = itemView.findViewById(R.id.tvVolumeInfo);
            rbOrder = itemView.findViewById(R.id.rbOrder);
        }
    }
}
