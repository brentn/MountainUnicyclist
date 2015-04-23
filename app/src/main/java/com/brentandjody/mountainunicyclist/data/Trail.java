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
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

/**
 * Created by brent on 10/04/15.
 */
@ParseClassName("Trail")
public class Trail extends ParseObject {
    private static final String ID = "objectId"; //don't change this name
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

    private static final TrailObservable mObservable = new TrailObservable();

    public Trail() {}

    public static ParseQuery<Trail> getQuery() { return ParseQuery.getQuery(Trail.class); }

    public void setName(String name) {
        put(NAME, name);
        mObservable.setChanged();
    }
    public void setDescription(String description) {
        put(DESCRIPTION, description);
        mObservable.setChanged();
    }
    public void setLocation(LatLng location) {
        put(LAT, location.latitude);
        put(LNG, location.longitude);
        mObservable.setChanged();
    }
    public void setRating(int rating) {
        if (rating < 0) return;
        if (rating > MAX_STARS) rating=MAX_STARS;
        put(RATING, rating);
        mObservable.setChanged();
    }
    public void setDifficulty(Difficulty difficulty) {
        put(DIFFICULTY, difficulty.toInt());
        mObservable.setChanged();
    }
    public void setTrailsystem(String trailsystem_id) {
        put(TRAILSYSTEM_ID, trailsystem_id);
        mObservable.setChanged();
    }
    public void setPhotoId(String photo_id) {
        put(PHOTO_ID, photo_id);
        mObservable.setChanged();
    }
    public void setFlags(Flags flags) {
        put(FLAGS, flags.toInt());
        mObservable.setChanged();
    }
    public void force_update() {
        mObservable.setChanged();
        mObservable.notifyObservers(this);
    }
    public void notifyObservers() {
        mObservable.notifyObservers(this);
    }

    public String ID() {return getObjectId();}
    public String Name() {return getString(NAME);}
    public String Description() {return getString(DESCRIPTION);}
    public LatLng Location() {
        double lat = getDouble(LAT);
        double lng = getDouble(LNG);
        return new LatLng(lat, lng);
    }
    public int Rating() {return getInt(RATING);}
    public Difficulty Difficulty() {return new Difficulty(getInt(DIFFICULTY));}
    public String TrailsystemId() {return getString(TRAILSYSTEM_ID);}
    public String PhotoId() {return getString(PHOTO_ID);}
    public Flags FLAGS() {return new Flags(getInt(FLAGS));}
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

    public void Load(String trail_id) {
        ParseQuery<Trail> query = Trail.getQuery();
        query.fromLocalDatastore();
        query.getInBackground(trail_id);
    }
    public static void Load(String trail_id, final GetCallback callback) {
        ParseQuery<Trail> query = Trail.getQuery();
        query.fromLocalDatastore();
        query.getInBackground(trail_id, new GetCallback<Trail>() {
            @Override
            public void done(Trail trail, ParseException e) {
                if (e == null) Log.d("LoadTrail", "trail loaded");
                else Log.w("LoadTrail", e.getMessage());
                callback.done(trail, e);
            }
        });
    }

    public static void LoadAllTrails(final FindCallback<Trail> callback) {
        ParseQuery<Trail> query = Trail.getQuery();
        query.fromLocalDatastore();
        query.findInBackground(callback);
    }

    public static void registerForUpdates(Observer observer) {
        mObservable.addObserver(observer);
    }

    private static class TrailObservable extends Observable {
        @Override
        public void setChanged() {
            super.setChanged();
        }
    }
}


