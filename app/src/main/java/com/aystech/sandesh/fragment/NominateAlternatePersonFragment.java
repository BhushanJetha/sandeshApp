package com.aystech.sandesh.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.ImageSelectionMethods;
import com.aystech.sandesh.utils.ViewProgressDialog;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NominateAlternatePersonFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private ConstraintLayout gpPanNo;
    private ImageView imgPan, imgPanCamera;
    private TextView tvPanCard;
    private EditText etUserName, etMobileNo, etPanNo;
    private Button btnSubmit;

    private String userName, mobileNo, panNo;
    int travelId;

    private Uri picUri;
    private Bitmap myBitmap;
    private String filepath;

    private ViewProgressDialog viewProgressDialog;

    public NominateAlternatePersonFragment() {
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
        View view = inflater.inflate(R.layout.fragment_nominate_alternate_person, container, false);

        if (getArguments() != null) {
            travelId = getArguments().getInt("travel_id");
        }

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        gpPanNo = view.findViewById(R.id.gpPanNo);
        imgPanCamera = view.findViewById(R.id.imgPanCamera);
        imgPan = view.findViewById(R.id.imgPan);
        imgPan.setImageResource(R.drawable.ic_sandesh_logo);
        tvPanCard = view.findViewById(R.id.tvPanCard);
        etUserName = view.findViewById(R.id.etUserName);
        etMobileNo = view.findViewById(R.id.etMobileNo);
        etPanNo = view.findViewById(R.id.etPanNo);
        btnSubmit = view.findViewById(R.id.btnSubmit);
    }

    private void onClickListener() {
        btnSubmit.setOnClickListener(this);
        gpPanNo.setOnClickListener(this);
        imgPanCamera.setOnClickListener(this);
        imgPan.setOnClickListener(this);
        tvPanCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                userName = etUserName.getText().toString().trim();
                mobileNo = etMobileNo.getText().toString().trim();
                panNo = etPanNo.getText().toString().trim();

                //TODO API Call
                nominateAlternatePerson();
                break;

            case R.id.gpPanNo:
            case R.id.imgPan:
            case R.id.imgPanCamera:
            case R.id.tvPanCard:
                gotoSelectPicture();
                break;
        }
    }

    private void gotoSelectPicture() {
        startActivityForResult(ImageSelectionMethods.getPickImageChooserIntent(context), 200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (ImageSelectionMethods.getPickImageResultUri(context, data) != null) {
                picUri = ImageSelectionMethods.getPickImageResultUri(context, data);
                filepath = ImageSelectionMethods.getPath(context, picUri);

                if (filepath.equals("Not found")) {
                    filepath = picUri.getPath();
                }

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), picUri);
                    myBitmap = ImageSelectionMethods.getResizedBitmap(myBitmap, 500);

                    imgPan.setImageBitmap(myBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void nominateAlternatePerson() {
        ViewProgressDialog.getInstance().showProgress(context);

        RequestBody travel_id = RequestBody.create(MultipartBody.FORM, String.valueOf(travelId));
        RequestBody user_name = RequestBody.create(MultipartBody.FORM, userName);
        RequestBody mobile_no = RequestBody.create(MultipartBody.FORM, mobileNo);
        RequestBody pancard_no = RequestBody.create(MultipartBody.FORM, panNo);

        MultipartBody.Part body;
        if (filepath != null && !filepath.equals("")) {
            File file = new File(filepath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("pancard_pic", file.getName(), requestBody);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            body = MultipartBody.Part.createFormData("pancard_pic", "", requestBody);
        }

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.nominateAlternatePerson(
                travel_id,
                user_name,
                mobile_no,
                pancard_no,
                body
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        ((MainActivity) context).getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
