package com.brentandjody.mountainunicyclist.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.brentandjody.mountainunicyclist.data.DBContract.Photos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 07/04/15.
 */
public class PhotoDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="photos.db";

    public PhotoDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_DB =
                "CREATE TABLE " + Photos.TABLE_NAME + " (" +
                        Photos._ID + " INTEGER PRIMARY KEY, " +
                        Photos._UID + " INTEGER, " +
                        Photos.COLUMN_OWNER_TYPE + " INTEGER NOT NULL, " +
                        Photos.COLUMN_OWNER_ID + " INTEGER NOT NULL, " +
                        Photos.COLUMN_DATA + " BLOB NOT NULL, " +
                        Photos.COLUMN_DOMINANT_COLOR + " TEXT, " +
                        Photos._FLAGS + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        db.execSQL("DROP TABLE IF EXISTS " + Photos.TABLE_NAME);
        onCreate(db);
    }

    public Photo Get(int id) {
        if (id<0) return null;
        Photo result = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + Photos.TABLE_NAME
                + " WHERE " + DBContract.Photos._ID + " = " + id;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            result = PhotoFromCursor(cursor);
        }
        cursor.close();
        return result;
    }

    public List<Photo> GetPhotosForTrail(int trailId) {
        List<Photo> result = new ArrayList<Photo>();
        if (trailId<0) return result;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + Photos.TABLE_NAME
                + " WHERE " + Photos.COLUMN_OWNER_TYPE + " = " + Photo.PHOTO_TYPE.TRAIL.ordinal()
                + " AND " + Photos.COLUMN_OWNER_ID + " = " + trailId;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            result.add(PhotoFromCursor(cursor));
        }
        return result;
    }

    public boolean Delete(int id) {
        if (id<0) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(Photos.TABLE_NAME, DBContract.Photos._ID + "=" + id, null);
        return result>0;
    }

    public int InsertOrUpdate(Photo photo) {
        int id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Photos.COLUMN_OWNER_TYPE, photo.OwnerType().ordinal());
        values.put(Photos.COLUMN_OWNER_ID, photo.Owner());
        values.put(Photos.COLUMN_DATA, photo.Data());
        values.put(Photos.COLUMN_DOMINANT_COLOR, photo.DominantColor());
        Photo record = Get(photo.ID());
        if (record==null) {
            id = (int)db.insert(Photos.TABLE_NAME, null, values);
        } else {
            id = record.ID();
            values.put(DBContract.Photos._ID, id);
            values.put(DBContract.Photos._UID, record.UID());
            db.update(Photos.TABLE_NAME, values, DBContract.Photos._ID + "=" + id, null);
        }
        return id;
    }

    private Photo PhotoFromCursor(Cursor cursor) {
        Photo result = new Photo(cursor.getInt(cursor.getColumnIndex(Photos._ID)),
                cursor.getInt(cursor.getColumnIndex(DBContract.Photos._UID)),
                cursor.getInt(cursor.getColumnIndex(Photos._FLAGS)),
                Photo.PHOTO_TYPE.values()[cursor.getInt(cursor.getColumnIndex(Photos.COLUMN_OWNER_TYPE))],
                cursor.getInt(cursor.getColumnIndex(Photos.COLUMN_OWNER_ID)),
                cursor.getString(cursor.getColumnIndex(Photos.COLUMN_DOMINANT_COLOR)),
                cursor.getBlob(cursor.getColumnIndex(Photos.COLUMN_DATA)));
        return result;
    }
}
