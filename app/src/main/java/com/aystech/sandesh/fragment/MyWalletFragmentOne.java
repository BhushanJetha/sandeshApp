package com.aystech.sandesh.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.adapter.WalletAdapter;
import com.aystech.sandesh.utils.FragmentUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyWalletFragmentOne extends Fragment {

    Context context;

    Button btnAddBal;
    RecyclerView rvPaymentHistory;
    WalletAdapter walletAdapter;

    MyWalletFragmentTwo myWalletFragmentTwo;

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


        myWalletFragmentTwo = (MyWalletFragmentTwo) Fragment.instantiate(context,
                MyWalletFragmentTwo.class.getName());

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        rvPaymentHistory = view.findViewById(R.id.rvPaymentHistory);

        walletAdapter = new WalletAdapter(context);
        rvPaymentHistory.setAdapter(walletAdapter);

        btnAddBal = view.findViewById(R.id.btnAddBal);
    }

    private void onClickListener() {
        btnAddBal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        myWalletFragmentTwo, R.id.frame_container, true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, "", false);
    }

}
