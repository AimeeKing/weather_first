package com.example.aimee.weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aimee on 2015/11/21.
 */
public class CityDB extends SQLiteOpenHelper {
    private final String DB_NAME="CITYID";
    public CityDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+DB_NAME+
        "("+
          "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "cityname TEXT NOT NULL,"+
            "cityid TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
