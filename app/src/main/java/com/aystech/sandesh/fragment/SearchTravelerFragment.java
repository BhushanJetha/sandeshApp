package com.aystech.sandesh.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.adapter.NoDataAdapter;
import com.aystech.sandesh.adapter.OrderAdapter;
import com.aystech.sandesh.interfaces.OnItemClickListener;
import com.aystech.sandesh.model.AcceptedOrdersModel;
import com.aystech.sandesh.model.CityModel;
import com.aystech.sandesh.model.CityResponseModel;
import com.aystech.sandesh.model.SearchOrderModel;
import com.aystech.sandesh.model.SearchTravellerModel;
import com.aystech.sandesh.model.SearchTravellerResponseModel;
import com.aystech.sandesh.model.StateModel;
import com.aystech.sandesh.model.StateResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.UserSession;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTravelerFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private StateResponseModel stateResponseModel;
    private CityResponseModel cityResponseModel;

    private ConstraintLayout clTravellerList;
    private TextView tvResultCount, tvSortBy;

    private Spinner spinnerFromState, spinnerFromCity, spinnerToState, spinnerToCity;
    private Button btnSearch;
    private ImageView ingStartDate, ingEndDate;
    private EditText etFromPincode, etToPincode, etStartDate, etEndDate;

    private RecyclerView rvOrder;
    private OrderAdapter orderAdapter;

    private TravellerDetailFragment travellerDetailFragment;

    private final Calendar myCalendar = Calendar.getInstance();

    private int fromStateId, fromCityId, toStateId, toCityId;
    private String strToPinCode, strFromPincode, strStartDate = "", strEndDate = "";
    private String tag;

    private ViewProgressDialog viewProgressDialog;
    private UserSession userSession;

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

        travellerDetailFragment = (TravellerDetailFragment) Fragment.instantiate(context,
                TravellerDetailFragment.class.getName());

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();
        userSession = new UserSession(context);

        clTravellerList = view.findViewById(R.id.clTravellerList);
        tvResultCount = view.findViewById(R.id.tvResultCount);
        tvSortBy = view.findViewById(R.id.tvSortBy);

        spinnerFromState = view.findViewById(R.id.spinnerFromState);
        spinnerFromCity = view.findViewById(R.id.spinnerFromCity);
        etFromPincode = view.findViewById(R.id.etFromPincode);
        spinnerToState = view.findViewById(R.id.spinnerToState);
        spinnerToCity = view.findViewById(R.id.spinnerToCity);
        etToPincode = view.findViewById(R.id.etToPincode);
        ingStartDate = view.findViewById(R.id.ingStartDate);
        etStartDate = view.findViewById(R.id.etStartDate);
        ingEndDate = view.findViewById(R.id.ingEndDate);
        etEndDate = view.findViewById(R.id.etEndDate);


        rvOrder = view.findViewById(R.id.rvOrder);
        btnSearch = view.findViewById(R.id.btnSearch);
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
                tag = "start_date";
                openDatePickerDialog();
                break;

            case R.id.ingEndDate:
                tag = "end_date";
                openDatePickerDialog();
                break;

            case R.id.btnSearch:
                strToPinCode = etToPincode.getText().toString();
                strFromPincode = etFromPincode.getText().toString();

                if(fromCityId != 0){
                    if(toCityId != 0){
                        if (!strStartDate.isEmpty()) {
                            if (!strEndDate.isEmpty()) {
                                //TODO API Call
                                searchTravelerData();
                            } else {
                                Uitility.showToast(getActivity(), "Please select end date !!");
                            }
                        } else {
                            Uitility.showToast(getActivity(), "Please select start date !!");
                        }
                    }else {
                        Uitility.showToast(getActivity(), "Please select to city !!");
                    }
                }else {
                    Uitility.showToast(getActivity(), "Please select from city !!");
                }

                break;
        }
    }

    private void openDatePickerDialog() {
        DatePickerDialog mDate = new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        mDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        mDate.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            view.setMinDate(System.currentTimeMillis() - 1000);

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            if (tag.equals("start_date")) {
                strStartDate = Uitility.dateFormat(year, monthOfYear, dayOfMonth); //SearchTravelerFragment
                etStartDate.setText(strStartDate);
            } else if (tag.equals("end_date")) {
                strEndDate = Uitility.dateFormat(year, monthOfYear, dayOfMonth); //SearchTravelerFragment
                etEndDate.setText(strEndDate);
            }
        }

    };

    private void searchTravelerData() {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("from_city_id", fromCityId);
        jsonObject.addProperty("from_pincode", strFromPincode);
        jsonObject.addProperty("to_city_id", toCityId);
        jsonObject.addProperty("to_pincode", strToPinCode);
        jsonObject.addProperty("start_date", strStartDate);
        jsonObject.addProperty("end_date", strEndDate);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<SearchTravellerResponseModel> call = apiInterface.searchTraveller(
                jsonObject
        );
        call.enqueue(new Callback<SearchTravellerResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SearchTravellerResponseModel> call, @NonNull Response<SearchTravellerResponseModel> response) {
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
            public void onFailure(@NonNull Call<SearchTravellerResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindDataToRV(List<SearchTravellerModel> data) {
        if (data.size() > 0) {
            clTravellerList.setVisibility(View.VISIBLE);
            if (data.size() == 1)
                tvResultCount.setText(data.size() + " result found");
            else
                tvResultCount.setText(data.size() + " results found");

            orderAdapter = new OrderAdapter(context, "traveller", new OnItemClickListener() {
                @Override
                public void onOrderItemClicked(SearchOrderModel searchOrderModel) {
                }

                @Override
                public void onTravellerItemClicked(SearchTravellerModel searchTravellerModel) {
                    FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                            travellerDetailFragment, R.id.frame_container, true);
                    Bundle bundle = new Bundle();
                    bundle.putInt("travel_id", searchTravellerModel.getTravelId());
                    bundle.putString("tag", "normal");
                    travellerDetailFragment.setArguments(bundle);
                }

                @Override
                public void openOtpDialog(AcceptedOrdersModel searchTravellerModel) {
                }
            });
            orderAdapter.addTravellerList(data);
            rvOrder.setAdapter(orderAdapter);
        } else {
            Uitility.showToast(context, "No Data Found!");
            clTravellerList.setVisibility(View.VISIBLE);
            tvResultCount.setVisibility(View.GONE);
            tvSortBy.setVisibility(View.GONE);
            NoDataAdapter noDataAdapter = new NoDataAdapter(context, "No Traveller Found!");
            rvOrder.setAdapter(noDataAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);

        String fromState = userSession.getFromState();
        if (fromState.length() > 0) {
            Gson gson = new Gson();
            stateResponseModel = gson.fromJson(fromState, StateResponseModel.class);
            bindStateDataToUI(stateResponseModel.getData());
        } else {
            //TODO API Call
            getState();
        }
    }

    private void getState() {
        RetrofitInstance.getClient().getState().enqueue(new Callback<StateResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<StateResponseModel> call, @NonNull Response<StateResponseModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        stateResponseModel = response.body();
                        Gson gson = new Gson();
                        String json = gson.toJson(response.body());
                        userSession.setFromState(json);
                        bindStateDataToUI(response.body().getData());
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<StateResponseModel> call, @NonNull Throwable t) {
            }
        });
    }

    private void bindStateDataToUI(List<StateModel> data) {
        ArrayList<String> manufactureArrayList = new ArrayList<>();

        manufactureArrayList.add(0,"Select State");
        for (int i = 0; i < data.size(); i++) {
            manufactureArrayList.add(data.get(i).getStateName());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, manufactureArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFromState.setAdapter(adapter);
        spinnerToState.setAdapter(adapter);

        spinnerFromState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    fromStateId = stateResponseModel.getStateId(selectedItem);
                    getCity(fromStateId, "from");
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerToState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    toStateId = stateResponseModel.getStateId(selectedItem);
                    getCity(toStateId, "to");
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getCity(int strStateId, final String tag) {
        if (tag.equals("to"))
            ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("state_id", strStateId);

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CityResponseModel> call = apiInterface.getCity(jsonObject);
        call.enqueue(new Callback<CityResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<CityResponseModel> call, @NonNull Response<CityResponseModel> response) {
                if (tag.equals("to"))
                    viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        cityResponseModel = response.body();
                        bindCityDataToUI(response.body().getData(), tag);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CityResponseModel> call, @NonNull Throwable t) {
                if (tag.equals("to"))
                    viewProgressDialog.hideDialog();
            }
        });
    }

    private void bindCityDataToUI(List<CityModel> data, String tag) {
        if (tag.equals("from")) {
            ArrayList<String> manufactureArrayList = new ArrayList<>();

            manufactureArrayList.add(0,"Select City");
            for (int i = 0; i < data.size(); i++) {
                manufactureArrayList.add(data.get(i).getCityName());
            }
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, manufactureArrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerFromCity.setAdapter(adapter);
        }
        if (tag.equals("to")) {
            ArrayList<String> manufactureArrayList = new ArrayList<>();

            manufactureArrayList.add(0,"Select City");
            for (int i = 0; i < data.size(); i++) {
                manufactureArrayList.add(data.get(i).getCityName());
            }
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, manufactureArrayList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerToCity.setAdapter(adapter);
        }

        spinnerFromCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    fromCityId = cityResponseModel.getCityId(selectedItem);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerToCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();

                if (!selectedItem.equals("")) {
                    toCityId = cityResponseModel.getCityId(selectedItem);
                }
            } // to close the onItemSelected

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
