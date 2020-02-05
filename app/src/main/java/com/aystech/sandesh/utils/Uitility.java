package com.aystech.sandesh.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Uitility {

    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static String dateFormat(int year, int month, int day){
        String formatedMonth = "", formatedDay = "";

        if(String.valueOf(month).length() == 1){
            formatedMonth = "0"+ month;
        }else {
            formatedMonth = String.valueOf(month+1);
        }

        if(String.valueOf(day).length() ==1){
            formatedDay = "0" + day;
        }else {
            formatedDay = String.valueOf(day);
        }

        return  year + "-" + formatedMonth + "-" + formatedDay;
    }

    public static boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}
