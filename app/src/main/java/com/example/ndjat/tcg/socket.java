package com.example.ndjat.tcg;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import com.github.nkzawa.emitter.Emitter;


/**
 * Created by ndjat on 15/02/2018.
 */

public class socket extends AppCompatActivity{

    private Socket mSocket;
    private String serverUrl;
    private Emitter.Listener onNewMessage;

    public void socket(){
        try {
            this.mSocket = IO.socket("http://chat.socket.io");
        } catch (URISyntaxException e) {}
    }

    public void socket(String serverUrl){
        this.serverUrl = serverUrl;
        try {
            this.mSocket = IO.socket(this.serverUrl);
        } catch (URISyntaxException e) {}

        this.onNewMessage = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                /*getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        String username;
                        String message;
                        try {
                            username = data.getString("username");
                            message = data.getString("message");
                        } catch (JSONException e) {
                            return;
                        }

                        // add the message to view
                        addMessage(username, message);
                    }
                });*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        String username;
                        String message;
                        try {
                            username = data.getString("username");
                            message = data.getString("message");
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
    }

    private void attemptSend(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }

        mSocket.emit("new message", message);

        Context context = getApplicationContext();
        CharSequence text = "Message envoyé avec succès!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
