package com.aystech.sandesh.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.aystech.sandesh.R;

public class TermsAndConditionFragment extends Fragment {

    private WebView webView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_terms_and_condition, container, false);


        webView = (WebView) view.findViewById(R.id.webView);
        // displaying content in WebView from html file that stored in assets folder
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_res/raw/" + "guidelines_and_policies.html");
        return  view;
    }
}
