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
    public static final String ID = "photoid";
    public static final String OWNER_ID = "ownerid";
    public static final String DOMINANT_COLOR = "dominantcolor";
    public static final String IMAGE_DATA = "data";
    private static final String FLAGS = "flags";

    public Photo() {}
    public Photo(byte[] data) {
        setID();
        setData(data);
    }

    public static ParseQuery<Photo> getQuery() { return ParseQuery.getQuery(Photo.class); }

    public void setID() {
        UUID uuid = UUID.randomUUID();
        put(ID, uuid.toString());
    }
    public void setFLAGS(Flags flags) {
        put(FLAGS, flags.toInt());
    }
    public void setOwnerId(String owner) {
        if (owner==null) return;
        put(OWNER_ID, owner);
    }
    public void setDominantColor(String color) {
        if (color==null) return;
        put(DOMINANT_COLOR, color);
    }
    public void setData(byte[] data) {
        if (data==null) return;
        final ParseFile file = new ParseFile("photo.png", data);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    put(ID, file);
                    Log.d("setPhotoData", "image saved");
                } else {
                    Log.w("setPhotoData", e.getMessage());
                }
            }
        });

    }

    public String ID() { return getString(ID); }
    public Flags FLAGS() { return new Flags(getInt(FLAGS)); }
    public String OwnerId() { return getString(OWNER_ID); }
    public String DominantColor() { return getString(DOMINANT_COLOR);}
    public void LoadData(final ImageView imageView) {
        ParseFile file = (ParseFile) get(IMAGE_DATA);
        if (file==null) return;
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e==null) {
                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    Log.d("LoadPhotoData", "successful");
                } else {
                    Log.w("LoadPhotoData", e.getMessage());
                }
            }
        });
    }

    public static void Load(String id, final ImageView imageView) {
        ParseQuery<Photo> query = Photo.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(ID, id);
        query.getFirstInBackground(new GetCallback<Photo>() {
            public void done(Photo photo, ParseException e) {
                if (e == null) {
                    photo.LoadData(imageView);
                    Log.d("LoadPhoto", "success");
                } else {
                    Log.w("LoadPhoto", e.getMessage());
                }
            }
        });
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

