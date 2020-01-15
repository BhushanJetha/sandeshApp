package com.aystech.sandesh.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.aystech.sandesh.R;

public class IndividualRegistrationActivity extends AppCompatActivity {

    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_registration);

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
                Intent i = new Intent(IndividualRegistrationActivity.this,   AddressDetailActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
