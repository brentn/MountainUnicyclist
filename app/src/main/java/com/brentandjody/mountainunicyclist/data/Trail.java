package com.brentandjody.mountainunicyclist.data;

import android.util.Log;

import com.brentandjody.mountainunicyclist.TrailAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.UUID;

import static com.brentandjody.mountainunicyclist.R.drawable.ic_difficult;
import static com.brentandjody.mountainunicyclist.R.drawable.ic_easy;
import static com.brentandjody.mountainunicyclist.R.drawable.ic_expert;
import static com.brentandjody.mountainunicyclist.R.drawable.ic_medium;

/**
 * Created by brent on 10/04/15.
 */
@ParseClassName("Trail")
public class Trail extends ParseObject {
    public enum Difficulty {NOT_SET, EASY, MEDIUM, DIFFICULT, EXPERT}
    private final int MAX_STARS = 8;

    public Trail() {}
    public Trail(String id, String name, String description, LatLng location,
                Difficulty difficulty, int rating, Trailsystem trailsystem, Photo photo, int flags) {
        put(DBContract.Trail._UID, id);
        put(DBContract.Trail.COLUMN_NAME, name);
        put(DBContract.Trail.COLUMN_DESCRIPTION, description);
        put(DBContract.Trail.COLUMN_LAT, location.latitude);
        put(DBContract.Trail.COLUMN_LNG, location.longitude);
        put(DBContract.Trail.COLUMN_DIFFICULTY, difficulty);
        put(DBContract.Trail.COLUMN_RATING, rating);
        put(DBContract.Trail.COLUMN_TRAILSYSTEM, trailsystem);
        put(DBContract.Trail.COLUMN_PHOTO, photo);
        put(DBContract.Trail._FLAGS, flags);

    }

    public static ParseQuery<Trail> getQuery() {
        return ParseQuery.getQuery(Trail.class);
    }

    public void setID() {
        UUID uuid = UUID.randomUUID();
        put(DBContract.Trail._UID, uuid.toString());
    }
    public void setName(String name) { put(DBContract.Trail.COLUMN_NAME, name);}
    public void setDescription(String description) { put(DBContract.Trail.COLUMN_DESCRIPTION, description); }
    public void setLocation(LatLng location) {
        if (location==null) return;
        put(DBContract.Trail.COLUMN_LAT, location.latitude);
        put(DBContract.Trail.COLUMN_LNG, location.longitude);
    }
    public void setRating(int rating) {
        if (rating < 0) return;
        if (rating > MAX_STARS) rating=MAX_STARS;
        put(DBContract.Trail.COLUMN_RATING, rating);
    }
    public void setDifficulty(Difficulty difficulty) { put(DBContract.Trail.COLUMN_DIFFICULTY, difficulty.ordinal());}
    public void setTrailsystem(ParseObject trailsystem) {
        if (trailsystem==null) return;
        put(DBContract.Trail.COLUMN_TRAILSYSTEM, trailsystem);
    }
    public void setPhoto(Photo photo) {
        if (photo==null) return;
        put(DBContract.Trail.COLUMN_PHOTO, photo);
    }
    public void setFlags(int flags) { put(DBContract.Trail._FLAGS, flags);}

    public String ID() {return getString(DBContract.Trail._UID);}
    public String Name() {return getString(DBContract.Trail.COLUMN_NAME);}
    public String Description() {return getString(DBContract.Trail.COLUMN_DESCRIPTION);}
    public LatLng Location() {
        double lat = getDouble(DBContract.Trail.COLUMN_LAT);
        double lng = getDouble(DBContract.Trail.COLUMN_LNG);
        return new LatLng(lat, lng);
    }
    public int Rating() {return getInt(DBContract.Trail.COLUMN_RATING);}
    public Difficulty Difficulty() {return Difficulty.values()[getInt(DBContract.Trail.COLUMN_DIFFICULTY)];}
    public Trailsystem Trailsystem() {return (Trailsystem) get(DBContract.Trail.COLUMN_TRAILSYSTEM);}
    public Photo Photo() {return (Photo) get(DBContract.Trail.COLUMN_PHOTO);}
    public int FLAGS() {return getInt(DBContract.Trail._FLAGS);}

    public static void LoadTrailAdapter(final TrailAdapter adapter) {
        ParseQuery<Trail> query = ParseQuery.getQuery("Trail");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<com.brentandjody.mountainunicyclist.data.Trail>() {
            @Override
            public void done(List<Trail> trails, ParseException e) {
                adapter.LoadData(trails);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public int getDifficultyIcon() {
        switch (Difficulty.values()[getInt(DBContract.Trail.COLUMN_DIFFICULTY)]) {
            case NOT_SET:return -1;
            case EASY: return ic_easy;
            case MEDIUM: return ic_medium;
            case DIFFICULT: return ic_difficult;
            case EXPERT: return ic_expert;
        }
        return -1;
    }
    public String getStars() {
        int count = getInt(DBContract.Trail.COLUMN_RATING);

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

}


