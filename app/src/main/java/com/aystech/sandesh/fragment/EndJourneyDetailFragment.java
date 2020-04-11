package com.aystech.sandesh.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.FragmentUtil;
import com.aystech.sandesh.utils.ImageSelectionMethods;
import com.aystech.sandesh.utils.UserSession;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EndJourneyDetailFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private DashboardFragment dashboardFragment;

    private Group gpNewSelfie, gpReceivedParcel;
    private ImageView imgNewSelfie, imgReceivedParcel;
    private Button btnEndJourney;

    private Uri picUri;
    private Bitmap myBitmap;

    private String strSelfieFilePath, strParcelFilePath;
    private String tag;
    private int parcelId, travelId, deliveryId;

    private UserSession userSession;
    private ViewProgressDialog viewProgressDialog;

    public EndJourneyDetailFragment() {
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
        View view = inflater.inflate(R.layout.fragment_end_journey_detail, container, false);

        if (getArguments() != null) {
            parcelId = getArguments().getInt("parcel_id");
            travelId = getArguments().getInt("travel_id");
            deliveryId = getArguments().getInt("delivery_id");
        }

        dashboardFragment = (DashboardFragment) Fragment.instantiate(context,
                DashboardFragment.class.getName());

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        userSession = new UserSession(context);
        viewProgressDialog = ViewProgressDialog.getInstance();

        gpNewSelfie = view.findViewById(R.id.gpNewSelfie);
        imgNewSelfie = view.findViewById(R.id.imgNewSelfie);
        imgNewSelfie.setImageResource(R.drawable.ic_parcel);
        gpReceivedParcel = view.findViewById(R.id.gpReceivedParcel);
        imgReceivedParcel = view.findViewById(R.id.imgReceivedParcel);
        imgReceivedParcel.setImageResource(R.drawable.ic_parcel);
        btnEndJourney = view.findViewById(R.id.btnEndJourney);
    }

    private void onClickListener() {
        gpNewSelfie.setOnClickListener(this);
        gpReceivedParcel.setOnClickListener(this);
        btnEndJourney.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.gpNewSelfie:
                gotoSelectPicture("selfie");
                break;

            case R.id.gpReceivedParcel:
                gotoSelectPicture("parcel");
                break;

            case R.id.btnEndJourney:
                //TODO API Call
                endJourney();
                break;
        }
    }

    private void gotoSelectPicture(String type) {
        tag = type;
        startActivityForResult(ImageSelectionMethods.getPickImageChooserIntent(context), 200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (ImageSelectionMethods.getPickImageResultUri(context, data) != null) {
                picUri = ImageSelectionMethods.getPickImageResultUri(context, data);
                if (tag.equals("selfie")) {
                    strSelfieFilePath = ImageSelectionMethods.getPath(context, picUri);

                    if (strSelfieFilePath.equals("Not found")) {
                        strSelfieFilePath = picUri.getPath();
                    }
                } else if (tag.equals("parcel")) {
                    strParcelFilePath = ImageSelectionMethods.getPath(context, picUri);

                    if (strParcelFilePath.equals("Not found")) {
                        strParcelFilePath = picUri.getPath();
                    }
                }

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), picUri);
                    myBitmap = ImageSelectionMethods.getResizedBitmap(myBitmap, 500);

                    if (tag.equals("selfie")) {
                        imgNewSelfie.setImageBitmap(myBitmap);
                    } else if (tag.equals("parcel")) {
                        imgReceivedParcel.setImageBitmap(myBitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void endJourney() {
        ViewProgressDialog.getInstance().showProgress(context);

        RequestBody travel_id = RequestBody.create(MultipartBody.FORM, String.valueOf(travelId));
        RequestBody parcel_id = RequestBody.create(MultipartBody.FORM, String.valueOf(parcelId));
        RequestBody delivery_id = RequestBody.create(MultipartBody.FORM, String.valueOf(deliveryId));

        MultipartBody.Part parcel_pic_body;
        if (strParcelFilePath != null && !strParcelFilePath.equals("")) {
            File file = new File(strParcelFilePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            parcel_pic_body = MultipartBody.Part.createFormData("parcel_pic", file.getName(), requestBody);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            parcel_pic_body = MultipartBody.Part.createFormData("parcel_pic", "", requestBody);
        }

        MultipartBody.Part selfie_pic_body;
        if (strSelfieFilePath != null && !strSelfieFilePath.equals("")) {
            File file = new File(strSelfieFilePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            selfie_pic_body = MultipartBody.Part.createFormData("selfie_pic", file.getName(), requestBody);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            selfie_pic_body = MultipartBody.Part.createFormData("selfie_pic", "", requestBody);
        }

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.endJourney(
                travel_id,
                parcel_id,
                delivery_id,
                parcel_pic_body,
                selfie_pic_body
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        //after end journey travel_id should be removed from internal storage
                        //caused for next order new travel_id should be managed.
                        userSession.setTravelId(0);
                        userSession.setHours(0);
                        userSession.setMinute(0);

                        FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(), dashboardFragment, R.id.frame_container,
                                false);
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
}
