package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.utils.AppController;
import com.bumptech.glide.Glide;

public class ZoomImageFragment extends BottomSheetDialogFragment {

    private static final String TAG = "ZoomImageFragment";
    private Context context;

    ImageView img, imgCLose;

    String imgUrl;

    public ZoomImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static ZoomImageFragment newInstance(String url) {
        ZoomImageFragment fragment = new ZoomImageFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zoom_image, container, false);

        if (getArguments() != null){
            imgUrl = getArguments().getString("url");
        }
        Log.e(TAG, "onCreateView: " +imgUrl);
        initView(view);

        return view;
    }

    private void initView(View view) {
        img = view.findViewById(R.id.img);
        imgCLose = view.findViewById(R.id.imgCLose);

        Glide.with(context)
                .load(imgUrl)
                .error(R.drawable.ic_logo_sandesh)
                .into(img);
        imgCLose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}