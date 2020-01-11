package com.aystech.sandesh.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.adapter.OrderAdapter;
import com.aystech.sandesh.utils.FragmentUtil;

import java.util.Calendar;

public class SearchTravelerFragment extends Fragment implements View.OnClickListener {

    Context context;

    Button btnSearch;
    ImageView ingStartDate, ingEndDate;
    EditText etStartDate, etEndDate;

    RecyclerView rvOrder;
    OrderAdapter orderAdapter;

    MyWalletFragmentOne myWalletFragmentOne;

    private int mYear, mMonth, mDay;

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
        btnSearch = view.findViewById(R.id.btnSearch);

        ingStartDate = view.findViewById(R.id.ingStartDate);
        etStartDate = view.findViewById(R.id.etStartDate);

        ingEndDate = view.findViewById(R.id.ingEndDate);
        etEndDate = view.findViewById(R.id.etEndDate);

        bindDataToRV();
    }

    private void bindDataToRV() {
        orderAdapter = new OrderAdapter(context);
        rvOrder.setAdapter(orderAdapter);
    }

    private void onClickListener() {
        ingStartDate.setOnClickListener(this);
        ingEndDate.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ingStartDate:
                openDatePickerDialog("start_date");
                break;

            case R.id.ingEndDate:
                openDatePickerDialog("end_date");
                break;

            case R.id.btnSearch:
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        myWalletFragmentOne, R.id.frame_container, true);
                break;
        }
    }

    private void openDatePickerDialog(final String tag) {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        final DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        if (tag.equals("start_date")) {
                            etStartDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }else if (tag.equals("end_date")){
                            etEndDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
