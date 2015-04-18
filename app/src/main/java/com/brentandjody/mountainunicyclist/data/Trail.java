package com.brentandjody.mountainunicyclist.data;

import android.util.Log;

import com.brentandjody.mountainunicyclist.TrailAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;
import java.util.UUID;

/**
 * Created by brent on 10/04/15.
 */
@ParseClassName("Trail")
public class Trail extends ParseObject {
    private static final String ID = "trailid";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String LAT = "latitude";
    private static final String LNG = "longitude";
    private static final String DIFFICULTY = "difficulty";
    private static final String RATING = "rating";
    private static final String PHOTO_ID = "featurephotoid";
    private static final String TRAILSYSTEM_ID = "trailsystemid";
    private static final String FLAGS = "flags";

    private static final int MAX_STARS = 8;

    private String mId = UUID.randomUUID().toString();
    private String mName = "";
    private String mDescription = "";
    private LatLng mLocation = null;
    private Difficulty mDifficulty = new Difficulty();
    private int mRating = MAX_STARS/2;
    private String mFeaturedPhotoId = "";
    private String mTrailsystemId = "";
    private Flags mFlags = new Flags();

    public Trail() {}

    public static ParseQuery<Trail> getQuery() { return ParseQuery.getQuery(Trail.class); }

    public void setName(String name) { mName=name; }
    public void setDescription(String description) { mDescription = description; }
    public void setLocation(LatLng location) { mLocation = location; }
    public void setRating(int rating) {
        if (rating < 0) return;
        if (rating > MAX_STARS) rating=MAX_STARS;
        mRating = rating;
    }
    public void setDifficulty(Difficulty difficulty) { mDifficulty = difficulty;}
    public void setTrailsystem(String trailsystem_id) { mTrailsystemId = trailsystem_id;}
    public void setPhotoId(String photo_id) { mFeaturedPhotoId = photo_id; }
    public void setFlags(Flags flags) { mFlags = flags;}

    public String ID() {return mId;}
    public String Name() {return mName;}
    public String Description() {return mDescription;}
    public LatLng Location() { return mLocation; }
    public int Rating() {return mRating;}
    public Difficulty Difficulty() {return mDifficulty;}
    public String TrailsystemId() {return mTrailsystemId;}
    public String PhotoId() {return mFeaturedPhotoId;}
    public Flags FLAGS() {return mFlags;}
    public String Stars() {
        int count = getInt(RATING);

        if (count < 0) count=0;
        if (count > MAX_STARS) count=MAX_STARS;
        String result = "";
        int x=0;
        while (x<count) {
            result += "★";
            x++;
        }
        while (x<MAX_STARS) {
            result += "☆";
            x++;
        }
        return result;
    }

    public void Save() {
        put(ID, mId);
        put(NAME, mName);
        put(DESCRIPTION, mDescription);
        if (mLocation==null) {
            put(LAT, 0);
            put(LNG, 0);
        } else put(LAT, mLocation.latitude);
        put(LNG, mLocation.longitude);
        put(RATING, mRating);
        put(DIFFICULTY, mDifficulty.toInt());
        put(TRAILSYSTEM_ID, mTrailsystemId);
        put(PHOTO_ID, mFeaturedPhotoId);
        put(FLAGS, mFlags.toInt());
        saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null) Log.d("SaveTrail", "Trail saved");
                else Log.w("SaveTrail", e.getMessage());
            }
        });
    }
    public void Load(String trail_id) {
        ParseQuery<Trail> query = Trail.getQuery();
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<Trail>() {
            @Override
            public void done(Trail trail, ParseException e) {
                if (e == null) {
                    mId = trail.getString(ID);
                    mName = trail.getString(NAME);
                    mDescription = trail.getString(DESCRIPTION);
                    double lat = trail.getDouble(LAT);
                    double lng = trail.getDouble(LNG);
                    if (lat == 0 && lng == 0) mLocation = null;
                    else mLocation = new LatLng(lat, lng);
                    mRating = trail.getInt(RATING);
                    mDifficulty = new Difficulty(getInt(DIFFICULTY));
                    mTrailsystemId = getString(TRAILSYSTEM_ID);
                    mFeaturedPhotoId = getString(PHOTO_ID);
                    mFlags = new Flags(getInt(FLAGS));
                    Log.d("LoadTrail", "trail loaded");
                } else Log.w("LoadTrail", e.getMessage());
            }
        });
    }
    public void Load(String trail_id, final GetCallback callback) {
        ParseQuery<Trail> query = Trail.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(ID, trail_id);
        query.getFirstInBackground(new GetCallback<Trail>() {
            @Override
            public void done(Trail trail, ParseException e) {
                if (e == null) {
                    mId = trail.getString(ID);
                    mName = trail.getString(NAME);
                    mDescription = trail.getString(DESCRIPTION);
                    double lat = trail.getDouble(LAT);
                    double lng = trail.getDouble(LNG);
                    if (lat == 0 && lng == 0) mLocation = null;
                    else mLocation = new LatLng(lat, lng);
                    mRating = trail.getInt(RATING);
                    mDifficulty = new Difficulty(getInt(DIFFICULTY));
                    mTrailsystemId = getString(TRAILSYSTEM_ID);
                    mFeaturedPhotoId = getString(PHOTO_ID);
                    mFlags = new Flags(getInt(FLAGS));
                    Log.d("LoadTrail", "trail loaded");
                } else Log.w("LoadTrail", e.getMessage());
                callback.done(trail, e);
            }
        });
    }
    public static void LoadAllTrails(final FindCallback<Trail> callback) {
        ParseQuery<Trail> query = Trail.getQuery();
        query.fromLocalDatastore();
        query.findInBackground(callback);
    }

}


