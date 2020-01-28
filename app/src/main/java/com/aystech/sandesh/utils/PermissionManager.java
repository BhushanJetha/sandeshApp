package com.aystech.sandesh.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.aystech.sandesh.R;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager {
    public static final int RUNTIME_PERMISSION = 23;
    public static AlertDialog dialog;

    // Callbacks to present result to requested activity/fragment
    public interface PermissionListener {

        void onPermissionsGranted(List<String> perms);

        void onPermissionsDenied(List<String> perms);

    }

    /**
     * @param :
     * @return :
     * @throws :
     * @purpose : Check user has a runtime permission or not
     * @Date : 05/09/2017
     * @version : 1.0.0
     * @Change History :
     * @since : 1.0.0
     */
    public static boolean hasPermissions(Context context, String... perms) {

        try {

            for (String perm : perms) {
                if (context != null) {
                    boolean hasPerm = (ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED);
                    if (!hasPerm) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {

        }
        return true;
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @purpose : Request a runtime permission
     * @Date : 05/09/2017
     * @version : 1.0.0
     * @Change History :
     * @since : 1.0.0
     */
    public static void requestPermissions(Object object, PermissionListener listener, String rationale, final String... perms) {
        requestPermissions(object, listener, rationale, android.R.string.ok, android.R.string.cancel, perms);
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @purpose : Logic goes here to request permission
     * @Date : 05/09/2017
     * @version : 1.0.0
     * @Change History :
     * @since : 1.0.0
     */
    public static void requestPermissions(final Object object, final PermissionListener listener, String rationale,
                                          @StringRes int positiveButton,
                                          @StringRes int negativeButton, final String... perms) {

        checkCallingObjectSuitability(object);

        // SharedPreference is used to distinguish between user has "denied" permission or "Never asked" permission
        SharedPreferences mSharedPreferences = getActivity(object).getSharedPreferences("permission_preference", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();

        boolean shouldShowRationale = false;
        for (String perm : perms) {
            shouldShowRationale = shouldShowRationale || shouldShowRequestPermissionRationale(object, perm);
        }

        // Logic goes here for permission rational
        if (shouldShowRationale && !TextUtils.isEmpty(rationale)) {
            executePermissionsRequest(object, perms, RUNTIME_PERMISSION);

        } else { // If permission not rational, means user request permission first time or after "Never asked" optional selected

            // Initialise to calculate user has a "Never asked" option selected or not
            int grantedCount = 0;
            int neverAskedCount = 0;

            ArrayList<String> neveraskedperms = new ArrayList<>();
            for (int i = 0; i < perms.length; i++) {
                String perm = perms[i];

                // Check if user has a requested permission and has a permission rational
                if (!hasPermissions(getActivity(object), perm)) {
                    boolean isRational = shouldShowRequestPermissionRationale(object, perm);
                    if (!isRational) {
                        if (mSharedPreferences.getBoolean(perm, false)) {
                            // If sharedPreference has a TRUE value means user has a "Never asked" permission.
                            neveraskedperms.add(perm);
                            neverAskedCount++; // Count each "Never asked" permission
                        } else {
                            // update flag to TRUE when user request first time.
                            mEditor.putBoolean(perm, true).apply();
                        }
                    }
                } else {
                    grantedCount++; // Count each "Granted" permission count.
                }
            }

            // If sum of "Granted" and "Never asked" count are same as total requested permission,
            // then it tell us to navigate in setting window to enable permission.
            // It means no more default permission dialog available to show user.
            // If sum not match then there are dialog/s to show user. Now request for runtime permission dialog
            if (grantedCount + neverAskedCount == perms.length) {
                // Report "Never asked" permission, if any.
                neverAskedDialog(object, listener, neveraskedperms);
                //listener.onPermissionNeverAsked(neveraskedperms);
            } else {
                executePermissionsRequest(object, perms, RUNTIME_PERMISSION);
            }
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @purpose :Callback which return all granted and ejected permission
     * Logic goes here to distinguish between "Granted" and "Denied" permission
     * @Date : 05/09/2017
     * @version : 1.0.0
     * @Change History :
     * @since : 1.0.0
     */
    public static void onRequestPermissionsResult(Object object, PermissionListener callbacks,
                                                  int requestCode, String[] permissions,
                                                  int[] grantResults) {

        if (requestCode == RUNTIME_PERMISSION) {
            checkCallingObjectSuitability(object);

            // Make a collection of granted and denied permissions from the request.
            ArrayList<String> granted = new ArrayList<>();
            ArrayList<String> denied = new ArrayList<>();

            for (int i = 0; i < permissions.length; i++) {
                String perm = permissions[i];

                if (grantResults.length > 0) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                        granted.add(perm);
                    } else {

                        if (hasPermissions(getActivity(object), perm)) {
                            granted.add(perm);
                        } else {
                            denied.add(perm);
                        }
                    }
                }
            }

            // Report granted permissions, if any.
            if (!granted.isEmpty()) {
                // Notify callbacks
                callbacks.onPermissionsGranted(granted);
            }

            // Report denied permissions, if any.
            if (!denied.isEmpty()) {
                callbacks.onPermissionsDenied(denied);
            }
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @purpose : It checks the permission status
     * @Date : 05/09/2017
     * @version : 1.0.0
     * @Change History :
     * @since : 1.0.0
     */
    private static boolean shouldShowRequestPermissionRationale(Object object, String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return false;
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @purpose : It Display permission Dialog
     * @Date : 05/09/2017
     * @version : 1.0.0
     * @Change History :
     * @since : 1.0.0
     */
    private static void executePermissionsRequest(Object object, String[] perms, int requestCode) {
        checkCallingObjectSuitability(object);

        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(perms, requestCode);
        }
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @purpose : Extract Activity from object
     * @Date : 05/09/2017
     * @version : 1.0.0
     * @Change History :
     * @since : 1.0.0
     */
    private static Activity getActivity(Object object) {
        if (object instanceof Activity) {
            return ((Activity) object);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else {
            return null;
        }
    }

    /**
     * @param : {Object} Any Comonent Object
     * @return :
     * @throws : Illegal Argument Exception
     * @purpose :Make sure Object is an Activity or Fragment
     * @Date : 05/09/2017
     * @version : 1.0.0
     * @Change History :
     * @since : 1.0.0
     */
    private static void checkCallingObjectSuitability(Object object) {
        if (!((object instanceof Fragment) || (object instanceof Activity))) {
            throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
        }
    }


    /**
     * @param :
     * @return :
     * @throws :
     * @purpose : It shows never ask dialog
     * @Date : 05/09/2017
     * @version : 1.0.0
     * @Change History :
     * @since : 1.0.0
     */
    private static void neverAskedDialog(final Object object, final PermissionListener listener, final ArrayList<String> neveraskedperms) {
        dialog = new AlertDialog.Builder(getActivity(object), R.style.MyAlertDialogStyle)
                .setTitle("Required Permissions")
                .setMessage("This app require permission to use awesome feature. Grant them in app settings.")
                .setCancelable(false)
                .setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        openSetting(object);
                    }
                }).create();
        dialog.show();
    }

    /**
     * @param :
     * @return :
     * @throws :
     * @purpose : It redirects to permission setting of current Application
     * @Date : 05/09/2017
     * @version : 1.0.0
     * @Change History :
     * @since : 1.0.0
     */
    private static void openSetting(final Object object) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", (getActivity(object).getPackageName()), null);
        intent.setData(uri);
        getActivity(object).startActivityForResult(intent, RUNTIME_PERMISSION);
    }
}
