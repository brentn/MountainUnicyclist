package com.brentandjody.mountainunicyclist.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.brentandjody.mountainunicyclist.R;
import com.brentandjody.mountainunicyclist.TrailAdapter;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;
import java.util.UUID;

import static com.brentandjody.mountainunicyclist.R.drawable;
import static com.brentandjody.mountainunicyclist.R.drawable.*;

/**
 * Created by brent on 10/04/15.
 */
@ParseClassName("Trail")
public class Trail extends ParseObject implements Parcelable {
    public enum Difficulty {NOT_SET, EASY, MEDIUM, DIFFICULT, EXPERT};
    private final int MAX_STARS = 8;

    public Trail() {
    }
    public Trail(Parcel in) {
        put(DBContract.Trail._ID, in.readString());
        put(DBContract.Trail.COLUMN_NAME, in.readString());
        put(DBContract.Trail.COLUMN_DESCRIPTION, in.readString());
        double lat = in.readDouble();
        double lng = in.readDouble();
        put(DBContract.Trail.COLUMN_LOCATION, new LatLng(lat,lng));
        put(DBContract.Trail.COLUMN_DIFFICULTY, Difficulty.values()[in.readInt()]);
        put(DBContract.Trail.COLUMN_RATING, in.readInt());
//        put(DBContract.Trail.COLUMN_TRAILSYSTEM, in.readValue());
//        put(DBContract.Trail.COLUMN_PHOTO, in.readArray());
        put(DBContract.Trail._FLAGS, in.readInt());
    }
    public Trail(String id, String name, String description, LatLng location,
                Difficulty difficulty, int rating, Trailsystem trailsystem, Photo photo, int flags) {
        put(DBContract.Trail._ID, id);
        put(DBContract.Trail.COLUMN_NAME, name);
        put(DBContract.Trail.COLUMN_DESCRIPTION, description);
        put(DBContract.Trail.COLUMN_LOCATION, location);
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
    public void setLocation(LatLng location) { put(DBContract.Trail.COLUMN_LOCATION, location); }
    public void setRating(int rating) {
        if (rating < 0) return;
        if (rating > MAX_STARS) rating=MAX_STARS;
        put(DBContract.Trail.COLUMN_RATING, rating);
    }
    public void setDifficulty(Difficulty difficulty) { put(DBContract.Trail.COLUMN_DIFFICULTY, difficulty);}
    public void setTrailsystem(ParseObject trailsystem) { put(DBContract.Trail.COLUMN_TRAILSYSTEM, trailsystem);}
    public void setPhoto(Photo photo) { put(DBContract.Trail.COLUMN_PHOTO, photo);}
    public void setFlags(int flags) { put(DBContract.Trail._FLAGS, flags);}

    public String ID() {return getString(DBContract.Trail._ID);}
    public String Name() {return getString(DBContract.Trail.COLUMN_NAME);}
    public String Description() {return getString(DBContract.Trail.COLUMN_DESCRIPTION);}
    public LatLng Location() {return (LatLng) get(DBContract.Trail.COLUMN_LOCATION);}
    public int Rating() {return getInt(DBContract.Trail.COLUMN_RATING);}
    public Difficulty Difficulty() {return (Difficulty) get(DBContract.Trail.COLUMN_DIFFICULTY);}
    public Trailsystem Trailsystem() {return (Trailsystem) get(DBContract.Trail.COLUMN_TRAILSYSTEM);}
    public Photo Photo() {return (Photo) get(DBContract.Trail.COLUMN_PHOTO);}
    public int FLAGS() {return getInt(DBContract.Trail._FLAGS);}

    public static void LoadTrailAdapter(final TrailAdapter adapter) {
        ParseQuery<Trail> query = ParseQuery.getQuery("Trail");
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<com.brentandjody.mountainunicyclist.data.Trail>() {
            @Override
            public void done(List<Trail> trails, ParseException e) {
                adapter.Fill(trails);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getString(DBContract.Trail._ID));
        dest.writeString(getString(DBContract.Trail.COLUMN_NAME));
        dest.writeString(getString(DBContract.Trail.COLUMN_DESCRIPTION));
        LatLng location = (LatLng) get(DBContract.Trail.COLUMN_LOCATION);
        dest.writeDouble(location.latitude);
        dest.writeDouble(location.longitude);
        Difficulty difficulty = (Difficulty) get(DBContract.Trail.COLUMN_DIFFICULTY);
        dest.writeInt(difficulty.ordinal());
        dest.writeInt(getInt(DBContract.Trail.COLUMN_RATING));
        //dest.writeSerializable((Trailsystem) get(DBContract.Trail.COLUMN_TRAILSYSTEM));
        //dest.writeSerializable((Photo) get(DBContract.Trail.COLUMN_PHOTO));
        dest.writeInt(getInt(DBContract.Trail._FLAGS));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Trail createFromParcel(Parcel in){
            return new Trail(in);
        }

        @Override
        public Object[] newArray(int size) {
            return new Trail[size];
        }

    };
}


