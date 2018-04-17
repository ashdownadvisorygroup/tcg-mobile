package com.example.ndjat.tcg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by ndjat on 16/02/2018.
 */

public class PhoneStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            //System.out.println("Receiver start");
            //Toast.makeText(context," Receiver start ", Toast.LENGTH_SHORT).show();

            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                Toast.makeText(context,"Ringing State Number is -"+incomingNumber,Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {

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

                            context.sendOrderedBroadcast(btnDown, enforcedPerm);
                            context.sendOrderedBroadcast(btnUp, enforcedPerm);
                        }
                    }

                }).start();
            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
                Toast.makeText(context,"Received State",Toast.LENGTH_SHORT).show();
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Toast.makeText(context,"Idle State",Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
