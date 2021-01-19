package com.aystech.sandesh.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.interfaces.OnItemClickListener;
import com.aystech.sandesh.model.AcceptedOrdersModel;
import com.aystech.sandesh.model.SearchOrderModel;
import com.aystech.sandesh.model.SearchTravellerModel;
import com.aystech.sandesh.utils.Uitility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    private final Context context;
    private List<SearchOrderModel> searchOrderModels;
    private List<SearchTravellerModel> searchTravellerModels;
    private List<AcceptedOrdersModel> acceptedOrdersModels;
    private final String tag;
    private final OnItemClickListener onItemClickListener;

    //this form SearchOrderFragment, SearchTravellerFragment, & HistoryFragment for order/traveller section
    public OrderAdapter(Context context, String tag, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.tag = tag;
        this.onItemClickListener = onItemClickListener;
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
        Log.d("Tell me Tag Name-->", tag);
        if (tag != null && tag.equals("traveller")) {
            if (searchTravellerModels.get(i).getFull_name() != null &&
                    !searchTravellerModels.get(i).getFull_name().equals(""))
                myViewHolder.tvName.setText("" + searchTravellerModels.get(i).getFull_name());
            else
                myViewHolder.tvName.setText("-");

            if ((searchTravellerModels.get(i).getStartDate() != null &&
                    !searchTravellerModels.get(i).getStartDate().equals("")) &&
                    (searchTravellerModels.get(i).getEndDate() != null &&
                            !searchTravellerModels.get(i).getEndDate().equals("")))
                myViewHolder.tvOrderDate.setText("" + startDateFormatConvert(searchTravellerModels.get(i).getStartDate()) + " - "
                        + endDateFormatConvert(searchTravellerModels.get(i).getEndDate()));
            else
                myViewHolder.tvOrderDate.setText("-");

            myViewHolder.tvOrderType.setText("" + searchTravellerModels.get(i).getDeliveryOption());
            myViewHolder.tvOrderDistance.setText("" + searchTravellerModels.get(i).getPreferredWeight());
            myViewHolder.tvOrderTypeContent.setText("Mode of Travel: " + searchTravellerModels.get(i).getModeOfTravel());
            if (searchTravellerModels.get(i).getTraveller_amount() != null) {
                myViewHolder.tvExpectedIncome.setVisibility(View.VISIBLE);
                myViewHolder.tvExpectedIncome.setText("Expected cost / Income : Rs. " + searchTravellerModels.get(i).getTraveller_amount());
            } else {
                myViewHolder.tvExpectedIncome.setVisibility(View.GONE);
            }

            myViewHolder.tvVolumeInfo.setVisibility(View.VISIBLE);
            myViewHolder.tvVolumeInfo.setText("Vol: " + searchTravellerModels.get(i).getAcceptableVolumeLength() + " * " +
                    searchTravellerModels.get(i).getAcceptableVolumeBreadth() + " * " + searchTravellerModels.get(i).getAcceptableVolumeWidth());

            if (searchTravellerModels.get(i).getRating() != 0.0) {
                myViewHolder.rbOrder.setVisibility(View.VISIBLE);
                myViewHolder.rbOrder.setRating(searchTravellerModels.get(i).getRating());
            } else {
                myViewHolder.rbOrder.setVisibility(View.GONE);
            }

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onTravellerItemClicked(searchTravellerModels.get(i));
                }
            });

        } else if (tag != null && tag.equals("order")) {
            if (searchOrderModels.get(i).getFull_name() != null &&
                    !searchOrderModels.get(i).getFull_name().equals(""))
                myViewHolder.tvName.setText("" + searchOrderModels.get(i).getFull_name());
            else
                myViewHolder.tvName.setText("-");

            if ((searchOrderModels.get(i).getStartDate() != null &&
                    !searchOrderModels.get(i).getStartDate().equals("")) &&
                    (searchOrderModels.get(i).getEndDate() != null &&
                            !searchOrderModels.get(i).getEndDate().equals("")))
                myViewHolder.tvOrderDate.setText("" + startDateFormatConvert(searchOrderModels.get(i).getStartDate()) + " - "
                        + endDateFormatConvert(searchOrderModels.get(i).getEndDate()));
            else
                myViewHolder.tvOrderDate.setText("-");

            myViewHolder.tvOrderType.setText("" + searchOrderModels.get(i).getDeliveryOption());
            myViewHolder.tvOrderDistance.setText("" + searchOrderModels.get(i).getWeight());
            myViewHolder.tvOrderTypeContent.setText("" + searchOrderModels.get(i).getNatureOfGoods());

            if(Uitility.journey.equals("MyOrdersFromProfile")){
                if(searchOrderModels.get(i).getTotal_amount() != null){

                    if(searchOrderModels.get(i).getTotal_amount() > 0){
                        myViewHolder.tvExpectedIncome.setVisibility(View.VISIBLE);
                        myViewHolder.tvExpectedIncome.setText("Expected cost / Income : Rs. " + searchOrderModels.get(i).getTotal_amount());
                    }
                }
            }else{
                if (searchOrderModels.get(i).getTraveller_amount() != null) {
                    myViewHolder.tvExpectedIncome.setVisibility(View.VISIBLE);
                    myViewHolder.tvExpectedIncome.setText("Expected cost / Income : Rs. " + searchOrderModels.get(i).getTraveller_amount());
                } else {
                    myViewHolder.tvExpectedIncome.setVisibility(View.GONE);
                }
            }

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onOrderItemClicked(searchOrderModels.get(i));
                }
            });
        } else if (tag != null && tag.equals("order_clicked_verify")) {
            if (acceptedOrdersModels.get(i).getFirstName() != null &&
                    !acceptedOrdersModels.get(i).getFirstName().equals(""))
                myViewHolder.tvName.setText("" + acceptedOrdersModels.get(i).getFirstName());
            else if (acceptedOrdersModels.get(i).getCompany_name() != null &&
                    !acceptedOrdersModels.get(i).getCompany_name().equals(""))
                myViewHolder.tvName.setText("" + acceptedOrdersModels.get(i).getCompany_name());
            else
                myViewHolder.tvName.setText("-");

            if (acceptedOrdersModels.get(i).getFull_name() != null &&
                    !acceptedOrdersModels.get(i).getFull_name().equals(""))
                myViewHolder.tvName.setText("" + acceptedOrdersModels.get(i).getFull_name());
            else
                myViewHolder.tvName.setText("-");

            if ((acceptedOrdersModels.get(i).getStartDate() != null &&
                    !acceptedOrdersModels.get(i).getStartDate().equals("")) &&
                    (acceptedOrdersModels.get(i).getEndDate() != null &&
                            !acceptedOrdersModels.get(i).getEndDate().equals("")))
                myViewHolder.tvOrderDate.setText("" + startDateFormatConvert(acceptedOrdersModels.get(i).getStartDate()) + " - "
                        + endDateFormatConvert(acceptedOrdersModels.get(i).getEndDate()));
            else
                myViewHolder.tvOrderDate.setText("-");

            myViewHolder.tvOrderType.setText("" + acceptedOrdersModels.get(i).getDeliveryOption());
            myViewHolder.tvOrderDistance.setText("" + acceptedOrdersModels.get(i).getWeight());
            myViewHolder.tvOrderTypeContent.setText("" + acceptedOrdersModels.get(i).getNatureOfGoods());
            if (acceptedOrdersModels.get(i).getTraveller_amount() != null) {
                myViewHolder.tvExpectedIncome.setVisibility(View.VISIBLE);
                myViewHolder.tvExpectedIncome.setText("Expected cost / Income : Rs. " + acceptedOrdersModels.get(i).getTraveller_amount());
            } else {
                myViewHolder.tvExpectedIncome.setVisibility(View.GONE);
            }

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.openOtpDialog(acceptedOrdersModels.get(i));
                }
            });
        } else {
            if (searchOrderModels.get(i).getFull_name() != null &&
                    !searchOrderModels.get(i).getFull_name().equals(""))
                myViewHolder.tvName.setText("" + searchOrderModels.get(i).getFull_name());
            else
                myViewHolder.tvName.setText("-");

            if ((searchOrderModels.get(i).getStartDate() != null &&
                    !searchOrderModels.get(i).getStartDate().equals("")) &&
                    (searchOrderModels.get(i).getEndDate() != null &&
                            !searchOrderModels.get(i).getEndDate().equals("")))
                myViewHolder.tvOrderDate.setText("" + startDateFormatConvert(searchOrderModels.get(i).getStartDate()) + " - "
                        + endDateFormatConvert(searchOrderModels.get(i).getEndDate()));
            else
                myViewHolder.tvOrderDate.setText("-");

            myViewHolder.tvOrderType.setText("" + searchOrderModels.get(i).getDeliveryOption());
            myViewHolder.tvOrderDistance.setText("" + searchOrderModels.get(i).getWeight());
            myViewHolder.tvOrderTypeContent.setText("" + searchOrderModels.get(i).getNatureOfGoods());
            if (searchOrderModels.get(i).getTraveller_amount() != null) {

                myViewHolder.tvExpectedIncome.setVisibility(View.VISIBLE);
                myViewHolder.tvExpectedIncome.setText("Expected cost / Income : Rs. " + searchOrderModels.get(i).getTraveller_amount());
            } else {
                myViewHolder.tvExpectedIncome.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private String startDateFormatConvert(String startDate) {
        SimpleDateFormat startDateFormat = null;
        Date date = null;
        if (startDate != null && !startDate.equals("")) {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = inputFormat.parse(startDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            startDateFormat = new SimpleDateFormat("MMM d");
        }
        assert startDateFormat != null;
        return startDateFormat.format(date);
    }

    @SuppressLint("SimpleDateFormat")
    private String endDateFormatConvert(String endDate) {
        SimpleDateFormat endDateFormat = null;
        Date date = null;
        if (endDate != null && !endDate.equals("")) {
            DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = inputFormat.parse(endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            endDateFormat = new SimpleDateFormat("MMM d yyyy");
        }
        assert endDateFormat != null;
        return endDateFormat.format(date);
    }

    @Override
    public int getItemCount() {
        if (tag != null && tag.equals("traveller")) {
            return searchTravellerModels.size();
        }
        if (tag != null && tag.equals("order_clicked_verify")) {
            return acceptedOrdersModels.size();
        }
        return searchOrderModels.size();
    }

    public void addTravellerList(List<SearchTravellerModel> data) {
        this.searchTravellerModels = data;
    }

    public void addOrderList(List<SearchOrderModel> data) {
        this.searchOrderModels = data;
    }

    public void addAcceptedOrders(List<AcceptedOrdersModel> data) {
        this.acceptedOrdersModels = data;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final TextView tvOrderDate;
        private final TextView tvOrderType;
        private final TextView tvOrderDistance;
        private final TextView tvOrderTypeContent;
        private final TextView tvExpectedIncome;
        private final TextView tvVolumeInfo;
        private final RatingBar rbOrder;

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
