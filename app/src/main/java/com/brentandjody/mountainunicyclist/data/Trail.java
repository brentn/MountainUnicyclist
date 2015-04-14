package com.brentandjody.mountainunicyclist.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.brentandjody.mountainunicyclist.R;
import com.google.android.gms.maps.model.LatLng;

import static com.brentandjody.mountainunicyclist.R.drawable;
import static com.brentandjody.mountainunicyclist.R.drawable.*;

/**
 * Created by brent on 10/04/15.
 */
public class Trail implements Parcelable {
    public enum Difficulty {NOT_SET, EASY, MEDIUM, DIFFICULT, EXPERT};

    private int _ID = -1;
    private int _UID = -1;
    private int _FLAGS = 0;
    private String _name = "";
    private String _description = "";
    private LatLng _location = null;
    private Difficulty _difficulty = Difficulty.MEDIUM;
    private int _rating = 4; //0 to 8
    private int _trailsystem = -1;
    private int _photoid = -1;

    public Trail() {
    }
    public Trail(Parcel in) {
        _ID = in.readInt();
        _UID = in.readInt();
        _FLAGS = in.readInt();

        _name = in.readString();
        _description = in.readString();
        double lat = in.readDouble();
        double lon = in.readDouble();
        _location = new LatLng(lat,lon);
        _difficulty = Difficulty.values()[in.readInt()];
        _rating = in.readInt();
        _trailsystem = in.readInt();
        _photoid = in.readInt();
    }
    public Trail(int id, int uid, int flags, String name, String description, LatLng location,
                 Difficulty diff, int rating, int trailsystem, int photoid) {
        _ID = id;
        _UID = uid;
        _FLAGS = flags;
        _name = name;
        _description = description;
        _location = location;
        _difficulty = diff;
        _rating = rating;
        _trailsystem = trailsystem;
        _photoid = photoid;
    }

    public void setName(String name) {_name=name;}
    public void setDescription(String desc) {_description=desc;}
    public void setLocation(LatLng loc) {_location=loc;}
    public void setRating(int rating) {
        if (rating < 0) return;
        if (rating > 8) rating=8;
        _rating = rating;
    }
    public void setDifficulty(Difficulty diff) {_difficulty=diff;}
    public void setTrailsystem(int id) {_trailsystem=id;}
    public void setPhotoid(int id) {_photoid=id;}

    public int ID() {return _ID;}
    public int UID() {return _UID;}
    public int FLAGS() {return _FLAGS;}
    public String Name() {return _name;}
    public String Description() {return _description;}
    public LatLng Location() {return _location;}
    public int Rating() {return _rating;}
    public Difficulty Difficulty() {return _difficulty;}
    public int Trailsystem() {return _trailsystem;}
    public int PhotoId() {return _photoid;}

    public int getDifficultyIcon() {
        switch (_difficulty) {
            case NOT_SET:return -1;
            case EASY: return ic_easy;
            case MEDIUM: return ic_medium;
            case DIFFICULT: return ic_difficult;
            case EXPERT: return ic_expert;
        }
        return -1;
    }

    public String getStars() {
        int count = _rating;
        int MAX_STARS = 8;
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
        dest.writeInt(_ID);
        dest.writeInt(_UID);
        dest.writeInt(_FLAGS);
        dest.writeString(_name);
        dest.writeString(_description);
        dest.writeDouble(_location.latitude);
        dest.writeDouble(_location.longitude);
        dest.writeInt(_difficulty.ordinal());
        dest.writeInt(_rating);
        dest.writeInt(_trailsystem);
        dest.writeInt(_photoid);
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


