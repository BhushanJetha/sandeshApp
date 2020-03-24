package com.aystech.sandesh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.model.WalletTransactionModel;

import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.MyViewHolder> {

    private Context context;
    private List<WalletTransactionModel> walletTransactionModels;

    public WalletAdapter(Context context, List<WalletTransactionModel> data) {
        this.context = context;
        this.walletTransactionModels = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.row_wallet_item, viewGroup, false);
        return new MyViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvWalletPaymentStatus.setText(walletTransactionModels.get(i).getPaymentType());
        myViewHolder.tvWalletPaymentAmt.setText("Rs. " + walletTransactionModels.get(i).getAmount());
        myViewHolder.tvPaymentMode.setText(walletTransactionModels.get(i).getPaymentMode());
        if (walletTransactionModels.get(i).getPaymentType().equals("Credited")){
            myViewHolder.imgWalletPaymentStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_credit));
        }else{
            myViewHolder.imgWalletPaymentStatus.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_debit));
        }
    }

    @Override
    public int getItemCount() {
        return walletTransactionModels.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvWalletPaymentStatus;
        private TextView tvWalletPaymentAmt;
        private TextView tvPaymentMode;
        private ImageView imgWalletPaymentStatus;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWalletPaymentStatus = itemView.findViewById(R.id.tvWalletPaymentStatus);
            tvWalletPaymentAmt = itemView.findViewById(R.id.tvWalletPaymentAmt);
            tvPaymentMode = itemView.findViewById(R.id.tvPaymentMode);
            imgWalletPaymentStatus = itemView.findViewById(R.id.imgWalletPaymentStatus);
        }
    }
}
