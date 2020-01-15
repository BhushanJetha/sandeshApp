package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.R;
import com.aystech.sandesh.utils.Constants;
import com.aystech.sandesh.utils.FragmentUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class MobileVerificationFragment extends Fragment {

    Context context;
    private LoginFragment loginFragment;
    private Button btnSubmit;
    private RadioButton rbIndividual, rbCorporate;
    private EditText etMobileNumber;

    public MobileVerificationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_mobile_verification, container, false);

        loginFragment = (LoginFragment) Fragment.instantiate(context, LoginFragment.class.getName());

        initView(view);

        onClickListener();

        return view;
    }

    private void initView(View view) {
        btnSubmit = view.findViewById(R.id.btnSubmit);
        rbIndividual = view.findViewById(R.id.rbIndividual);
        rbCorporate = view.findViewById(R.id.rbCorporate);
        etMobileNumber = view.findViewById(R.id.etMobileNumber);
    }

    private void onClickListener() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMobileNumber;

                try{
                    JSONObject jsonObject=new JSONObject();
                    strMobileNumber = etMobileNumber.getText().toString();

                    if(!strMobileNumber.isEmpty()  && !Constants.userType.isEmpty()){
                        jsonObject.put("mobileNumber",strMobileNumber);
                        jsonObject.put("userType",Constants.userType);

                        Log.d("Json Request -> ", jsonObject.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }


                FragmentUtil.commonMethodForFragment(((MainActivity) context).getSupportFragmentManager(),
                        loginFragment, R.id.frame_container, true);
            }
        });

        rbIndividual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.userType = "Individual";
            }
        });

        rbCorporate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.userType = "Corporate";
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(false, false,"", false);
    }
}
