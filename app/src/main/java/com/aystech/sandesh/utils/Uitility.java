package com.aystech.sandesh.utils;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class Uitility {

    public static String journey = "";

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

    public static long calculateDateDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        return different / daysInMilli;
    }

    public static boolean showDatePickerWithConditionalDate(DatePickerDialog mDate, String tag,Context context,
                                                            String strStartDate, int year, int month, int day){
        boolean flag = true;
        if(tag.equals("start_date")){
            mDate.show();
            flag = true;
        }
        if(tag.equals("end_date")){
            if(!strStartDate.isEmpty()){
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);//Year,Mounth -1,Day
                mDate.getDatePicker().setMinDate(c.getTimeInMillis());
                mDate.getDatePicker().setMaxDate(System.currentTimeMillis());
                mDate.show();
                flag = true;
            }
            else {
                showToast(context, "Please select start date first");
                flag = false;
            }
        }
        return flag;

    }

    public static boolean showDatePickerWithConditionalDateForOrders(DatePickerDialog mDate, String tag,Context context,
                                                            String strStartDate, int year, int month, int day){
        boolean flag = true;
        if(tag.equals("start_date")){
            mDate.show();
            flag = true;
        }
        if(tag.equals("end_date")){
            if(!strStartDate.isEmpty()){
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);//Year,Mounth -1,Day
                mDate.getDatePicker().setMinDate(c.getTimeInMillis());

                mDate.show();
                flag = true;
            }
            else {
                showToast(context, "Please select start date first");
                flag = false;
            }
        }
        return flag;

    }

}
