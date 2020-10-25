package com.example.scheduleservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.scheduleservice.ScheduleDBContract.*;

import androidx.annotation.Nullable;

public class ScheduleDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ScheduleData.db";
    public static final int DATABASE_VERSION = 1;

    public ScheduleDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SCHEDULE_DATA_TABLE = "CREATE TABLE " +
                ScheduleTableInfo.TABLE_NAME + " (" +
                ScheduleTableInfo._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ScheduleTableInfo.COLUMN_CATEGORY + " TEXT NOT NULL, " +
                ScheduleTableInfo.COLUMN_FILENAME + " TEXT NOT NULL " + ");";
        db.execSQL(SQL_CREATE_SCHEDULE_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ ScheduleTableInfo.TABLE_NAME);
        onCreate(db);
    }
}
