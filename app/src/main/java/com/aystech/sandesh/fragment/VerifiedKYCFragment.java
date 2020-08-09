package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.model.AadhaarDetailsResponseModel;
import com.bumptech.glide.Glide;

public class VerifiedKYCFragment extends BottomSheetDialogFragment {

    private Context context;

    private String kycStatus, aadhaarNumber, fullName;

    private ImageView imgCLose, imgKYCStatus;
    private TextView tvAadhaarNo, tvFullName, tvKYCStatus;

    public static VerifiedKYCFragment newInstance(AadhaarDetailsResponseModel aadhaarDetailsModel) {
        VerifiedKYCFragment bottomSheetFragment = new VerifiedKYCFragment();
        Bundle bundle = new Bundle();
        bundle.putString("kyc_status", aadhaarDetailsModel.getMessage());
        bundle.putString("aadhaar_no", aadhaarDetailsModel.getData().getAadhaarNumber());
        bundle.putString("full_name", aadhaarDetailsModel.getData().getFullName());
        bottomSheetFragment.setArguments(bundle);
        return bottomSheetFragment;
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
        View view = inflater.inflate(R.layout.fragment_verified_k_y_c, container, false);

        if (getArguments() != null) {
            kycStatus = getArguments().getString("kyc_status");
            aadhaarNumber = getArguments().getString("aadhaar_no");
            fullName = getArguments().getString("full_name");
        }

        initView(view);

        return view;
    }

    private void initView(View view) {
        imgKYCStatus = view.findViewById(R.id.imgKYCStatus);
        imgCLose = view.findViewById(R.id.imgCLose);
        tvAadhaarNo = view.findViewById(R.id.tvAadhaarNo);
        tvFullName = view.findViewById(R.id.tvFullName);
        tvKYCStatus = view.findViewById(R.id.tvKYCStatus);

        Glide.with(context)
                .asGif()
                .load(R.drawable.ic_kyc_success)
                .into(imgKYCStatus);
        tvKYCStatus.setText(kycStatus);
        tvAadhaarNo.setText("Aadhaar Number - " + aadhaarNumber);
        tvFullName.setText("Full Name - " + fullName);

        imgCLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}