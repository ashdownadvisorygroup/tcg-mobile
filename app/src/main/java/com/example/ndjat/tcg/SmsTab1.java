package com.example.ndjat.tcg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by ndjat on 24/10/2017.
 */

public class SmsTab1 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_form);

        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                EditText phoneText = (EditText)findViewById(R.id.editText5);
                EditText smsText = (EditText)findViewById(R.id.editText7);
                EditText delayText = (EditText)findViewById(R.id.editText8);
                TextView textView = (TextView)findViewById(R.id.textView6);

                String phone = phoneText.getText().toString();
                String mysms = smsText.getText().toString();
                int delay = Integer.valueOf(delayText.getText().toString());

                this.launchTimer(delay, textView, phone, mysms, SmsTab1.this);

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

            private void launchTimer(final int delay, final TextView textView, final String phone, final String mysms, final Context context) {

                new CountDownTimer(delay*1000, 500) {

                    public void onTick(long millisUntilFinished) {
                        textView.setText("Appel du " + phone + " dans : " + (millisUntilFinished / 1000) + "s");
                    }

                    public void onFinish() {
                        textView.setText("Envoie du SMS!");
                        final MakeSms sms = new MakeSms(phone, delay, mysms, context);
                        sms.send();
                    }
                }.start();


            }


        });




    }

}
