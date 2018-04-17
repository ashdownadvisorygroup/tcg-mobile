package com.example.ndjat.tcg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ndjat on 24/10/2017.
 */

public class SmsTab2 extends Activity {

    private ListView obj;
    DBHelper mydb;
    EditText searchField;
    ArrayAdapter arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_history);

        mydb = new DBHelper(this);
        ArrayList array_list = mydb.getAllSms();
        Log.e("sms ************", array_list.toString());
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);

        obj = (ListView)findViewById(R.id.listView);
        obj.setTextFilterEnabled(true);
        obj.setAdapter(arrayAdapter);
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                final int id_To_Search = arg2 + 1;

                Cursor rs = mydb.getCall(id_To_Search);
                rs.moveToFirst();

                String[] mylist = new String[6];
                String phone = rs.getString(rs.getColumnIndex(DBHelper.SMS_COLUMN_PHONE));
                mylist[0] = "Numéro : "+phone;
                int message = rs.getInt(rs.getColumnIndex(DBHelper.SMS_COLUMN_MESSAGE));
                mylist[1] = "Message : "+message;
                int delay = rs.getInt(rs.getColumnIndex(DBHelper.SMS_COLUMN_DELAY));
                mylist[2] = "Délai : "+delay;
                String status = rs.getString(rs.getColumnIndex(DBHelper.SMS_COLUMN_STATUS));
                mylist[3] = "Statut : "+status;
                String created = rs.getString(rs.getColumnIndex(DBHelper.SMS_COLUMN_CREATED));
                mylist[4] = "Création : "+created;
                String modified = rs.getString(rs.getColumnIndex(DBHelper.SMS_COLUMN_MODIFIED));
                mylist[5] = "Modification : "+modified;

                if (!rs.isClosed())  {
                    rs.close();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(SmsTab2.this);
                builder.setTitle("Détails du SMS")
                        .setItems(mylist, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                            }
                        })
                        .setCancelable(true)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //CallTab2.this.finish();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Effacer", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteSms(id_To_Search);
                                Toast.makeText(getApplicationContext(), "SMS supprimé",
                                        Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        searchField = (EditText)findViewById(R.id.searchField);
        searchField.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                arrayAdapter.getFilter().filter(arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

}
