package com.aystech.sandesh.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.utils.UserSession;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReferFriendFragment extends Fragment {

    Context context;

    EditText etReferralCode;
    Button btnReferNow;

    UserSession userSession;

    public ReferFriendFragment() {
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
        View view = inflater.inflate(R.layout.fragment_refer_friend, container, false);

        initView(view);

        clickListener();

        return view;
    }

    private void initView(View view) {
        userSession = new UserSession(context);

        etReferralCode = view.findViewById(R.id.etReferralCode);
        if (userSession.getReferralCode() != null &&
                !userSession.getReferralCode().equals("")) {
            etReferralCode.setText("" + userSession.getReferralCode());
        } else {
            etReferralCode.setText("-");
        }
        btnReferNow = view.findViewById(R.id.btnReferNow);
    }

    private void clickListener() {
        btnReferNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(
                        Intent.EXTRA_TEXT, "Earn While You Travel / Send Your Parcels @ lowest charges. Use My Referral Code " + userSession.getReferralCode() + " when your register on SANDESH. Register for FREE. T & C applied.");
                startActivity(Intent.createChooser(shareIntent, "send to"));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
