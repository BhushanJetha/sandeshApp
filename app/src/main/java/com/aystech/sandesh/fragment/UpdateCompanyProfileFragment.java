package com.aystech.sandesh.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.model.CorporateModel;
import com.aystech.sandesh.model.UserModel;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.AppController;
import com.aystech.sandesh.utils.ImageSelectionMethods;
import com.aystech.sandesh.utils.Uitility;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCompanyProfileFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private UserModel userModel;
    private CorporateModel corporateModel;

    private ImageView imgCompanyProfile, imgCompanyProfileCamera;
    private EditText etCompanyName, etBranch, etGSTNo, etAuthorisedName, etDesignation, etEmailId;
    Button btnUpdate;

    String strCompanyName, strBranch, strGSTNo, strAuthorisedName, strDesignation, strEmailId;

    Uri picUri;
    Bitmap myBitmap;
    private String filepath;

    private ViewProgressDialog viewProgressDialog;

    public UpdateCompanyProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_update_company_profile, container, false);

        if (getArguments() != null) {
            userModel = getArguments().getParcelable("userModel");
            corporateModel = getArguments().getParcelable("corporateModel");
        }

        initView(view);

        setDataToUI();

        onClickListener();

        return view;
    }

    private void initView(View view) {
        viewProgressDialog = ViewProgressDialog.getInstance();

        imgCompanyProfile = view.findViewById(R.id.imgCompanyProfile);
        imgCompanyProfileCamera = view.findViewById(R.id.imgCompanyProfileCamera);
        etCompanyName = view.findViewById(R.id.etCompanyName);
        etBranch = view.findViewById(R.id.etBranch);
        etGSTNo = view.findViewById(R.id.etGSTNo);
        etAuthorisedName = view.findViewById(R.id.etAuthorisedName);
        etDesignation = view.findViewById(R.id.etDesignation);
        etEmailId = view.findViewById(R.id.etEmailId);
        btnUpdate = view.findViewById(R.id.btnUpdate);
    }

    private void setDataToUI() {
        Glide.with(context)
                .load(AppController.imageURL + userModel.getProfileImg())
                .error(R.drawable.ic_logo_sandesh)
                .into(imgCompanyProfile);

        etCompanyName.setText(corporateModel.getCompanyName());
        strCompanyName = corporateModel.getCompanyName();

        etBranch.setText(corporateModel.getBranch());
        strBranch = corporateModel.getBranch();

        etGSTNo.setText(corporateModel.getGstNo());
        strGSTNo = corporateModel.getGstNo();

        etAuthorisedName.setText(corporateModel.getAuthPersonName());
        strAuthorisedName = corporateModel.getAuthPersonName();

        etDesignation.setText(corporateModel.getDesignation());
        strDesignation = corporateModel.getDesignation();

        etEmailId.setText(userModel.getEmailId());
        strEmailId = userModel.getEmailId();
    }

    private void onClickListener() {
        imgCompanyProfile.setOnClickListener(this);
        imgCompanyProfileCamera.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdate:
                strCompanyName = etCompanyName.getText().toString();
                strBranch = etBranch.getText().toString();
                strGSTNo = etGSTNo.getText().toString();
                strAuthorisedName = etAuthorisedName.getText().toString();
                strDesignation = etDesignation.getText().toString();
                strEmailId = etEmailId.getText().toString();


                if(!strCompanyName.isEmpty()){
                    if(!strBranch.isEmpty()){
                        if (!strGSTNo.isEmpty()) {
                            if (!strAuthorisedName.isEmpty()) {
                                if (!strDesignation.isEmpty()) {
                                    if (!strEmailId.isEmpty()) {
                                        if (Uitility.isValidEmailId(strEmailId)) {
                                            //TODO API Call
                                            updateProfile();
                                        } else {
                                            Uitility.showToast(getActivity(), "Please enter valid email id !");
                                        }
                                    } else {
                                        Uitility.showToast(getActivity(), "Please enter your company email id !");
                                    }
                                } else {
                                    Uitility.showToast(getActivity(), "Please enter authorised person designation !");
                                }
                            } else {
                                Uitility.showToast(getActivity(), "Please enter authorized person name !");
                            }
                        }else {
                            Uitility.showToast(getActivity(), "Please enter your GST No !");
                        }
                    }else {
                        Uitility.showToast(getActivity(), "Please enter your company branch !");
                    }
                }else {
                    Uitility.showToast(getActivity(), "Please enter your company name !");
                }

                break;

            case R.id.imgUserProfile:
            case R.id.imgCompanyProfileCamera:
                gotoSelectPicture();
                break;
        }
    }

    private void updateProfile() {
        viewProgressDialog.showProgress(context);

        RequestBody emailIdPart = RequestBody.create(MultipartBody.FORM, strEmailId);
        RequestBody companyNamePart = RequestBody.create(MultipartBody.FORM, strCompanyName);
        RequestBody branchPart = RequestBody.create(MultipartBody.FORM, strBranch);
        RequestBody gstNoPart = RequestBody.create(MultipartBody.FORM, strGSTNo);
        RequestBody authPersonNamePart = RequestBody.create(MultipartBody.FORM, strAuthorisedName);
        RequestBody designationPart = RequestBody.create(MultipartBody.FORM, strDesignation);

        MultipartBody.Part body;
        if (filepath != null && !filepath.equals("")) {
            File file = new File(filepath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            body = MultipartBody.Part.createFormData("profile_pic", file.getName(), requestBody);
        } else {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            body = MultipartBody.Part.createFormData("profile_pic", "", requestBody);
        }

        ApiInterface apiInterface = RetrofitInstance.getClient();
        Call<CommonResponse> call = apiInterface.updateCompanyProfile(
                emailIdPart,
                companyNamePart,
                branchPart,
                gstNoPart,
                authPersonNamePart,
                designationPart,
                body
        );
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {
                viewProgressDialog.hideDialog();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        Toast.makeText(context, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        ((MainActivity) context).getSupportFragmentManager().popBackStack();
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

                    Glide.with(imgCompanyProfile.getContext())
                            .asBitmap()
                            .load(myBitmap)
                            .into(imgCompanyProfile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
