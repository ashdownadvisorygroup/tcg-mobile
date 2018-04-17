package com.example.ndjat.tcg;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import static android.content.ContentValues.TAG;

/**
 * Created by ndjat on 03/11/2017.
 */

public class DataTab1 extends Activity  {

    Integer clicked = 0;

    // Progress Dialog Object
    private ProgressDialog prgDialog;
    // Progress Dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;
    // Music resource URL
    //private static String file_url = "<a class="vglnk" href="http://programmerguru.com/android-tutorial/wp-content/uploads/2014/01/jai_ho.mp3" rel="nofollow"><span>http</span><span>://</span><span>programmerguru</span><span>.</span><span>com</span><span>/</span><span>android</span><span>-</span><span>tutorial</span><span>/</span><span>wp</span><span>-</span><span>content</span><span>/</span><span>uploads</span><span>/</span><span>2014</span><span>/</span><span>01</span><span>/</span><span>jai</span><span>_</span><span>ho</span><span>.</span><span>mp3</span></a>";



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_form);

        clicked = 0;
        Log.e("message", "Dans la fonction");
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(clicked == 0){
                    setMobileDataState(true);
                    clicked = 1;
                }
                else{
                    setMobileDataState(false);
                    clicked = 0;
                }*/
                EditText delayText = (EditText) findViewById(R.id.editText4);
                int delay = Integer.parseInt(delayText.getText().toString());
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        download();
                    }
                }, delay);*/
                TextView textView = (TextView)findViewById(R.id.textView3);
                String url = "http://ipv4.download.thinkbroadband.com/10MB.zip";
                int size = 10;
                launchTimer(delay, textView, url, size, DataTab1.this);
            }
        });

    }

    public void download(){
        EditText urlText = (EditText) findViewById(R.id.editText);
        String url = urlText.getText().toString();
        EditText pathText = (EditText) findViewById(R.id.editText3);
        String addPath = pathText.getText().toString();
        url = "http://ipv4.download.thinkbroadband.com/10MB.zip";
        String PATH = Environment.getExternalStorageDirectory().toString()
                + "/"+addPath;
        Log.e("Chemin ***************", PATH);
        File outputFile = new File(PATH);

        //DataTab1.downloadFile(url, outputFile);

        // declare the dialog as a member field of your activity
        ProgressDialog mProgressDialog;

// instantiate it within the onCreate method
        mProgressDialog = new ProgressDialog(DataTab1.this);
        mProgressDialog.setMessage("Downloading ...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

// execute this when the downloader must be fired
        final DownloadTask downloadTask = new DownloadTask(DataTab1.this, mProgressDialog);
        //downloadTask.execute(url, PATH);
        new DownloadfromInternet().execute(url);


        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });

    }

    public void setMobileDataState(boolean mobileDataEnabled)
    {
        try
        {
            TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

            if (null != setMobileDataEnabledMethod)
            {
                setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Error setting mobile data state", ex);
        }
    }

    public boolean getMobileDataState()
    {
        try
        {
            TelephonyManager telephonyService = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            Method getMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("getDataEnabled");

            if (null != getMobileDataEnabledMethod)
            {
                boolean mobileDataEnabled = (Boolean) getMobileDataEnabledMethod.invoke(telephonyService);

                return mobileDataEnabled;
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Error getting mobile data state", ex);
        }

        return false;
    }

    private static int downloadFile(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
            return 0;
        } catch(FileNotFoundException e) {
            return 404; // swallow a 404
        } catch (IOException e) {
            return 404; // swallow a 404
        }
    }


    // Show Dialog Box with Progress bar
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                prgDialog = new ProgressDialog(this);
                prgDialog.setMessage("Downloading Mp3 file. Please wait...");
                prgDialog.setIndeterminate(false);
                prgDialog.setMax(100);
                prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                prgDialog.setCancelable(false);
                prgDialog.show();
                return prgDialog;
            default:
                return null;
        }
    }

    // Async Task Class
    class DownloadfromInternet extends AsyncTask<String, String, String> {
        // Show Progress bar before
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            showDialog(progress_bar_type);
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
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/jai_ho.mp3");
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
            prgDialog.setProgress(Integer.parseInt(progress[0]));
        }

        // Once File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Music file was downloaded
            dismissDialog(progress_bar_type);
            Toast.makeText(getApplicationContext(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            //playMusic();
        }
    }



    // usually, subclasses of AsyncTask are declared inside the activity class.
// that way, you can easily modify the UI thread from here
    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;
        private ProgressDialog dialog;

        public DownloadTask(Context context, ProgressDialog dialog) {
            this.context = context;
            this.dialog = dialog;
        }

        @Override
        protected void onPreExecute() {
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                //output = new FileOutputStream("/sdcard/file_name.extension");
                output = new FileOutputStream(sUrl[1]);
                Log.e(" output ****", "output"+sUrl[1]);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) {
                        // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                        Log.e("Progress : ", String.valueOf((int) (total * 100 / fileLength)));
                    }

                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }

        /*protected void onProgressUpdate(Integer... progress) {
            setProgressPercent(progress[0]);
        }*/

        protected void onPostExecute(Void result) {
            // do UI work here
            if (this.dialog.isShowing()) {
                this.dialog.dismiss();
            }
        }

    }


    private void launchTimer(final int delay, final TextView textView, final String url, final int size, final Context context) {

        new CountDownTimer(delay*1000, 500) {

            public void onTick(long millisUntilFinished) {
                textView.setText("Téléchargement de " + size + "MB dans : " + (millisUntilFinished / 1000) + "s");
            }

            public void onFinish() {
                textView.setText("Téléchargement de " + size + "MB!");
                final MakeData data = new MakeData(context);
                data.download();
            }
        }.start();


    }

}
