package com.nikith_shetty.interrupt60;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikith_Shetty on 22/02/2017.
 */

public class PostsDB extends SQLiteOpenHelper {

    private static String DB_NAME = "POSTS_DB";
    private static String TABLE_NAME = "POSTS";
    private static int DB_VERSION = 1;
    private static String ID_COL = "_id";
    private static String IMGURL_COL = "imgurl";
    private static String TITLE_COL = "title";
    private static String CONTENT_COL = "content";
    private static String DISPLAYAS_COL = "displayas";
    private static String WEBLINK_COL = "weblink";
    private static String TIMESTAMP_COL = "timestamp";

    public PostsDB(Context context) {
        super(context, DB_NAME , null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "( "
                + ID_COL + " INTEGER PRIMARY KEY, "
                + IMGURL_COL + " TEXT, "
                + TITLE_COL + " TEXT, "
                + CONTENT_COL + " TEXT, "
                + DISPLAYAS_COL + " TEXT, "
                + WEBLINK_COL + " TEXT, "
                + TIMESTAMP_COL + " TIMESTAMP);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    long insertData(String id, String imgurl, String title, String content,
                    String displayas, String weblink, String timestamp){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ID_COL, id);
        cv.put(IMGURL_COL, imgurl);
        cv.put(TITLE_COL, title);
        cv.put(CONTENT_COL, content);
        cv.put(DISPLAYAS_COL, displayas);
        cv.put(WEBLINK_COL, weblink);
        cv.put(TIMESTAMP_COL, timestamp);
        long rowID = db.insert(TABLE_NAME, null, cv);
        db.close();
        return rowID;
    }

    int deleteData(String id_col){
        SQLiteDatabase db = getWritableDatabase();
        int result = db.delete(TABLE_NAME, ID_COL + "=?", new String[]{id_col});
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
