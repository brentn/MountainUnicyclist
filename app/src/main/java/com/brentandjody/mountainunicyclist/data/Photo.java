package com.brentandjody.mountainunicyclist.data;


import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

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
        final ParseFile file = new ParseFile("photo.png", data);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    put(DBContract.Photos.COLUMN_IMAGE_ID, file);
                    Log.d("setData", "image saved");
                } else {
                    Log.w("setData", e.getMessage());
                }
            }
        });

    }

    public String ID() { return getString(DBContract.Photos._UID); }
    public int FLAGS() { return getInt(DBContract.Photos._FLAGS); }
    public String OwnerId() { return getString(DBContract.Photos.COLUMN_OWNER_ID); }
    public String DominantColor() { return getString(DBContract.Photos.COLUMN_DOMINANT_COLOR);}
    public void LoadData(final ImageView imageView) {
        ParseFile file = (ParseFile) get(DBContract.Photos.COLUMN_DATA);
        if (file==null) return;
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e==null) {
                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                } else {
                    Log.w("LoadData", e.getMessage());
                }
            }
        });
    }

    public static void Load(String id, final ImageView imageView) {
        ParseQuery<Photo> query = Photo.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(DBContract.Photos._UID, id);
        query.getFirstInBackground(new GetCallback<Photo>() {
            public void done(Photo photo, ParseException e) {
                if (e == null) {
                    photo.LoadData(imageView);
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
            if (e == null && photo!=null);
                photo.deleteEventually();
            }
        });
    }
}

