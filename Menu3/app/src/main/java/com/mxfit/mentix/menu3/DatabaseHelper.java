package com.mxfit.mentix.menu3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.android.gms.maps.model.LatLng;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "history.db";
    private static String TABLE_NAME;
    private static final String COL_1 = "latitude";
    private static final String COL_2 = "longitude";
    private SQLiteDatabase db;

    DatabaseHelper(Context context, String name) {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
        TABLE_NAME = name;
        db.execSQL("create table " + TABLE_NAME + "(" +
                COL_1 + " double , "+
                COL_2 + " double "+
                ")");
        db.execSQL("INSERT INTO " + TABLE_NAME +
                "(" + COL_1 + ", "
                    + COL_2 + ") VALUES (0, 0)");
    }


    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    boolean insertMultipleData(LatLng[] locations, int size, double distance, double time){
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        db.beginTransaction();
        long result = 0;
        for(int i = 0; i<=size; i++)
        {
            contentValues.put(COL_1, locations[i].latitude);
            contentValues.put(COL_2, locations[i].longitude);
            result = db.insert(TABLE_NAME, null, contentValues);
        }
        //update informacji w pierwszej tabeli dla przetrzymywania dystansu i czasu
        db.execSQL("UPDATE "+ TABLE_NAME + " SET " + COL_1 + " = " + distance +
                    ", " + COL_2 + " = " + time + " WHERE ROWID=1");

        db.setTransactionSuccessful();
        db.endTransaction();

        return result != -1;
    }

    Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table' and " +
                           "name != 'android_metadata' order by name DESC", null);
    }

    Cursor queryPass(String string){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM "+string + " where ROWID = 1", null);
    }

    Cursor selectSpecificTable(String tablename){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + tablename , null);
    }

    void dropTable(String string){
        SQLiteDatabase db = this.getWritableDatabase();
        String my_new_str = "t_" + string.replaceAll(" ", "_");
        my_new_str = my_new_str.replaceAll(":", "ź");
        my_new_str = my_new_str.replaceAll("/", "ś");
        db.execSQL("DROP TABLE " + my_new_str );
    }

}
