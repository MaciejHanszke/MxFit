package com.mxfit.mentix.menu3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabasePremadesHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "premades.db";
    static final String DBLOCATION = "data/data/com.mxfit.mentix.menu3/databases/";
    private static String TABLE_NAME = "Level";
    private static String RUNTABLE_NAME = "Runs";
    public static final String COL_1 = "pushups";
    private static final String COL_2 = "done";
    private static final String COL_3 = "kilometers";
    private Context mContext;
    private SQLiteDatabase db;

    DatabasePremadesHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    boolean insertRunData(Double km){
        db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
            contentValues.put(COL_3, km);
            contentValues.put(COL_2, 0);
            long result = db.insert(RUNTABLE_NAME, null, contentValues);


        return result != -1;
    }

    int getHowManyDays(int trainingLevel)
    {
        openDatabase();
        Cursor cursor = db.rawQuery("Select count(ROWID) from " + TABLE_NAME + trainingLevel, null);
        cursor.moveToFirst();
        closeDatabase();
        return cursor.getInt(0);
    }

    int getHowManyTrainings()
    {
        openDatabase();
        Cursor cursor = db.rawQuery("SELECT count(name) FROM sqlite_master WHERE type = 'table' and (name != 'android_metadata' AND name != 'Runs')", null);
        cursor.moveToFirst();
        closeDatabase();
        return cursor.getInt(0);
    }

    int getHowManyRuns()
    {
        openDatabase();
        Cursor cursor = db.rawQuery("SELECT count(ROWID) FROM "+ RUNTABLE_NAME, null);
        cursor.moveToFirst();
        closeDatabase();
        return cursor.getInt(0);
    }

    void setPushupsFinished(int day, int trainingLevel)
    {
        openDatabase();
        db.execSQL("UPDATE "+ TABLE_NAME + trainingLevel + " SET " + COL_2 + "=1 WHERE ROWID=" + day);
        closeDatabase();
    }

    void setRunFinished(int day)
    {
        openDatabase();
        db.execSQL("UPDATE "+ RUNTABLE_NAME + " SET " + COL_2 + "=1 WHERE ROWID=" + day);
        closeDatabase();
    }

    List <Pushups> getListPushups(boolean isListed){
        Pushups pushup;
        String table;
        List<Pushups> pushupsList = new ArrayList<>();
        openDatabase();
        Cursor data = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table' and (name != 'android_metadata' AND name != 'Runs')", null);
        Cursor cursor;

        data.moveToFirst();
        while(!data.isAfterLast()) {
            table = data.getString(0);
            if(isListed) {
                pushup = new Pushups(0, Integer.parseInt(data.getString(0).substring(5)), "1", false);
                pushupsList.add(pushup);
            }
            cursor = db.rawQuery("Select ROWID, pushups, done from " + table, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                pushup = new Pushups(cursor.getInt(0),Integer.parseInt(data.getString(0).substring(5)), cursor.getString(1), cursor.getInt(2) == 1);
                pushupsList.add(pushup);
                cursor.moveToNext();
            }
            data.moveToNext();
            cursor.close();
        }
        data.close();
        closeDatabase();
        return pushupsList;
    }

    List <PreRun> getListPreRuns(){
        PreRun preRun;
        List<PreRun> PreRunList = new ArrayList<>();
        openDatabase();

        Cursor data = db.rawQuery("SELECT ROWID, kilometers, done FROM "+ RUNTABLE_NAME , null);
        data.moveToFirst();
        while(!data.isAfterLast()) {
            preRun = new PreRun(data.getInt(0), data.getFloat(1), data.getInt(2)==1);
            PreRunList.add(preRun);
            data.moveToNext();
        }
        data.close();
        closeDatabase();
        return PreRunList;
    }

    String getDayPushups(int day, int trainingLevel){
        openDatabase();
        Cursor cursor = db.rawQuery("Select pushups from " + TABLE_NAME + trainingLevel + " where ROWID = " +day, null);
        cursor.moveToFirst();
        return cursor.getString(0);
    }

    float getDayRun(int day){
        openDatabase();
        Cursor cursor = db.rawQuery("Select kilometers from " + RUNTABLE_NAME + " where ROWID = " +day, null);
        cursor.moveToFirst();
        return cursor.getFloat(0);
    }

    private void openDatabase(){
        String dbPath = mContext.getDatabasePath(DATABASE_NAME).getPath();
        if(db != null && db.isOpen()){
            return;
        }
        db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    private void closeDatabase(){
        if(db!=null)
        {
            db.close();
        }
    }



}
