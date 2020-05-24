package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.adapter.NoDataAdapter;
import com.aystech.sandesh.adapter.OrderAdapter;
import com.aystech.sandesh.interfaces.OnItemClickListener;
import com.aystech.sandesh.model.AcceptedOrdersModel;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.MyOrdersResponseModel;
import com.aystech.sandesh.model.MyRidesResponseModel;
import com.aystech.sandesh.model.SearchOrderModel;
import com.aystech.sandesh.model.SearchTravellerModel;
import com.aystech.sandesh.model.SearchTravellerResponseModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartJourneyFragment extends Fragment {

    private static final String TAG = "StartJourneyFragment";
    private Context context;

    private OrderListFragment orderListFragment;
    private TrackYourParcelFragment trackYourParcelFragment;
    private OrderDetailFragment orderDetailFragment;
    private TravellerDetailFragment travellerDetailFragment;
    private NominateAlternatePersonFragment nominateAlternatePersonFragment;

    private TextView tvTitle;
    private RecyclerView rvTraveller;
    private OrderAdapter orderAdapter;

    private String tag, rating;

    private ViewProgressDialog viewProgressDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start_journey, container, false);

        if (getArguments() != null)
            tag = getArguments().getString("tag");

        orderListFragment = (OrderListFragment) Fragment.instantiate(context,
                OrderListFragment.class.getName());
        trackYourParcelFragment = (TrackYourParcelFragment)
                Fragment.instantiate(context, TrackYourParcelFragment.class.getName());
        orderDetailFragment = (OrderDetailFragment) Fragment.instantiate(context,
                OrderDetailFragment.class.getName());
        travellerDetailFragment = (TravellerDetailFragment) Fragment.instantiate(context,
                TravellerDetailFragment.class.getName());
        nominateAlternatePersonFragment = (NominateAlternatePersonFragment) Fragment.instantiate(context,
                NominateAlternatePersonFragment.class.getName());

        initView(view);

        if (tag != null && tag.equals("my_rides")) {
            //TODO API Call
            getMyRides();
        } else if (tag != null && tag.equals("my_orders")) {
            //TODO API Call
            getMyOrders();
        } else if (tag != null && tag.equals("nominate_person")) {
            //TODO API Call
            getData(); //nominate_person
        } else {
            //TODO API Call
            getData(); //in else
        }
        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        rvTraveller = view.findViewById(R.id.rvTraveller);
        tvTitle = view.findViewById(R.id.tvTitle);
        if (tag != null && tag.equals("track_parcel")) {
            tvTitle.setText("My Traveller List");
        } else if (tag != null && tag.equals("my_rides")) {
            tvTitle.setText("My Rides");
        } else if (tag != null && tag.equals("my_orders")) {
            tvTitle.setText("My Orders");
        } else if (tag != null && tag.equals("nominate_person")) {
            tvTitle.setText("Traveller List");
        } else {
            tvTitle.setText("Start Journey");
        }
    }

    private void getMyRides() {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "travel");

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<MyRidesResponseModel> call = apiInterface.getMyRidesHistory(
                jsonObject
        );
        call.enqueue(new Callback<MyRidesResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<MyRidesResponseModel> call, @NonNull Response<MyRidesResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        bindTravellerDataToRV(response.body().getData()); //getMyRides
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyRidesResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void getMyOrders() {
        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "parcel");

        RetrofitInstance.getClient().getMyOrdersHistory(jsonObject).enqueue(new Callback<MyOrdersResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<MyOrdersResponseModel> call, @NonNull Response<MyOrdersResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        bindOrdersDataToRV(response.body().getData());
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MyOrdersResponseModel> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    private void getData() {
        ViewProgressDialog.getInstance().showProgress(context);

        RetrofitInstance.getClient().getMyTravellerList().enqueue(new Callback<SearchTravellerResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<SearchTravellerResponseModel> call, @NonNull Response<SearchTravellerResponseModel> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        bindTravellerDataToRV(response.body().getData()); //getData
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

    private void bindTravellerDataToRV(List<SearchTravellerModel> data) {
        if (data.size() > 0) {
            orderAdapter = new OrderAdapter(context, "traveller", new OnItemClickListener() {
                @Override
                public void onOrderItemClicked(SearchOrderModel searchOrderModel) {
                }

                @Override
                public void onTravellerItemClicked(SearchTravellerModel searchTravellerModel) {
                    if (tag != null && !tag.equals("")) {
                        switch (tag) {
                            case "track_parcel": {
                                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                                        trackYourParcelFragment, R.id.frame_container, true);
                                Bundle bundle = new Bundle();
                                bundle.putInt("travel_id", searchTravellerModel.getTravelId());
                                trackYourParcelFragment.setArguments(bundle);
                                break;
                            }
                            case "my_rides": {
                                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                                        travellerDetailFragment, R.id.frame_container, true);
                                Bundle bundle = new Bundle();
                                bundle.putInt("travel_id", searchTravellerModel.getTravelId());
                                bundle.putString("tag", "my_rides");
                                travellerDetailFragment.setArguments(bundle);
                                break;
                            }
                            case "nominate_person": {
                                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                                        nominateAlternatePersonFragment, R.id.frame_container, true);
                                Bundle bundle = new Bundle();
                                bundle.putInt("travel_id", searchTravellerModel.getTravelId());
                                nominateAlternatePersonFragment.setArguments(bundle);
                                break;
                            }
                        }
                    } else {
                        FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                                orderListFragment, R.id.frame_container,
                                true);
                        Bundle bundle = new Bundle();
                        bundle.putInt("travel_id", searchTravellerModel.getTravelId());
                        bundle.putString("tag", "order_clicked_verify");
                        orderListFragment.setArguments(bundle);
                    }
                }

                @Override
                public void openOtpDialog(AcceptedOrdersModel searchTravellerModel) {
                }
            });
            orderAdapter.addTravellerList(data);
            rvTraveller.setAdapter(orderAdapter);
        } else {
            NoDataAdapter noDataAdapter = new NoDataAdapter(context, "No Traveller Found!");
            rvTraveller.setAdapter(noDataAdapter);
        }
    }

    private void bindOrdersDataToRV(List<SearchOrderModel> data) {
        if (data.size() > 0) {
            orderAdapter = new OrderAdapter(context, "order", new OnItemClickListener() {
                @Override
                public void onOrderItemClicked(SearchOrderModel searchOrderModel) {
                    Log.e(TAG, "onOrderItemClicked: " + searchOrderModel.getTravelId());

                    showRatingDialog(searchOrderModel);
                }

                @Override
                public void onTravellerItemClicked(SearchTravellerModel searchTravellerModel) {
                }

                @Override
                public void openOtpDialog(AcceptedOrdersModel searchTravellerModel) {
                }
            });
            orderAdapter.addOrderList(data);
            rvTraveller.setAdapter(orderAdapter);
        } else {
            NoDataAdapter noDataAdapter = new NoDataAdapter(context, "No Order Found!");
            rvTraveller.setAdapter(noDataAdapter);
        }
    }

    private void showRatingDialog(final SearchOrderModel searchOrderModel) {
        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.dialog_rating, null);

        final AlertDialog.Builder alert = new AlertDialog.Builder(context);
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final RatingBar ratingBar = alertLayout.findViewById(R.id.ratingBar);
        final EditText etFeedback = alertLayout.findViewById(R.id.etFeedback);
        Button btnSubmitRating = alertLayout.findViewById(R.id.btnSubmitRating);

        final AlertDialog dialog = alert.create();
        dialog.show();

        btnSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = String.valueOf(ratingBar.getRating());
                if (TextUtils.isEmpty(rating)) {
                    Toast.makeText(context, "Please select rating...", Toast.LENGTH_SHORT).show();
                } /*else if (TextUtils.isEmpty(etFeedback.getText().toString().trim())) {
                    etFeedback.setError("Please give us your valuable feedback");
                    etFeedback.requestFocus();
                } */else {
                    etFeedback.setError(null);

                    dialog.dismiss();

                    //TODO API Call
                    giveRating(searchOrderModel);
                }
            }
        });
    }

    private void giveRating(final SearchOrderModel searchOrderModel) {

        ViewProgressDialog.getInstance().showProgress(context);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("travel_id", searchOrderModel.getTravelId());
        jsonObject.addProperty("rating", rating);
        jsonObject.addProperty("feedback_msg", "");

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.rating(
                jsonObject
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                                orderDetailFragment, R.id.frame_container, true);
                        Bundle bundle = new Bundle();
                        bundle.putInt("parcel_id", searchOrderModel.getParcelId());
                        bundle.putString("tag", "just_show_order_detail");
                        orderDetailFragment.setArguments(bundle);
                    } else {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                viewProgressDialog.hideDialog();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
