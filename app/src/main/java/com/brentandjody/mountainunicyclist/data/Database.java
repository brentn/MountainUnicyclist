package com.brentandjody.mountainunicyclist.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by brentn on 24/04/15.
 */
public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_LOOKUP = "lookup";
    private static final String KEY = "key";
    private static final String VALUE = "value";

    public static final String KEY_FEATURE_PHOTO = "feature_photo";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_LOOKUP_TABLE =
                "CREATE TABLE " + TABLE_LOOKUP + " (" +
                        KEY + " TEXT NOT NULL PRIMARY KEY, " +
                        VALUE + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_LOOKUP_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOOKUP);
    }

    public String Lookup(String object_id, String key) {
        String result = null;
        String lookup_key = object_id + ":" + key;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_LOOKUP, new String[] {VALUE}, KEY+"=?", new String[] {lookup_key},null,null,null);
        if (c.moveToFirst()) result = c.getString(c.getColumnIndex(VALUE));
        c.close();
        db.close();
        return result;
    }
}
