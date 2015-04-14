package com.brentandjody.mountainunicyclist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 12/04/15.
 */
public class LocationsDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "locations.db";

    public LocationsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TRAILS_TABLE =
                "CREATE TABLE " + DBContract.Trail.TABLE_NAME + " (" +
                        DBContract.Trail._ID + " INTEGER PRIMARY KEY, " +
                        DBContract.Trail._UID + " INTEGER, " +
                        DBContract.Trail.COLUMN_TRAILSYSTEM + " INTEGER NOT NULL, " +
                        DBContract.Trail.COLUMN_NAME + " TEXT, " +
                        DBContract.Trail.COLUMN_DESCRIPTION + " TEXT, " +
                        DBContract.Trail.COLUMN_LAT + " REAL NOT NULL, " +
                        DBContract.Trail.COLUMN_LONG + " REAL NOT NULL, " +
                        DBContract.Trail.COLUMN_DIFFICULTY + " INTEGER NOT NULL, " +
                        DBContract.Trail.COLUMN_RATING + " INTEGER, " +
                        DBContract.Trail.COLUMN_PHOTO + " INTEGER, " +
                        DBContract.Trail._FLAGS + " INTEGER NOT NULL DEFAULT 0);";

        final String SQL_CREATE_FEATURE_TABLE =
                "CREATE TABLE " + DBContract.Feature.TABLE_NAME + " (" +
                        DBContract.Feature._ID + " INTEGER PRIMARY KEY, " +
                        DBContract.Feature._UID + " INTEGER, " +
                        DBContract.Feature.COLUMN_TRAIL + " INTEGER NOT NULL, " +
                        DBContract.Feature.COLUMN_NAME + " TEXT, " +
                        DBContract.Feature.COLUMN_DESCRIPTION + " TEXT, " +
                        DBContract.Feature.COLUMN_LAT + " REAL NOT NULL, " +
                        DBContract.Feature.COLUMN_LONG + " REAL NOT NULL, " +
                        DBContract.Feature.COLUMN_PHOTO + " INTEGER, " +
                        DBContract.Feature._FLAGS + " INTEGER NOT NULL DEFAULT 0);";

        final String SQL_CREATE_TRAILSYSTEM_TABLE =
                "CREATE TABLE " + DBContract.TrailSystem.TABLE_NAME + " (" +
                        DBContract.TrailSystem._ID + " INTEGER PRIMARY KEY, " +
                        DBContract.TrailSystem._UID + " INTEGER, " +
                        DBContract.TrailSystem.COLUMN_TITLE + " TEXT, " +
                        DBContract.TrailSystem.COLUMN_DESCRIPTION + " TEXT, " +
                        DBContract.TrailSystem.COLUMN_LAT + " REAL NOT NULL, " +
                        DBContract.TrailSystem.COLUMN_LONG + " REAL NOT NULL, " +
                        DBContract.TrailSystem.COLUMN_PHOTO + " INTEGER, " +
                        DBContract.TrailSystem._FLAGS + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_TRAILS_TABLE);
        db.execSQL(SQL_CREATE_FEATURE_TABLE);
        db.execSQL(SQL_CREATE_TRAILSYSTEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Trail.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Feature.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.TrailSystem.TABLE_NAME);
        onCreate(db);
    }

    // ## READING ##

    public Trail getTrail(int id) {
        if (id<0) return null;
        Trail result = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DBContract.Trail.TABLE_NAME
                + " WHERE " + DBContract.Trail._ID + " = " + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            result = TrailFromCursor(cursor);
        }
        cursor.close();
        return result;
    }

    public List<Trail> getAllTrails() {
        List<Trail> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DBContract.Trail.TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            result.add(TrailFromCursor(cursor));
        }
        cursor.close();
        return result;
    }

    public List<Trailsystem> getAllTrailsystems() {
        List<Trailsystem> result = new ArrayList<Trailsystem>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + DBContract.TrailSystem.TABLE_NAME
                + " WHERE " + DBContract.TrailSystem._FLAGS + " = 0 "
                + " ORDER BY " + DBContract.TrailSystem.COLUMN_TITLE + ";";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Trailsystem trailsystem = TrailsystemFromCursor(cursor);
                result.add(trailsystem);
            } while (cursor.moveToNext());
        }
        db.close();
        return result;
    }

    // ## INSERT/UPDATE ##

    public int InsertOrUpdate(Trail trail) {
        int id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.Trail._UID, trail.UID());
        values.put(DBContract.Trail._FLAGS, trail.FLAGS());
        values.put(DBContract.Trail.COLUMN_NAME, trail.Name());
        values.put(DBContract.Trail.COLUMN_DESCRIPTION, trail.Description());
        values.put(DBContract.Trail.COLUMN_LAT, trail.Location().latitude);
        values.put(DBContract.Trail.COLUMN_LONG, trail.Location().longitude);
        values.put(DBContract.Trail.COLUMN_DIFFICULTY, trail.Difficulty().ordinal());
        values.put(DBContract.Trail.COLUMN_RATING, trail.Rating());
        values.put(DBContract.Trail.COLUMN_TRAILSYSTEM, trail.Trailsystem());
        values.put(DBContract.Trail.COLUMN_PHOTO, trail.PhotoId());
        Trail record = getTrail(trail.ID());
        if (record==null) {
            id = (int)db.insert(DBContract.Trail.TABLE_NAME, null, values);
        } else {
            id = record.ID();
            db.update(DBContract.Trail.TABLE_NAME, values, DBContract.Trail._ID + "=" + id, null);
        }
        return id;
    }


    // ## BUILDERS ##

    private Trail TrailFromCursor(Cursor cursor) {
        if (cursor==null) return null;
        float lat = cursor.getFloat(cursor.getColumnIndex(DBContract.Trail.COLUMN_LAT));
        float lon = cursor.getFloat(cursor.getColumnIndex(DBContract.Trail.COLUMN_LONG));
        Trail.Difficulty difficulty = Trail.Difficulty.values()[cursor.getInt(cursor.getColumnIndex(DBContract.Trail.COLUMN_DIFFICULTY))];
        Trail trail = new Trail(cursor.getInt(cursor.getColumnIndex(DBContract.Trail._ID)),
                cursor.getInt(cursor.getColumnIndex(DBContract.Trail._UID)),
                cursor.getInt(cursor.getColumnIndex(DBContract.Trail._FLAGS)),
                cursor.getString(cursor.getColumnIndex(DBContract.Trail.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(DBContract.Trail.COLUMN_DESCRIPTION)),
                new LatLng(lat,lon), difficulty,
                cursor.getInt(cursor.getColumnIndex(DBContract.Trail.COLUMN_RATING)),
                cursor.getInt(cursor.getColumnIndex(DBContract.Trail.COLUMN_TRAILSYSTEM)),
                cursor.getInt(cursor.getColumnIndex(DBContract.Trail.COLUMN_PHOTO)));
        return trail;
    }

    private Trailsystem TrailsystemFromCursor(Cursor c) {
        if (c==null) return null;
        int ID = c.getInt(c.getColumnIndex(DBContract.TrailSystem._ID));
        int UID = c.getInt(c.getColumnIndex(DBContract.TrailSystem._UID));
        int FLAGS = c.getInt(c.getColumnIndex(DBContract.TrailSystem._FLAGS));
        String title = c.getString(c.getColumnIndex(DBContract.TrailSystem.COLUMN_TITLE));
        String description = c.getString(c.getColumnIndex(DBContract.TrailSystem.COLUMN_DESCRIPTION));
        int lat = c.getInt(c.getColumnIndex(DBContract.TrailSystem.COLUMN_LAT));
        int lon = c.getInt(c.getColumnIndex(DBContract.TrailSystem.COLUMN_LONG));
        int photoid = c.getInt(c.getColumnIndex(DBContract.TrailSystem.COLUMN_PHOTO));
        return new Trailsystem(ID, UID, FLAGS, title, description, lat, lon, photoid);
    }
}