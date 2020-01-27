package com.aystech.sandesh.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.adapter.OrderAdapter;
import com.aystech.sandesh.model.SearchOrderModel;
import com.aystech.sandesh.model.SearchOrderResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTravelerFragment extends Fragment implements View.OnClickListener {

    Context context;

    Button btnSearch;
    ImageView ingStartDate, ingEndDate;
    EditText etFrom, etTo, etStartDate, etEndDate;

    RecyclerView rvOrder;
    OrderAdapter orderAdapter;

    MyWalletFragmentOne myWalletFragmentOne;

    private int mYear, mMonth, mDay;

    ViewProgressDialog viewProgressDialog;

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
        viewProgressDialog = ViewProgressDialog.getInstance();

        rvOrder = view.findViewById(R.id.rvOrder);
        btnSearch = view.findViewById(R.id.btnSearch);

        ingStartDate = view.findViewById(R.id.ingStartDate);
        etStartDate = view.findViewById(R.id.etStartDate);

        ingEndDate = view.findViewById(R.id.ingEndDate);
        etEndDate = view.findViewById(R.id.etEndDate);
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
                //TODO API Call
                //searchTravelerData();
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
                        } else if (tag.equals("end_date")) {
                            etEndDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }


    private void searchTravelerData() {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("from_city_id", etFrom.getText().toString());
        jsonObject.addProperty("to_city_id", etTo.getText().toString());
        jsonObject.addProperty("start_date", etStartDate.getText().toString());
        jsonObject.addProperty("end_date", etEndDate.getText().toString());

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<SearchOrderResponseModel> call = apiInterface.searchTraveller(
                jsonObject
        );
        call.enqueue(new Callback<SearchOrderResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SearchOrderResponseModel> call, @NonNull Response<SearchOrderResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        bindDataToRV(response.body().getData());
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchOrderResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindDataToRV(List<SearchOrderModel> data) {
        orderAdapter = new OrderAdapter(context, data);
        rvOrder.setAdapter(orderAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
