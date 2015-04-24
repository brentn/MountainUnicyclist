package com.brentandjody.mountainunicyclist.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.parse.FindCallback;
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
 * Photo class that synchs with Parse.com
 */
@ParseClassName("Photo")
public class Photo extends ParseObject {
    private static final String ID = "objectId"; //Don't change this name
    private static final String OWNER_ID = "ownerid";
    private static final String DOMINANT_COLOR = "dominantcolor";
    private static final String IMAGE_FILE = "file";
    private static final String FILENAME = "photo.png";
    private static final String FLAGS = "flags";

    public Photo() {
    }

    public static ParseQuery<Photo> getQuery() { return ParseQuery.getQuery(Photo.class); }

    public void setOwnerId(String owner) { put(OWNER_ID, owner);}
    public void setDominantColor(String color) {put(DOMINANT_COLOR, color); }
    public void setData(byte[] data) {
        final ParseFile file = new ParseFile(FILENAME, data);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) {
                    put(IMAGE_FILE, file);
                    saveEventually();
                    Log.d("setData", "Image file saved");
                } else Log.w("setData", e.getMessage());
            }
        });
    }

    public String ID() { return getObjectId(); }
    public String OwnerId() { return getString(OWNER_ID); }
    public String DominantColor() { return getString(DOMINANT_COLOR); }

    public Flags FLAGS() { return new Flags(getInt(FLAGS)); }

    public void Load(String photo_id) {
        ParseQuery<Photo> query = Photo.getQuery();
        query.fromLocalDatastore();
        query.getInBackground(photo_id, new GetCallback<Photo>() {
            @Override
            public void done(Photo photo, ParseException e) {
                if (e == null) {
                    Log.d("LoadPhoto", "Image file loaded.");
                } else Log.w("LoadPhoto", e.getMessage());
            }
        });
    }
    public void Load(String photo_id, final GetDataCallback photo_data_callback) {
        ParseQuery<Photo> query = getQuery();
        query.fromLocalDatastore();
        query.getInBackground(photo_id, new GetCallback<Photo>() {
            @Override
            public void done(Photo photo, ParseException e) {
                ParseFile file = photo.getParseFile(IMAGE_FILE);
                file.getDataInBackground(photo_data_callback);
            }
        });
    }
    public void LoadInto(final ImageView imageView) {
        ParseFile file = getParseFile(IMAGE_FILE);
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e==null) {
                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    Log.d("LoadInto", "ImageView loaded with image data");
                } else Log.w("LoadInto", e.getMessage());
            }
        });
    }
    public static void LoadImage(String photo_id, final GetDataCallback photo_data_callback) {
        ParseQuery<Photo> query = getQuery();
        query.fromLocalDatastore();
        query.getInBackground(photo_id, new GetCallback<Photo>() {
            @Override
            public void done(Photo photo, ParseException e) {
                if (e==null) {
                    ParseFile file = photo.getParseFile(IMAGE_FILE);
                    file.getDataInBackground(photo_data_callback);
                    Log.d("LoadImage", "photo object loaded");
                } else Log.w("LoadImage", e.getMessage());
            }
        });
    }
    public static void LoadImagesForOwner(String owner_id, final FindCallback callback) {
        ParseQuery<Photo> query = getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(OWNER_ID, owner_id);
        query.findInBackground(callback);
    }
    public static void Delete(String id) {
        ParseQuery<Photo> query = Photo.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(ID, id);
        query.getFirstInBackground(new GetCallback<Photo>() {
            @Override
            public void done(Photo photo, ParseException e) {
                if (e == null)
                    if (photo!=null) photo.deleteEventually();
                    else
                        Log.w("DeletePhoto", e.getMessage());
            }
        });
    }


}

