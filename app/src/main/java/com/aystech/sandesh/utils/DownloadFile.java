package com.aystech.sandesh.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadFile extends Activity {

    TextView tv_loading;
    String dest_file_path;
    int downloadedSize = 0, totalsize;
    String download_file_url;
    float per = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dest_file_path = getIntent().getExtras().getString("invoice_name");
        download_file_url = getIntent().getExtras().getString("path");

        tv_loading = new TextView(this);
        setContentView(tv_loading);
        tv_loading.setGravity(Gravity.CENTER);
        tv_loading.setTypeface(null, Typeface.BOLD);

        downloadAndOpenPDF();
    }

    void downloadAndOpenPDF() {
        new Thread(new Runnable() {
            public void run() {
                File file = downloadFile(download_file_url);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    // New Approach
                    Uri apkURI = FileProvider.getUriForFile(
                            DownloadFile.this,
                            getApplicationContext()
                                    .getPackageName() + ".provider", file);
                    intent.setDataAndType(apkURI, "application/pdf");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    // End New Approach
                    startActivity(intent);
                    finish();
                } catch (ActivityNotFoundException e) {
                    tv_loading
                            .setError("PDF Reader application is not installed in your device");
                }
            }
        }).start();

    }

    File downloadFile(String dwnload_file_path) {
        File file = null;
        try {

            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

            // set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            file = new File(SDCardRoot, dest_file_path);

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // downloading
            totalsize = urlConnection.getContentLength();
            setText("Starting PDF download...");

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;
                setText("Total PDF File size  : "
                        + (totalsize / 1024)
                        + " KB\n\nDownloading PDF " + (int) per
                        + "% complete");
            }
            // close the output stream when complete //
            fileOutput.close();
            setText("Download Complete. Open PDF Application installed in the device.");

        } catch (final MalformedURLException e) {
            setTextError("Some error occured. Press back and try again."
            );
        } catch (final IOException e) {
            setTextError("Some error occured. Press back and try again."
            );
        } catch (final Exception e) {
            setTextError(
                    "Failed to download image. Please check your internet connection."
            );
        }
        return file;
    }

    void setTextError(final String message) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setTextColor(Color.RED);
                tv_loading.setText(message);
            }
        });

    }

    void setText(final String txt) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setText(txt);
            }
        });

    }
}
