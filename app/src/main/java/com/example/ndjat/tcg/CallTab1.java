package com.example.ndjat.tcg;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by ndjat on 23/10/2017.
 */

public class CallTab1  extends Activity
{
    private DBHelper mydb ;
    private long id;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_form);

        mydb = new DBHelper(this);

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                MakeCall.acceptCall(CallTab1.this);
            }
        }).start();*/

        /*new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Runtime.getRuntime().exec("input keyevent " +
                            Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));
                } catch (IOException e) {
                    // Runtime.exec(String) had an I/O problem, try to fall back
                    String enforcedPerm = "android.permission.CALL_PRIVILEGED";
                    Intent btnDown = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                            Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN,
                                    KeyEvent.KEYCODE_HEADSETHOOK));
                    Intent btnUp = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
                            Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP,
                                    KeyEvent.KEYCODE_HEADSETHOOK));

                    CallTab1.this.sendOrderedBroadcast(btnDown, enforcedPerm);
                    CallTab1.this.sendOrderedBroadcast(btnUp, enforcedPerm);
                }
            }

        }).start();*/

        /*try {
            Process proc = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(proc.getOutputStream());

            os.writeBytes("service call phone 5\n");
            os.flush();

            os.writeBytes("exit\n");
            os.flush();

            if (proc.waitFor() == 255) {
                // TODO handle being declined root access
                // 255 is the standard code for being declined root for SU
            }
        } catch (IOException e) {
            // TODO handle I/O going wrong
            // this probably means that the device isn't rooted
        } catch (InterruptedException e) {
            // don't swallow interruptions
            Thread.currentThread().interrupt();
        }*/

        /////////////////////////////////////////////////////////

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                EditText phoneText = (EditText)findViewById(R.id.editText);
                EditText durationText = (EditText)findViewById(R.id.editText3);
                EditText delayText = (EditText)findViewById(R.id.editText4);
                TextView textView = (TextView)findViewById(R.id.textView3);

                String phone = phoneText.getText().toString();
                int duration = Integer.valueOf(durationText.getText().toString());
                int delay = Integer.valueOf(delayText.getText().toString());

                id = mydb.insertCall(phone, duration, delay, "registered", "outgoing");
                if(id != -1){
                    Toast.makeText(getApplicationContext(), "Appel enrégistré avec succès!",
                            Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getApplicationContext(), "Echec de sauvegarde de l'appel!",
                            Toast.LENGTH_SHORT).show();
                }

                this.launchTimer(delay, textView, phone, duration, CallTab1.this);

                /*Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(CallTab1.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);*/

            }

            private void launchTimer(final int delay, final TextView textView, final String phone, final int duration, final Context context) {

                new CountDownTimer(delay*1000, 500) {

                    public void onTick(long millisUntilFinished) {
                        textView.setText("Appel du " + phone + " dans : " + (millisUntilFinished / 1000) + "s");
                    }

                    public void onFinish() {
                        textView.setText("Lancement de l'appel!");
                        final MakeCall call = new MakeCall(phone, delay, duration, context);
                        call.call();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Do something after duration in seconds
                                try {
                                    call.endcall();
                                    if(mydb.updateCall((int) id, "done")){
                                        Toast.makeText(getApplicationContext(), "Appel enrégistré avec succès!",
                                                Toast.LENGTH_SHORT).show();
                                    } else{
                                        Toast.makeText(getApplicationContext(), "Echec de sauvegarde de l'appel!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                } catch (NoSuchMethodException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, duration*1000);
                    }
                }.start();


            }


        });




    }
}
