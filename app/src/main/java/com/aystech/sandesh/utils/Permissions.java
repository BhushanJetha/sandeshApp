package com.aystech.sandesh.utils;

import android.Manifest;

public class Permissions {
    public static final String[] STORAGE = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
}
