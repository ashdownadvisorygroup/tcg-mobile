package com.example.ndjat.tcg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by ndjat on 17/02/2018.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TCG.db";

    public static final String CALL_TABLE_NAME = "call";
    public static final String CALL_COLUMN_ID = "id";
    public static final String CALL_COLUMN_PHONE = "phone";
    public static final String CALL_COLUMN_DURATION = "duration";
    public static final String CALL_COLUMN_DELAY = "delay";
    public static final String CALL_COLUMN_STATUS = "status";//done - pending - received - error
    public static final String CALL_COLUMN_TYPE = "type";//calling - receiving
    public static final String CALL_COLUMN_CREATED = "created";
    public static final String CALL_COLUMN_MODIFIED = "modified";

    public static final String SMS_TABLE_NAME = "sms";
    public static final String SMS_COLUMN_ID = "id";
    public static final String SMS_COLUMN_PHONE = "phone";
    public static final String SMS_COLUMN_MESSAGE = "message";
    public static final String SMS_COLUMN_DELAY = "delay";
    public static final String SMS_COLUMN_STATUS = "status";//done - error - received
    public static final String SMS_COLUMN_CREATED = "created";
    public static final String SMS_COLUMN_MODIFIED = "modified";

    public static final String DATA_TABLE_NAME = "data";
    public static final String DATA_COLUMN_ID = "id";
    public static final String DATA_COLUMN_SIZE = "size";
    public static final String DATA_COLUMN_URL = "url";
    public static final String DATA_COLUMN_DELAY = "delay";
    public static final String DATA_COLUMN_STATUS = "status";//done - error - received - ongoing
    public static final String DATA_COLUMN_CREATED = "created";
    public static final String DATA_COLUMN_MODIFIED = "modified";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table call " +
                        "(id integer primary key, phone text,duration integer,delay integer, status text,type text,created text,modified text)"
        );
        db.execSQL(
                "create table sms " +
                        "(id integer primary key, phone text,message text,delay integer, status text,created text,modified text)"
        );
        db.execSQL(
                "create table data " +
                        "(id integer primary key,size integer, url text,delay integer, status text,created text,modified text)"
        );
        /*db.execSQL(
                "create table contacts " +
                        "(id integer primary key, name text,phone text,email text, street text,place text)"
        );*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS call");
        db.execSQL("DROP TABLE IF EXISTS sms");
        db.execSQL("DROP TABLE IF EXISTS data");
        //db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public long insertCall (String phone, int duration, int delay, String status,String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date dt = new Date();
        String strDt = simpleDate.format(dt);
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone", phone);
        contentValues.put("duration", duration);
        contentValues.put("delay", delay);
        contentValues.put("status", status);
        contentValues.put("type", type);
        contentValues.put("created", strDt);
        contentValues.put("modified", strDt);
        long id = db.insert("call", null, contentValues);
        return id;
    }

    public long insertSms (String phone, String message, int delay, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date dt = new Date();
        String strDt = simpleDate.format(dt);
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone", phone);
        contentValues.put("message", message);
        contentValues.put("delay", delay);
        contentValues.put("status", status);
        contentValues.put("created", strDt);
        contentValues.put("modified", strDt);
        long id = db.insert("sms", null, contentValues);
        return id;
    }

    public long insertData (int size, String url, int delay, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date dt = new Date();
        String strDt = simpleDate.format(dt);
        ContentValues contentValues = new ContentValues();
        contentValues.put("size", size);
        contentValues.put("url", url);
        contentValues.put("delay", delay);
        contentValues.put("status", status);
        contentValues.put("created", strDt);
        contentValues.put("modified", strDt);
        long id = db.insert("data", null, contentValues);
        return id;
    }

    public Cursor getCall(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from call where id="+id+"", null );
        return res;
    }

    public Cursor getSms(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from sms where id="+id+"", null );
        return res;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from data where id="+id+"", null );
        return res;
    }

    public int numberOfCallRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CALL_TABLE_NAME);
        return numRows;
    }

    public int numberOfSmsRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, SMS_TABLE_NAME);
        return numRows;
    }

    public int numberOfDataRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, DATA_TABLE_NAME);
        return numRows;
    }

    public boolean updateCall (Integer id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date dt = new Date();
        String strDt = simpleDate.format(dt);
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        contentValues.put("modified", strDt);
        db.update("call", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateSms (Integer id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date dt = new Date();
        String strDt = simpleDate.format(dt);
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        contentValues.put("modified", strDt);
        db.update("sms", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateData (Integer id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date dt = new Date();
        String strDt = simpleDate.format(dt);
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        contentValues.put("modified", strDt);
        db.update("data", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteCall (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("call",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteSms (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("sms",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteData (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("data",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCall() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from call", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CALL_COLUMN_PHONE)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllSms() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from sms", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(SMS_COLUMN_PHONE)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllData() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from data", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(DATA_COLUMN_SIZE)));
            res.moveToNext();
        }
        return array_list;
    }

}
