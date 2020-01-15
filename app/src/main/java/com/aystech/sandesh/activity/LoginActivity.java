package com.aystech.sandesh.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.utils.Constants;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView tvRegisterHere, tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        onClick();
    }

    private void init(){
        btnLogin = findViewById(R.id.btnLogin);
        tvRegisterHere = findViewById(R.id.tvRegisterHere);
    }

    private void onClick(){
        tvRegisterHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.fragmentType = "Registration";
                Intent i = new Intent(LoginActivity.this,   MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.fragmentType = "Dashboard";
                Intent i = new Intent(LoginActivity.this,   MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
