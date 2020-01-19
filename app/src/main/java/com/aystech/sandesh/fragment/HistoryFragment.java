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
import com.aystech.sandesh.adapter.OrderAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    Context context;

    RecyclerView rvHistory;
    OrderAdapter orderAdapter;

    public HistoryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        initView(view);

        bindDataToRV();

        return view;
    }

    private void initView(View view) {
        rvHistory = view.findViewById(R.id.rvHistory);
    }

    private void bindDataToRV() {
        orderAdapter = new OrderAdapter(context);
        rvHistory.setAdapter(orderAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
