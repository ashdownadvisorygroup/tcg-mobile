package com.example.ndjat.tcg;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ndjat on 16/02/2018.
 */

public class MakeData{

    private String url = "http://ipv4.download.thinkbroadband.com/";
    private int delay;
    private int size;
    private Context context;

    public static final int progress_bar_type = 0;
    // Progress Dialog Object
    private ProgressDialog prgDialog;

    public MakeData(String url, int delay, int size, Context context) {
        this.url = url;
        this.delay = delay;
        this.size = size;
        this.context = context;
    }

    public MakeData(String url, Context context) {
        this.url = url;
        this.delay = 0;
        this.size = 10;
        this.context = context;
    }

    public MakeData(Context context) {
        this.url = this.url + "10MB.zip";
        this.delay = 0;
        this.size = 10;
        this.context = context;
    }

    public MakeData(String url, int size, Context context) {
        this.url = url;
        this.size = size;
        this.delay = 0;
        this.context = context;
    }

    public MakeData(int size, Context context) {
        this.url = this.url + size + "MB.zip";
        this.size = size;
        this.delay = 0;
        this.context = context;
    }

    public void download() {
        new DownloadfromInternet(this.context).execute(this.url);
    }

    // Async Task Class
    class DownloadfromInternet extends AsyncTask<String, String, String> {

        private Context context;
        private ProgressDialog prgDialog;

        public DownloadfromInternet(Context context) {
            this.context = context;
            this.prgDialog = new ProgressDialog(this.context);
        }

        // Show Progress bar before
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);
            this.prgDialog = new ProgressDialog(this.context);
            this.prgDialog.setMessage("Downloading file. Please wait...");
            this.prgDialog.setIndeterminate(false);
            this.prgDialog.setMax(100);
            this.prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.prgDialog.setCancelable(false);
            this.prgDialog.show();
        }

        // Download File from Internet
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // Get file length
                int lenghtOfFile = conection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(),10*1024);
                // Output stream to write file in SD card
                DateFormat df = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
                String filename = df.format(new Date());
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+ "/" + filename + ".zip");
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress which triggers onProgressUpdate method
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    // Write data to file
                    output.write(data, 0, count);
                }
                // Flush output
                output.flush();
                // Close streams
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        // While Downloading Music File
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            this.prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        // Once File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Music file was downloaded
            //dismissDialog(progress_bar_type);
            if(this.prgDialog.isShowing()){
                this.prgDialog.dismiss();
            }
            Toast.makeText(this.context, "Téléchargement de 10MB terminé", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();
        }
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getsize() {
        return size;
    }

    public void setsize(int size) {
        this.size = size;
    }

}
