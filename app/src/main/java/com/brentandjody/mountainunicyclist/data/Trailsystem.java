package com.brentandjody.mountainunicyclist.data;

import android.graphics.Point;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by brent on 07/04/15.
 */
public class Trailsystem {
    private int _id;
    private int _uid = 0;
    private int _flags = 0;
    private String _title;
    private String _description;
    private LatLng _coordinates;
    private int _featurePhotoId;

    // Constructors
    public Trailsystem(int lat1000, int lon1000) {
        _coordinates = new LatLng(lat1000/1000, lon1000/1000);
    }
    public Trailsystem(double lat, double lon) {
        _coordinates = new LatLng(lat, lon);
    }
    public Trailsystem(int id, int uid, int flags, String title, String description, int lat1000, int lon1000, int photoId) {
        _id=id;
        _uid=uid;
        _flags=flags;
        _title=title;
        _description = description;
        _coordinates = new LatLng(lat1000/1000, lon1000/1000);
        _featurePhotoId = photoId;
    }
}
