package com;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.ndjat.tcg.NotifierService;

public class BootBroadcastReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Intent startServiceIntent = new Intent(context, NotifierService.class);
        startWakefulService(context, startServiceIntent);
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
