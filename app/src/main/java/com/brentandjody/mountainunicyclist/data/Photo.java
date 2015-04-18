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
 */
@ParseClassName("Photo")
public class Photo extends ParseObject {
    private static final String ID = "photoid";
    private static final String OWNER_ID = "ownerid";
    private static final String DOMINANT_COLOR = "dominantcolor";
    private static final String IMAGE_DATA = "data";
    private static final String FILENAME = "photo.png";
    private static final String FLAGS = "flags";

    private String mId = UUID.randomUUID().toString();
    private String mOwnerId = "";
    private String mDominantColor = "";
    private byte[] mData = null;
    private Flags  mFlags = new Flags();

    public Photo() {
    }

    public static ParseQuery<Photo> getQuery() { return ParseQuery.getQuery(Photo.class); }

    public void setOwnerId(String owner) { mOwnerId = owner;}
    public void setDominantColor(String color) {mDominantColor = color; }
    public void setData(byte[] data) { mData = data; }

    public String ID() { return mId; }
    public String OwnerId() { return mOwnerId; }
    public String DominantColor() { return mDominantColor; }
    public byte[] Data() { return mData; }
    public Bitmap bitmap() {return BitmapFactory.decodeByteArray(mData, 0, mData.length);}
    public Flags FLAGS() { return mFlags; }

    public void Save() {
        put(ID, mId);
        put(OWNER_ID, mOwnerId);
        put(DOMINANT_COLOR, mDominantColor);
        put(FLAGS, mFlags.toInt());
        if (mData!=null) {
            final ParseFile file = new ParseFile(FILENAME, mData);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null) {
                        put(IMAGE_DATA, file);
                        Log.d("SavePhoto", "image file saved");
                        saveEventually(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e==null) Log.d("SavePhoto", "photo saved");
                                else Log.w("SavePhoto", e.getMessage());
                            }
                        });
                    } else Log.w("SavePhoto", e.getMessage());
                }
            });
        } else {
            saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null) Log.d("SavePhoto", "photo saved without image file");
                    else Log.w("SavePhoto", e.getMessage());
                }
            });
        }
    }
    public void Load(String photo_id) {
        Load(photo_id, new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e == null) {
                    mData = bytes;
                    Log.d("LoadPhoto", "Image file loaded.");
                } else Log.w("LoadPhoto", e.getMessage());
            }
        });
    }
    public void Load(String photo_id, final GetDataCallback callback) {
        ParseQuery<Photo> query = getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(ID, photo_id);
        query.getFirstInBackground(new GetCallback<Photo>() {
            @Override
            public void done(Photo photo, ParseException e) {
                mId = photo.getString(ID);
                mOwnerId = photo.getString(OWNER_ID);
                mDominantColor = photo.getString(DOMINANT_COLOR);
                ParseFile file = (ParseFile) photo.get(FILENAME);
                file.getDataInBackground(callback);
                mFlags = photo.FLAGS();
            }
        });
    }
    public void LoadInto(final ImageView imageView) {
        ParseFile file = (ParseFile) get(FILENAME);
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        });
    }
    public static void LoadImage(String photo_id, final GetDataCallback callback) {
        ParseQuery<Photo> query = getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(ID, photo_id);
        query.getFirstInBackground(new GetCallback<Photo>() {
            @Override
            public void done(Photo photo, ParseException e) {
                if (e==null) {
                    ParseFile file = (ParseFile) photo.get(FILENAME);
                    file.getDataInBackground(callback);
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




    //        if (data==null) return;
//        final ParseFile file = new ParseFile("photo.png", data);
//        file.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    put(ID, file);
//                    Log.d("setPhotoData", "image saved");
//                } else {
//                    Log.w("setPhotoData", e.getMessage());
//                }
//            }
//        });
//
//    }

//    public void LoadData(final ImageView imageView) {
//        ParseFile file = (ParseFile) get(IMAGE_DATA);
//        if (file==null) return;
//        file.getDataInBackground(new GetDataCallback() {
//            @Override
//            public void done(byte[] bytes, ParseException e) {
//                if (e==null) {
//                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
//                    Log.d("LoadPhotoData", "successful");
//                } else {
//                    Log.w("LoadPhotoData", e.getMessage());
//                }
//            }
//        });
//    }

//    public static void Load(String id, final ImageView imageView) {
//        ParseQuery<Photo> query = Photo.getQuery();
//        query.fromLocalDatastore();
//        query.whereEqualTo(ID, id);
//        query.getFirstInBackground(new GetCallback<Photo>() {
//            public void done(Photo photo, ParseException e) {
//                if (e == null) {
//                    photo.LoadData(imageView);
//                    Log.d("LoadPhoto", "success");
//                } else {
//                    Log.w("LoadPhoto", e.getMessage());
//                }
//            }
//        });
//    }
//

}

