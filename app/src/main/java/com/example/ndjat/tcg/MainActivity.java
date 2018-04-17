package com.example.ndjat.tcg;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //final Handler mHandler = new Handler();

    public Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://172.20.10.3:3333");
        } catch (URISyntaxException e) {}
    }

    public Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                        Log.e("username", username);
                        Log.e("message", message);
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    //addMessage(username, message);

                    Context context = getApplicationContext();
                    CharSequence text = "username : " + username + " || message : " + message;
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });
        }
    };

    public Emitter.Listener trafficHandler = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String traffic;
                    try {
                        traffic = data.getString("traffic");

                        if(traffic.equals("voice")){
                            String phone = data.getString("phone");
                            int duration = data.getInt("duration");
                            int delay = data.getInt("delay");
                            TextView textView = (TextView)findViewById(R.id.textView7);
                            new Thread(new Runnable() {
                                public void run() {
                                    // a potentially  time consuming task

                                }
                            }).start();
                            launchTimerCall(delay, textView, phone, duration, MainActivity.this);
                        } else if (traffic.equals("sms")){

                            String phone = data.getString("phone");
                            String mysms = data.getString("sms");
                            int delay = data.getInt("delay");
                            TextView textView = (TextView)findViewById(R.id.textView7);
                            new Thread(new Runnable() {
                                public void run() {
                                    // a potentially  time consuming task

                                }
                            }).start();
                            launchTimerSms(delay, textView, phone, mysms, MainActivity.this);
                        } else if (traffic.equals("data")){

                            String url = data.getString("url");
                            int size = data.getInt("size");
                            int delay = data.getInt("delay");
                            TextView textView = (TextView)findViewById(R.id.textView7);
                            new Thread(new Runnable() {
                                public void run() {
                                    // a potentially  time consuming task

                                }
                            }).start();
                            launchTimerData(delay, textView, url, size, MainActivity.this);
                        } else {

                        }

                        Log.e("traffic", traffic);
                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    //addMessage(username, message);

                    Context context = getApplicationContext();
                    CharSequence text = "traffic : " + traffic;
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            });
        }
    };

    private void attemptSend(String socket, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        mSocket.emit(socket, message);
        Log.e("emit", "Emit successfull !!!!!!!!!!!!!!!!!!!");

        Context context = getApplicationContext();
        CharSequence text = "Evènement <<" + message + ">> envoyé avec succès!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSocket.on("new message", onNewMessage);
        mSocket.on("traffic", trafficHandler);
        mSocket.connect();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*Intent BATTERYintent = this.registerReceiver(null, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        int level = BATTERYintent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        float temperature = (float)(BATTERYintent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;
        Toast.makeText(MainActivity.this, "LEVEL" + level, Toast.LENGTH_SHORT).show();
        Toast.makeText(MainActivity.this, "TEMPERATURE" + MainActivity.getCpuTemp(), Toast.LENGTH_SHORT).show();*/

        //scheduleAlarm();
        // Create the Handler object (on the main thread by default)
        final Handler handler = new Handler();
        // Define the code block to be executed
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                //Log.d("Handlers", "Called on main thread");
                Intent BATTERYintent = MainActivity.this.registerReceiver(null, new IntentFilter(
                        Intent.ACTION_BATTERY_CHANGED));
                int level = BATTERYintent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                float batteryTmp = (float)(BATTERYintent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10;
                float cpuTmp = MainActivity.getCpuTemp();
                Toast.makeText(MainActivity.this, "LEVEL : " + level, Toast.LENGTH_SHORT).show();
                Log.e("Niveau de battérie : ", "-----------------------" + level);
                attemptSend("battery level", ""+level);
                Toast.makeText(MainActivity.this, "BATTERY TEMPERATURE : " + batteryTmp, Toast.LENGTH_SHORT).show();
                Log.e("Température batterie : ", "-----------------------" + batteryTmp);
                attemptSend("battery temperature", ""+batteryTmp);
                Toast.makeText(MainActivity.this, "CPU TEMPERATURE : " + cpuTmp, Toast.LENGTH_SHORT).show();
                Log.e("Température du cpu : ", "-----------------------" + cpuTmp);
                attemptSend("cpu temperature", ""+cpuTmp);
                // Repeat this the same runnable code block again another 2 seconds
                // 'this' is referencing the Runnable object
                handler.postDelayed(this, 10000);
            }
        };
        // Start the initial runnable task by posting through the handler
        handler.post(runnableCode);


        //Log.v(null, "LEVEL" + level);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        Button call = (Button) findViewById(R.id.button3);
        Button sms = (Button) findViewById(R.id.button4);
        Button data = (Button) findViewById(R.id.button5);

        call.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click

                attemptSend("call request", "test");

                Context context = getApplicationContext();
                CharSequence text = "Call button pressed";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        });

        sms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click

                attemptSend("sms request", "test");

                Context context = getApplicationContext();
                CharSequence text = "SMSbutton pressed";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        });

        data.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click

                attemptSend("data request", "test");

                Context context = getApplicationContext();
                CharSequence text = "Data button pressed";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            // Handle the camera action
            Intent searchIntent = new Intent(MainActivity.this, History.class);
            Log.e("Entrée : ", "Nous sommes entrés dans l'intent");
            startActivity(searchIntent);
            Log.e("Entrée : ", "Activité dérangée");
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_call) {
            Intent searchIntent = new Intent(MainActivity.this, Call.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_sms) {
            Intent searchIntent = new Intent(MainActivity.this, Sms.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else if (id == R.id.nav_data) {
            Intent searchIntent = new Intent(MainActivity.this, Data.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        else if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void launchTimerCall(final int delay, final TextView textView, final String phone, final int duration, final Context context) {

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

    private void launchTimerSms(final int delay, final TextView textView, final String phone, final String mysms, final Context context) {

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

    private void launchTimerData(final int delay, final TextView textView, final String url, final int size, final Context context) {

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

    public static float getCpuTemp() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = reader.readLine();
            float temp = Float.parseFloat(line) / 1000.0f;

            return temp;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    // Setup a recurring alarm every half hour
    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every every half hour from this point onwards
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES, pIntent);
    }

    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

}
