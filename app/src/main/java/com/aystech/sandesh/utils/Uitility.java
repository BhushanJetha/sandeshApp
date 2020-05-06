package com.aystech.sandesh.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Pattern;

public class Uitility {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String dateFormat(int year, int month, int day) {
        String formatedMonth = "", formatedDay = "";

        if (String.valueOf(month).length() == 1) {
            int finalMonth = (month + 1);
            if (finalMonth >= 10)
                formatedMonth = "" + (month + 1);
            else
                formatedMonth = "0" + (month + 1);
        } else {
            formatedMonth = String.valueOf(month + 1);
        }

        if (String.valueOf(day).length() == 1) {
            formatedDay = "0" + day;
        } else {
            formatedDay = String.valueOf(day);
        }

        return year + "-" + formatedMonth + "-" + formatedDay;
    }

    public static boolean isValidEmailId(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public static String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
