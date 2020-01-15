package com.aystech.sandesh.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.aystech.sandesh.R;

public class IndividualRegistrationActivity extends AppCompatActivity {

    private EditText etFirstName, etMiddleName, etLastName, etEmailId, etPassword, etReEnteredPassword, etRefferalCode;
    private RadioButton rbMale, rbFemale, rbOther;
    private CheckBox cbAccetTermsAndConditions;
    private TextView tvBirthDate;
    private ImageView ivProfilePicture;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_registration);

        init();
        onClick();
    }

    private void init(){

        etFirstName = findViewById(R.id.etFirstName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etLastName = findViewById(R.id.etLastName);
        etEmailId = findViewById(R.id.etEmailId);
        etPassword = findViewById(R.id.etPassword);
        etReEnteredPassword = findViewById(R.id.etReEnteredPassword);
        etRefferalCode = findViewById(R.id.etReferalCode);

        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbOther = findViewById(R.id.rbOther);

        cbAccetTermsAndConditions = findViewById(R.id.cbTermsCondition);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        ivProfilePicture = findViewById(R.id.imgProfilePicture);
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
