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
    private static final String FLAGS = "flags";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String LAT = "latitude";
    private static final String LNG = "longitude";


    public Trailsystem(){}

    public static ParseQuery<Trailsystem> getQuery() { return ParseQuery.getQuery(Trailsystem.class); }

    public void setFlags(int flags) {put(FLAGS, flags);}
    public void setTitle(String title) {put(NAME, title);}
    public void setDescription(String description) {put(DESCRIPTION, description);}
    public void setLocation(LatLng location) {
        put(LAT, location.latitude);
        put(LNG, location.longitude);
    }

    public String ID() {return getObjectId();}
    public int Flags() {return getInt(FLAGS);}
    public String Name() {return getString(NAME);}
    public String Description() {return getString(DESCRIPTION);}
    public LatLng Location() {
        double lat = getDouble(LAT);
        double lng = getDouble(LNG);
        return new LatLng(lat, lng);
    }

}
