package com.aystech.sandesh.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.aystech.sandesh.R;
import com.aystech.sandesh.activity.MainActivity;
import com.aystech.sandesh.utils.AppController;

/**
 * A simple {@link Fragment} subclass.
 */
public class FAQFragment extends Fragment {

    Context context;

    private WebView webView;

    public FAQFragment() {
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
        View view = inflater.inflate(R.layout.fragment_faq, container, false);

        webView = (WebView) view.findViewById(R.id.wvFAQ);
        // displaying content in WebView from html file that stored in assets folder
        webView.getSettings().setJavaScriptEnabled(true);
        if (AppController.isBaseUrl) {
            webView.loadUrl(AppController.devURL + AppController.frequently_ask_question);
        } else {
            webView.loadUrl(AppController.prodURL + AppController.frequently_ask_question);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) context).setUpToolbar(true, false, "", false);
    }
}
