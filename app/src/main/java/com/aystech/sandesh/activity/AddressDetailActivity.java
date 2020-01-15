package com.aystech.sandesh.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.aystech.sandesh.R;
import com.aystech.sandesh.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class AddressDetailActivity extends AppCompatActivity {

    private Button btnSubmit;
    private Spinner spState, spCity;
    private EditText etAddressLine1, etAddressLine2, etLandmark, etPincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_detail);

        init();
        onClick();

    }

    private void init(){

        spState = findViewById(R.id.spState);
        spCity = findViewById(R.id.spCity);
        etAddressLine1 = findViewById(R.id.etAddressLine1);
        etAddressLine2 = findViewById(R.id.etAddressLine2);
        etLandmark = findViewById(R.id.etLandmark);
        etPincode = findViewById(R.id.etPincode);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    private void onClick(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strState, strCity, strAddressLine1, strAddressLine2, strLandmark, strPincode;

                try{

                    strAddressLine1 = etAddressLine1.getText().toString();
                    strAddressLine2 = etAddressLine2.getText().toString();
                    strLandmark = etLandmark.getText().toString();
                    strPincode = etPincode.getText().toString();

                    JSONObject jsonObject=new JSONObject();

                    if(!strAddressLine1.isEmpty() && !strLandmark.isEmpty() && !strPincode.isEmpty()){
                        jsonObject.put("stateId","");
                        jsonObject.put("cityId","");
                        jsonObject.put("addressLine1",strAddressLine1);
                        jsonObject.put("addressLine2",strAddressLine2);
                        jsonObject.put("landmark",strLandmark);
                        jsonObject.put("pincode",strPincode);

                        Log.d("Json Request -> ", jsonObject.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }

                Constants.fragmentType = "Dashboard";
                Intent i = new Intent(AddressDetailActivity.this,   MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
