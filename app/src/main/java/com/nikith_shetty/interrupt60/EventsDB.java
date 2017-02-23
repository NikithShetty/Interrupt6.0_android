package com.nikith_shetty.interrupt60;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikith_Shetty on 22/02/2017.
 */

public class EventsDB extends SQLiteOpenHelper {

    private static String DB_NAME = "EVENTS_DB";
    private static String TABLE_NAME = "EVENTS";
    private static int DB_VERSION = 1;
    private static String EVENT_ID = "event_id";
    private static String EVENT_NAME = "event_name";
    private static String IMGURL = "img_url";
    private static String EVENT_DESC = "event_desc";
    private static String DATE_TIME = "date_time";
    private static String VENUE = "venue";
    private static String FEE = "fee";
    private static String CONTACT = "contact";

    public EventsDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "( "
                + EVENT_ID + " TEXT PRIMARY KEY, "
                + EVENT_NAME + " TEXT, "
                + IMGURL + " TEXT, "
                + EVENT_DESC + " TEXT, "
                + DATE_TIME + " TEXT, "
                + VENUE + " TEXT, "
                + FEE + " TEXT, "
                + CONTACT + " TEXT);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    long insertData(String eve_id, String name, String imgurl, String desc,
                    String time, String venue, String fee, String contact){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(EVENT_ID, eve_id);
        cv.put(EVENT_NAME, name);
        cv.put(IMGURL, imgurl);
        cv.put(EVENT_DESC, desc);
        cv.put(DATE_TIME, time);
        cv.put(VENUE, venue);
        cv.put(FEE, fee);
        cv.put(CONTACT, contact);
        long rowID = db.insert(TABLE_NAME, null, cv);
        db.close();
        return rowID;
    }

    int deleteData(String id_col){
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_NAME, EVENT_ID + "=?", new String[]{id_col});
        db.close();
        return result;
    }

    Cursor readAllData(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.query(TABLE_NAME,
                null,
                null, null, null, null, null);
        return result;
    }

    int emptyDB(){
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_NAME, null, new String[]{});
        db.close();
        return result;
    }
}
