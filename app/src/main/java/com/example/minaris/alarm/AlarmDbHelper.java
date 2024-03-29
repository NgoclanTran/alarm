package com.example.minaris.alarm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.minaris.alarm.AlarmContract.AlarmEntry;
import com.example.minaris.alarm.AlarmContract.MotionEntry;

/**
 * Created by jodeneve on 21/11/2016.
 */

public class AlarmDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "AlarmDB";
    public static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ALARM = "CREATE TABLE " + AlarmEntry.TABLE_NAME + " (" +
            AlarmEntry._ID + " INTEGER PRIMARY KEY," +
            AlarmEntry.TIME_SLOT + " TEXT," +
            AlarmEntry.RINGTONE + " TEXT," +
            AlarmEntry.SNOOZABLE + " INTEGER," +
            AlarmEntry.ACTIVE + " INTEGER," +
            AlarmEntry.REPEAT + " TEXT)";

    private static final String SQL_CREATE_SNOOZE = "CREATE TABLE " + MotionEntry.TABLE_NAME + " (" +
            MotionEntry._ID + " INTEGER PRIMARY KEY," +
            MotionEntry.MOTION_NAME+ " TEXT," +
            MotionEntry.X + " TEXT," +
            MotionEntry.Y + " TEXT," +
            MotionEntry.Z + " TEXT," +
            MotionEntry.TYPE + " TEXT," +
            MotionEntry.INTERVAL + " TEXT)";




    public AlarmDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ALARM);
        db.execSQL(SQL_CREATE_SNOOZE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
