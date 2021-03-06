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
import com.aystech.sandesh.adapter.NoDataAdapter;
import com.aystech.sandesh.adapter.WalletAdapter;
import com.aystech.sandesh.model.WalletTransactionModel;

import java.util.ArrayList;
import java.util.List;

public class MyWalletFragmentOne extends Fragment {

    private Context context;

    private ArrayList<WalletTransactionModel> walletTransactionModels = new ArrayList<>();

    private RecyclerView rvPaymentHistory;

    public MyWalletFragmentOne() {
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
        View view = inflater.inflate(R.layout.fragment_my_wallet_fragment_one, container, false);

        if (getArguments()!=null){
            walletTransactionModels = getArguments().getParcelableArrayList("transactionData");
        }
        initView(view);

        bindDataToUI(walletTransactionModels);

        return view;
    }

    private void initView(View view) {
        rvPaymentHistory = view.findViewById(R.id.rvPaymentHistory);
    }

    private void bindDataToUI(List<WalletTransactionModel> data) {
        if (data.size() > 0) {
            WalletAdapter walletAdapter = new WalletAdapter(context, data);
            rvPaymentHistory.setAdapter(walletAdapter);
        } else {
            NoDataAdapter noDataAdapter = new NoDataAdapter(context, "No record found!");
            rvPaymentHistory.setAdapter(noDataAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
