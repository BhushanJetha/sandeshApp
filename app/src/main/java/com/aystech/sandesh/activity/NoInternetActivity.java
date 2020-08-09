package com.aystech.sandesh.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.utils.Connectivity;
import com.bumptech.glide.Glide;

public class NoInternetActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private Button btnRetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);

        initView();

        onClick();
    }

    private void initView() {
        imageView = findViewById(R.id.imageView);
        btnRetry = findViewById(R.id.btnRetry);

        Glide.with(this)
                .asGif()
                .load(R.drawable.ic_no_internet)
                .into(imageView);
    }

    private void onClick(){
        btnRetry.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {
            if (Connectivity.isConnected(NoInternetActivity.this)) {
                finish();
            }
        }
    }
}