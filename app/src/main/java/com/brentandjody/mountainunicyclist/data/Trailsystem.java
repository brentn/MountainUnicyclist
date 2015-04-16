package com.brentandjody.mountainunicyclist.data;

import android.graphics.Point;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.UUID;

/**
 * Created by brent on 07/04/15.
 */
@ParseClassName("Trailsystem")
public class Trailsystem extends ParseObject {


    // Constructors
    public Trailsystem() {
        setID();
    }
    public Trailsystem(int id) {
        put(DBContract.Trailsystem._UID, id);
    }
    public Trailsystem(LatLng location) {
        setID();
        setLocation(location);
    }
    public Trailsystem(double lat, double lon) {
        setID();
        setLocation(new LatLng(lat, lon));
    }
    public Trailsystem(String id, int flags, String title, String description, LatLng location, Photo photo) {
        put(DBContract.Trailsystem._UID, id);
        setFlags(flags);
        setTitle(title);
        setDescription(description);
        setLocation(location);
        setPhoto(photo);
    }

    public static ParseQuery<Trailsystem> getQuery() {
        return ParseQuery.getQuery(Trailsystem.class);
    }


    public void setID() {
        UUID uuid = UUID.randomUUID();
        put(DBContract.Trailsystem._UID, uuid.toString());
    }
    public void setFlags(int flags) {put(DBContract.Trailsystem._FLAGS, flags);}
    public void setTitle(String title) {put(DBContract.Trailsystem.COLUMN_TITLE, title);}
    public void setDescription(String description) {put(DBContract.Trailsystem.COLUMN_DESCRIPTION, description);}
    public void setLocation(LatLng location) {put(DBContract.Trailsystem.COLUMN_LOCATION, location);}
    public void setPhoto(Photo photo) {put(DBContract.Trailsystem.COLUMN_PHOTO, photo);}

    public String ID() {return getString(DBContract.Trailsystem._UID);}
    public int Flags() {return getInt(DBContract.Trailsystem._FLAGS);}
    public String Title() {return getString(DBContract.Trailsystem.COLUMN_TITLE);}
    public String Description() {return getString(DBContract.Trailsystem.COLUMN_DESCRIPTION);}
    public LatLng Location() {return (LatLng) get(DBContract.Trailsystem.COLUMN_LOCATION);}
    public Photo Photo() {return (Photo) getParseObject(DBContract.Trailsystem.COLUMN_PHOTO);}


}
