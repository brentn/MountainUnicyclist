package com.brentandjody.mountainunicyclist.data;


import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.UUID;

/**
 * Created by brent on 10/04/15.
 */
@ParseClassName("Photo")
public class Photo extends ParseObject {

    public Photo() {}
    public Photo(byte[] data) {
        setID();
        setData(data);
    }

    public static ParseQuery<Photo> getQuery() {
        return ParseQuery.getQuery(Photo.class);
    }

    public void setID() {
        UUID uuid = UUID.randomUUID();
        put(DBContract.Photos._UID, uuid.toString());
    }
    public void setFLAGS(int flags) { put(DBContract.Photos._FLAGS, flags);}
    public void setOwnerId(String owner) {
        if (owner==null) return;
        put(DBContract.Photos.COLUMN_OWNER_ID, owner);
    }
    public void setDominantColor(String color) {
        if (color==null) return;
        put(DBContract.Photos.COLUMN_DOMINANT_COLOR, color);
    }
    public void setData(byte[] data) {
        if (data==null) return;
        put(DBContract.Photos.COLUMN_DATA, data);
    }

    public String ID() { return getString(DBContract.Photos._UID); }
    public int FLAGS() { return getInt(DBContract.Photos._FLAGS); }
    public String OwnerId() { return getString(DBContract.Photos.COLUMN_OWNER_ID); }
    public String DominantColor() { return getString(DBContract.Photos.COLUMN_DOMINANT_COLOR);}
    public byte[] Data() { return getBytes(DBContract.Photos.COLUMN_DATA); }

    public static void Load(String id, final ImageView image) {
        ParseQuery<Photo> query = Photo.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(DBContract.Photos._UID, id);
        query.getFirstInBackground(new GetCallback<Photo>() {
            public void done(Photo photo, ParseException e) {
                if (e == null) {
                    image.setImageBitmap(BitmapFactory.decodeByteArray(photo.Data(), 0, photo.Data().length));
                } else {
                    Log.w("LoadPhoto", e.getMessage());
                }
            }
        });
    }

    public void Delete() {
        deleteEventually();
    }
    public static void Delete(Photo photo) {
        photo.deleteEventually();
    }
    public static void Delete(String id) {
        ParseQuery<Photo> query = Photo.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(DBContract.Photos._UID, id);
        query.getFirstInBackground(new GetCallback<Photo>() {
            @Override
            public void done(Photo photo, ParseException e) {
                if (e == null);
                photo.deleteEventually();
            }
        });
    }
}

