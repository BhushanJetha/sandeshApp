package com.aystech.sandesh.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private TextView tvRegisterHere, tvForgotPassword;
    private EditText etUserName, etPassword;

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

        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
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

                String strUserName, strPassword;

                try{
                    JSONObject jsonObject=new JSONObject();
                    strUserName = etUserName.getText().toString();
                    strPassword = etPassword.getText().toString();

                    if(!strUserName.isEmpty() && !strPassword.isEmpty()){
                        jsonObject.put("mobile_number",strUserName);
                        jsonObject.put("password",strPassword);
                        jsonObject.put("fcm_id","");

                        Log.d("Json Request -> ", jsonObject.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }

                Constants.fragmentType = "Dashboard";
                Intent i = new Intent(LoginActivity.this,   MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
