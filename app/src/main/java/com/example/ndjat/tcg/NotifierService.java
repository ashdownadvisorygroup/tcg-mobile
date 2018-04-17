package com.example.ndjat.tcg;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.BatteryManager;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NotifierService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.ndjat.tcg.action.FOO";
    private static final String ACTION_BAZ = "com.example.ndjat.tcg.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.ndjat.tcg.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.ndjat.tcg.extra.PARAM2";

    public NotifierService() {
        super("NotifierService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, NotifierService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, NotifierService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            float batteryTmp = (float)(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;
            float cpuTmp = MainActivity.getCpuTemp();
            Looper.loop();
            Toast.makeText(NotifierService.this, "LEVEL" + level, Toast.LENGTH_SHORT).show();
            Log.e("Niveau de battérie : ", "-----------------------" + level);
            Toast.makeText(NotifierService.this, "BATTERY TEMPERATURE" + batteryTmp, Toast.LENGTH_SHORT).show();
            Log.e("Température batterie : ", "-----------------------" + batteryTmp);
            Toast.makeText(NotifierService.this, "CPU TEMPERATURE" + cpuTmp, Toast.LENGTH_SHORT).show();
            Log.e("Température du cpu : ", "-----------------------" + cpuTmp);
            new CountDownTimer(5*1000, 500) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    Looper.myLooper().quit();
                }
            }.start();
            /*final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }*/
            // Release the wake lock provided by the WakefulBroadcastReceiver.
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
