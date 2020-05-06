package com.aystech.sandesh.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.aystech.sandesh.R;
import com.aystech.sandesh.model.CommonResponse;
import com.aystech.sandesh.remote.ApiInterface;
import com.aystech.sandesh.remote.RetrofitInstance;
import com.aystech.sandesh.utils.UserSession;
import com.aystech.sandesh.utils.ViewProgressDialog;
import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private String add_amt;
    private UserSession userSession;
    private ViewProgressDialog viewProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        userSession = new UserSession(this);
        viewProgressDialog = ViewProgressDialog.getInstance();

        if (getIntent() != null) {
            add_amt = getIntent().getStringExtra("add_amt");

            if (!TextUtils.isEmpty(add_amt) || !add_amt.equals("")) {
                double totalOrderAmount = Double.parseDouble(add_amt);
                totalOrderAmount = totalOrderAmount * 100;

                Log.e("totalOrderAmount Before", "" + new DecimalFormat("##.##").format(totalOrderAmount));

                startPayment(new DecimalFormat("##.##").format(totalOrderAmount));
            }
        }
    }

    private void startPayment(String add_amt) {
 /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = PaymentActivity.this;
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Sandesh");
            options.put("description", "Aapki Amanat...Apno Tak!");
            options.put("currency", "INR");
            options.put("amount", add_amt);
            options.put("payment_capture", 1);

            JSONObject notes = new JSONObject();
            notes.put("user_id", userSession.getUSER_ID());
            notes.put("user_name", userSession.getUSER_NAME());

            JSONObject preFill = new JSONObject();
            preFill.put("email", userSession.getUSER_EMAIL());
            preFill.put("contact", userSession.getUSER_MOBILE());

            options.put("prefill", preFill);
            options.put("notes", notes);

            co.open(activity, options);
            Log.e("Payment JSON ", " " + options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {

        //TODO API Call
        try {
            viewProgressDialog.showProgress(this);

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("amount", add_amt);
            jsonObject.addProperty("transactionID", razorpayPaymentID);

            ApiInterface apiInterface = RetrofitInstance.getClient();
            Call<CommonResponse> call;
            call = apiInterface.verifyPaymentDetail(
                    jsonObject
            );
            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(@NonNull Call<CommonResponse> call, @NonNull Response<CommonResponse> response) {

                    viewProgressDialog.hideDialog();

                    if (response.body() != null) {
                        Toast.makeText(PaymentActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        finishBackFragment();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<CommonResponse> call, @NonNull Throwable t) {
                    viewProgressDialog.hideDialog();
                }
            });
        } catch (Exception e) {
            finish();
            Log.e("Payment Failure", "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {

        if (code == 0) {
            finishBackFragment();
        } else {
            try {
                Log.e("Payment Failure Code:", " " + code);
                Log.e("Payment Failure Res: ", " " + response);

                Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("Payment Failure", "Exception in onPaymentError", e);
            }
            finishBackFragment();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finishBackFragment();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void finishBackFragment() {
        finish();
    }

    @Override
    public void onBackPressed() {
        finishBackFragment();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Log.e(this.getClass().getName(), "back button pressed");
            finishBackFragment();
        }
        return super.onKeyDown(keyCode, event);
    }
}
