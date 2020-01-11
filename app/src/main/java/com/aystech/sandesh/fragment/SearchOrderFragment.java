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

import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.R;
import com.aystech.sandesh.adapter.OrderAdapter;
import com.aystech.sandesh.utils.FragmentUtil;

import java.util.Calendar;

public class SearchOrderFragment extends Fragment implements View.OnClickListener {

    Context context;
    SearchTravelerFragment searchTravelerFragment;

    Button btnSearch;
    ImageView ingDate;
    EditText etDate;

    RecyclerView rvOrder;
    OrderAdapter orderAdapter;

    private int mYear, mMonth, mDay;

    public SearchOrderFragment() {
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
        View view = inflater.inflate(R.layout.fragment_search_order, container, false);

        searchTravelerFragment = (SearchTravelerFragment) Fragment.instantiate(context,
                SearchTravelerFragment.class.getName());

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        rvOrder = view.findViewById(R.id.rvOrder);
        btnSearch = view.findViewById(R.id.btnSearch);
        etDate = view.findViewById(R.id.etDate);
        ingDate = view.findViewById(R.id.ingDate);

        bindDataToRV();
    }

    private void bindDataToRV() {
        orderAdapter = new OrderAdapter(context);
        rvOrder.setAdapter(orderAdapter);
    }

    private void onClickListener() {
        ingDate.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ingDate:
                openDatePickerDialog();
                break;

            case R.id.btnSearch:
                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        searchTravelerFragment, R.id.frame_container, true);
                break;
        }
    }

    private void openDatePickerDialog() {
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
                        etDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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
