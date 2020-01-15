package com.aystech.sandesh.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aystech.sandesh.R;
import com.aystech.sandesh.utils.Constants;

public class AddressDetailActivity extends AppCompatActivity {

    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);

        init();
        onClick();

    }

    private void init(){
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void onClick(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.fragmentType = "Dashboard";
                Intent i = new Intent(AddressDetailActivity.this,   MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
