package com.aystech.sandesh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aystech.sandesh.R;
import com.bumptech.glide.Glide;

public class NoDataAdapter extends RecyclerView.Adapter<NoDataAdapter.MyViewHolder> {

    private final Context context;
    private final String title;

    public NoDataAdapter(Context context, String title) {
        this.context = context;
        this.title = title;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_no_data_found,
                viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Glide.with(context)
                .asGif()
                .load(R.drawable.ic_no_data_found)
                .into(myViewHolder.imgCommon);
        myViewHolder.tvTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgCommon;
        private final TextView tvTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCommon = itemView.findViewById(R.id.imgCommon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
