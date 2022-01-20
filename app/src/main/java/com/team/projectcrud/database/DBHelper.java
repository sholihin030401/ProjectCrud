package com.team.projectcrud.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static String DB_NAME = "datauser";
    private static final int DB_VERSION = 1;

    public static final String SQL_CREATE_TABLE_USER = String.format("CREATE TABLE %s"
                                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                    " %s TEXT NOT NULL," +
                                    " %s TEXT NOT NULL," +
                                    " %s TEXT NOT NULL," +
                                    " %s TEXT NOT NULL)",
            DBContract.TABLE_USER,
            DBContract.UserColumns._ID,
            DBContract.UserColumns.EMAIL,
            DBContract.UserColumns.NAME,
            DBContract.UserColumns.PHONE,
            DBContract.UserColumns.ADDRESS);

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.TABLE_USER);
        onCreate(db);
    }
}
