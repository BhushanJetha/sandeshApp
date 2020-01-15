package com.aystech.sandesh.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ImageView;

import com.aystech.sandesh.R;
import com.bumptech.glide.Glide;

public class ViewProgressDialog {

    private static ViewProgressDialog viewProgressDialog;
    private Dialog m_Dialog;

    private ViewProgressDialog() {
    }

    public static ViewProgressDialog getInstance() {
        if (viewProgressDialog == null) {
            viewProgressDialog = new ViewProgressDialog();
        }
        return viewProgressDialog;
    }

    public void showProgress(Context m_Context) {
        m_Dialog = new Dialog(m_Context);
        m_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        m_Dialog.setCancelable(false);
        m_Dialog.setContentView(R.layout.custom_loading_layout);

        ImageView gifImageView = m_Dialog.findViewById(R.id.custom_loading_imageView);

        Glide.with(m_Context)
                .asGif()
                .load(R.drawable.ic_loader)
                .into(gifImageView);

        m_Dialog.show();
    }

    public void hideDialog() {
        m_Dialog.dismiss();
    }
}
