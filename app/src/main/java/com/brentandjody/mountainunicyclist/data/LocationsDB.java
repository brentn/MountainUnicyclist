//package com.brentandjody.mountainunicyclist.data;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import com.google.android.gms.maps.model.LatLng;
//import com.parse.ParseException;
//import com.parse.ParseObject;
//import com.parse.SaveCallback;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
///**
// * Created by brent on 12/04/15.
// */
//public class LocationsDB extends SQLiteOpenHelper {
//    private static final int DATABASE_VERSION = 1;
//    public static final String DATABASE_NAME = "locations.db";
//
//    public LocationsDB(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
////        final String SQL_CREATE_TRAILS_TABLE =
////                "CREATE TABLE " + DBContract.Trail.TABLE_NAME + " (" +
////                        DBContract.Trail._ID + " INTEGER PRIMARY KEY, " +
////                        DBContract.Trail._UID + " TEXT, " +
////                        DBContract.Trail.COLUMN_TRAILSYSTEM + " TEXT NOT NULL, " +
////                        DBContract.Trail.COLUMN_NAME + " TEXT, " +
////                        DBContract.Trail.COLUMN_DESCRIPTION + " TEXT, " +
////                        DBContract.Trail.COLUMN_LAT + " REAL NOT NULL, " +
////                        DBContract.Trail.COLUMN_LONG + " REAL NOT NULL, " +
////                        DBContract.Trail.COLUMN_DIFFICULTY + " INTEGER NOT NULL, " +
////                        DBContract.Trail.COLUMN_RATING + " INTEGER, " +
////                        DBContract.Trail.COLUMN_PHOTO + " TEXT, " +
////                        DBContract.Trail._FLAGS + " INTEGER NOT NULL DEFAULT 0);";
//
//        final String SQL_CREATE_FEATURE_TABLE =
//                "CREATE TABLE " + DBContract.Feature.TABLE_NAME + " (" +
//                        DBContract.Feature._ID + " INTEGER PRIMARY KEY, " +
//                        DBContract.Feature._UID + " TEXT, " +
//                        DBContract.Feature.COLUMN_TRAIL + " INTEGER NOT NULL, " +
//                        DBContract.Feature.COLUMN_NAME + " TEXT, " +
//                        DBContract.Feature.COLUMN_DESCRIPTION + " TEXT, " +
//                        DBContract.Feature.COLUMN_LAT + " REAL NOT NULL, " +
//                        DBContract.Feature.COLUMN_LONG + " REAL NOT NULL, " +
//                        DBContract.Feature.COLUMN_PHOTO + " TEXT, " +
//                        DBContract.Feature._FLAGS + " INTEGER NOT NULL DEFAULT 0);";
//
//        final String SQL_CREATE_TRAILSYSTEM_TABLE =
//                "CREATE TABLE " + DBContract.TrailSystem.TABLE_NAME + " (" +
//                        DBContract.TrailSystem._ID + " INTEGER PRIMARY KEY, " +
//                        DBContract.TrailSystem._UID + " TEXT, " +
//                        DBContract.TrailSystem.COLUMN_TITLE + " TEXT, " +
//                        DBContract.TrailSystem.COLUMN_DESCRIPTION + " TEXT, " +
////                        DBContract.TrailSystem.COLUMN_LAT + " REAL NOT NULL, " +
////                        DBContract.TrailSystem.COLUMN_LONG + " REAL NOT NULL, " +
////                        DBContract.TrailSystem.COLUMN_PHOTO + " TEXT, " +
//                        DBContract.TrailSystem._FLAGS + " INTEGER NOT NULL DEFAULT 0);";
//
////        db.execSQL(SQL_CREATE_TRAILS_TABLE);
//        db.execSQL(SQL_CREATE_FEATURE_TABLE);
//        db.execSQL(SQL_CREATE_TRAILSYSTEM_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
//        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Trail.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + DBContract.Feature.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + DBContract.TrailSystem.TABLE_NAME);
//        onCreate(db);
//    }
//
//    // ## READING ##
//
//    public Trail getTrail(UUID id) {
//        if (id!=null) return null;
//        Trail result = null;
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM " + DBContract.Trail.TABLE_NAME
//                + " WHERE " + DBContract.Trail._UID + " = " + id;
//        Cursor cursor = db.rawQuery(query, null);
//        if (cursor.moveToFirst()) {
//            result = TrailFromCursor(cursor);
//        }
//        cursor.close();
//        return result;
//    }
//
//    public List<Trail> getAllTrails() {
//        List<Trail> result = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM " + DBContract.Trail.TABLE_NAME + ";";
//        Cursor cursor = db.rawQuery(query, null);
//        while (cursor.moveToNext()) {
//            result.add(TrailFromCursor(cursor));
//        }
//        cursor.close();
//        return result;
//    }
//
//    public List<Trailsystem> getAllTrailsystems() {
//        List<Trailsystem> result = new ArrayList<Trailsystem>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "SELECT * FROM " + DBContract.TrailSystem.TABLE_NAME
//                + " WHERE " + DBContract.TrailSystem._FLAGS + " = 0 "
//                + " ORDER BY " + DBContract.TrailSystem.COLUMN_TITLE + ";";
//        Cursor cursor = db.rawQuery(query, null);
//        if (cursor.moveToFirst()) {
//            do {
//                Trailsystem trailsystem = TrailsystemFromCursor(cursor);
//                result.add(trailsystem);
//            } while (cursor.moveToNext());
//        }
//        db.close();
//        return result;
//    }
//
//    // ## INSERT/UPDATE ##
//
//    public UUID InsertOrUpdate(Trail trail) {
//        UUID id = trail.ID();
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(DBContract.Trail._FLAGS, trail.FLAGS());
//        values.put(DBContract.Trail._UID, trail.ID().toString());
//        values.put(DBContract.Trail.COLUMN_NAME, trail.Name());
//        values.put(DBContract.Trail.COLUMN_DESCRIPTION, trail.Description());
//        values.put(DBContract.Trail.COLUMN_LAT, trail.Location().latitude);
//        values.put(DBContract.Trail.COLUMN_LONG, trail.Location().longitude);
//        values.put(DBContract.Trail.COLUMN_DIFFICULTY, trail.Difficulty().ordinal());
//        values.put(DBContract.Trail.COLUMN_RATING, trail.Rating());
//        values.put(DBContract.Trail.COLUMN_TRAILSYSTEM, trail.Trailsystem().toString());
//        values.put(DBContract.Trail.COLUMN_PHOTO, trail.PhotoId().toString());
//        Trail record = getTrail(trail.ID());
//        if (record==null) {
//            db.insert(DBContract.Trail.TABLE_NAME, null, values);
//        } else {
//            db.update(DBContract.Trail.TABLE_NAME, values, DBContract.Trail._ID + "=" + id.toString(), null);
//        }
//        return id;
//    }
//
//    // ## BUILDERS ##
//
//    private Trail TrailFromCursor(Cursor cursor) {
//        if (cursor==null) return null;
//        float lat = cursor.getFloat(cursor.getColumnIndex(DBContract.Trail.COLUMN_LAT));
//        float lon = cursor.getFloat(cursor.getColumnIndex(DBContract.Trail.COLUMN_LONG));
//        Trail.Difficulty difficulty = Trail.Difficulty.values()[cursor.getInt(cursor.getColumnIndex(DBContract.Trail.COLUMN_DIFFICULTY))];
//        Trail trail = new Trail(UUID.fromString(cursor.getString(cursor.getColumnIndex(DBContract.Trail._ID))),
//                cursor.getInt(cursor.getColumnIndex(DBContract.Trail._FLAGS)),
//                cursor.getString(cursor.getColumnIndex(DBContract.Trail.COLUMN_NAME)),
//                cursor.getString(cursor.getColumnIndex(DBContract.Trail.COLUMN_DESCRIPTION)),
//                new LatLng(lat,lon), difficulty,
//                cursor.getInt(cursor.getColumnIndex(DBContract.Trail.COLUMN_RATING)),
//                UUID.fromString(cursor.getString(cursor.getColumnIndex(DBContract.Trail.COLUMN_TRAILSYSTEM))),
//                cursor.getString(cursor.getColumnIndex(DBContract.Trail.COLUMN_PHOTO)));
//        return trail;
//    }
//
//    private Trailsystem TrailsystemFromCursor(Cursor c) {
//        if (c==null) return null;
//        UUID ID = UUID.fromString(c.getString(c.getColumnIndex(DBContract.TrailSystem._ID)));
//        int FLAGS = c.getInt(c.getColumnIndex(DBContract.TrailSystem._FLAGS));
//        String title = c.getString(c.getColumnIndex(DBContract.TrailSystem.COLUMN_TITLE));
//        String description = c.getString(c.getColumnIndex(DBContract.TrailSystem.COLUMN_DESCRIPTION));
//        int lat = c.getInt(c.getColumnIndex(DBContract.TrailSystem.COLUMN_LAT));
//        int lon = c.getInt(c.getColumnIndex(DBContract.TrailSystem.COLUMN_LONG));
//        UUID photoid = UUID.fromString(c.getString(c.getColumnIndex(DBContract.TrailSystem.COLUMN_PHOTO)));
//        return new Trailsystem(ID,  FLAGS, title, description, lat, lon, photoid);
//    }
//
//}