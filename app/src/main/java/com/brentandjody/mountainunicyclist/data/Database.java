package com.brentandjody.mountainunicyclist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by brentn on 24/04/15.
 */
public class Database extends SQLiteOpenHelper {
    public static final String TABLE_TRAILS = "trails";
    public static final String COLUMN_TRAILS_FEATURED_PHOTO = "featured_photo";

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
