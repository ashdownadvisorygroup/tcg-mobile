package com.example.ndjat.tcg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    public static final String ACTION = "com.example.ndjat.tcg";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent i = new Intent(context, NotifierService.class);
        i.putExtra("foo", "bar");
        context.startService(i);
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
