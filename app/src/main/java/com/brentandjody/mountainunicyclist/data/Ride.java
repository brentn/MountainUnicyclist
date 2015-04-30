package com.brentandjody.mountainunicyclist.data;

/**
 * A class to track dates and times that people ride trails
 * Created by brentn on 29/04/15.
 */

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;


@ParseClassName("Ride")
public class Ride extends ParseObject {
    private static final String TRAIL = "trail_id";
    private static final String DATE = "date";
    private static final String RIDER = "rider_id";
    private static final String CONDITIONS = "trail_conditions";
    private static final String RATING = "rating";

    private static final RideObservable mObservable = new RideObservable();

    public Ride() {}
    public static ParseQuery<Ride> getQuery() { return ParseQuery.getQuery(Ride.class); }

    //setters
    public void setTrailId(String trailId) { put(TRAIL, trailId);}
    public void setDate(Date date) { put(DATE, date.getTime());}
    public void setRiderId(String riderId) { put(RIDER, riderId);}
    public void setConditions(int conditions) { put(CONDITIONS, conditions);}
    public void setRating(int rating) {put(RATING, rating);}

    //getters
    public String TrailId() {return getString(TRAIL);}
    public Date Date() {return new Date(getLong(DATE));}
    public String RiderId() {return getString(RIDER);}
    public int Conditions() {return getInt(CONDITIONS);}
    public int Rating() {return getInt(RATING);}

    // ** Observable
    public static void registerForUpdates(Observer observer) {
        mObservable.addObserver(observer);
    }
    public void notifyObservers() {
        mObservable.notifyObservers(this);
    }
    public void force_update() {
        mObservable.setChanged();
        mObservable.notifyObservers(this);
    }
    private static class RideObservable extends Observable {
        @Override
        public void setChanged() {
            super.setChanged();
        }
    }
}
