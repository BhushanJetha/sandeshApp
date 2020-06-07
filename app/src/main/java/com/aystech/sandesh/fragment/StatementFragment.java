package com.aystech.sandesh.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.MyTransactionResponseModel;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatementFragment extends Fragment implements View.OnClickListener {

    Context context;

    private MyWalletFragmentOne myWalletFragmentOne;

    private ImageView ingStartDate, ingEndDate;
    private EditText etStartDate, etEndDate;
    private Button btnView;

    final Calendar myCalendar = Calendar.getInstance();

    private String tag, strStartDate = "", strEndDate = "";

    private ViewProgressDialog viewProgressDialog;

    public StatementFragment() {
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
        View view = inflater.inflate(R.layout.fragment_statement, container, false);

        myWalletFragmentOne = (MyWalletFragmentOne) Fragment.instantiate(context,
                MyWalletFragmentOne.class.getName());

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        ingStartDate = view.findViewById(R.id.ingFromCalendar);
        etStartDate = view.findViewById(R.id.etFrom);
        ingEndDate = view.findViewById(R.id.ingToCalendar);
        etEndDate = view.findViewById(R.id.etTo);
        btnView = view.findViewById(R.id.btnView);
    }

    private void onClickListener() {
        ingStartDate.setOnClickListener(this);
        ingEndDate.setOnClickListener(this);
        btnView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ingFromCalendar:
                tag = "start_date";
                openDatePickerDialog();
                break;

            case R.id.ingToCalendar:
                tag = "end_date";
                openDatePickerDialog();
                break;

            case R.id.btnView:
                //TODO API Call
                getStatement();
                break;
        }
    }

    private void openDatePickerDialog() {
        DatePickerDialog mDate = new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        mDate.getDatePicker().setMaxDate(System.currentTimeMillis());
        mDate.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (tag.equals("start_date")) {
                strStartDate = Uitility.dateFormat(year, monthOfYear, dayOfMonth); //StatementFragment
                etStartDate.setText(strStartDate);
            } else if (tag.equals("end_date")) {
                strEndDate = Uitility.dateFormat(year, monthOfYear, dayOfMonth); //StatementFragment
                etEndDate.setText(strEndDate);
            }
        }

    };

    private void getStatement() {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("start_date", strStartDate);
        jsonObject.addProperty("end_date", strEndDate);

        RetrofitInstance.getClient().getMyStatement(jsonObject).enqueue(new Callback<MyTransactionResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<MyTransactionResponseModel> call, @NonNull Response<MyTransactionResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                            myWalletFragmentOne, R.id.frame_container, true);
                    Bundle bundle = new Bundle();

                    myWalletFragmentOne.setArguments(bundle);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyTransactionResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }
}
