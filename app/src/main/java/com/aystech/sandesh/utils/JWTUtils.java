package com.aystech.sandesh.utils;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

public class JWTUtils {

    public static String decoded(String JWTEncoded) throws Exception {
        String[] split = new String[0];
        try {
            split = JWTEncoded.split("\\.");
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));
        } catch (UnsupportedEncodingException e) {
            //Error
        }
        return getJson(split[1]);
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }
}
