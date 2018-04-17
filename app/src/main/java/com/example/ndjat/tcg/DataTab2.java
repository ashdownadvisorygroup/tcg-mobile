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
 * Created by ndjat on 03/11/2017.
 */

public class DataTab2 extends Activity {

    private ListView obj;
    DBHelper mydb;
    EditText searchField;
    ArrayAdapter arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_history);

        mydb = new DBHelper(this);
        ArrayList array_list = mydb.getAllData();
        Log.e("datas ************", array_list.toString());
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
                String size = rs.getString(rs.getColumnIndex(DBHelper.DATA_COLUMN_SIZE));
                mylist[0] = "Volume : "+size+"MB";
                String url = rs.getString(rs.getColumnIndex(DBHelper.DATA_COLUMN_URL));
                mylist[1] = "Url : "+url;
                int delay = rs.getInt(rs.getColumnIndex(DBHelper.DATA_COLUMN_DELAY));
                mylist[2] = "Délai : "+delay;
                String status = rs.getString(rs.getColumnIndex(DBHelper.DATA_COLUMN_STATUS));
                mylist[3] = "Statut : "+status;
                String created = rs.getString(rs.getColumnIndex(DBHelper.DATA_COLUMN_CREATED));
                mylist[4] = "Création : "+created;
                String modified = rs.getString(rs.getColumnIndex(DBHelper.DATA_COLUMN_MODIFIED));
                mylist[5] = "Modification : "+modified;

                if (!rs.isClosed())  {
                    rs.close();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(DataTab2.this);
                builder.setTitle("Détails data")
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
                                mydb.deleteData(id_To_Search);
                                Toast.makeText(getApplicationContext(), "Data supprimé",
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
