package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.R;
import com.aystech.sandesh.adapter.OrderAdapter;
import com.aystech.sandesh.utils.FragmentUtil;

public class SearchTravelerFragment extends Fragment {

    Context context;

    Button btnSearch;
    RecyclerView rvOrder;
    OrderAdapter orderAdapter;

    MyWalletFragmentOne myWalletFragmentOne;

    public SearchTravelerFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search_traveler, container, false);

        myWalletFragmentOne = (MyWalletFragmentOne) Fragment.instantiate(context,
                MyWalletFragmentOne.class.getName());

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        rvOrder = view.findViewById(R.id.rvOrder);

        orderAdapter = new OrderAdapter(context);
        rvOrder.setAdapter(orderAdapter);

        btnSearch = view.findViewById(R.id.btnSearch);
    }

    private void onClickListener() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        myWalletFragmentOne, R.id.frame_container, true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false,"", false);
    }
}
