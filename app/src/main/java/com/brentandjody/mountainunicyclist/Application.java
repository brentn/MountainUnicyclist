package com.brentandjody.mountainunicyclist;

import com.brentandjody.mountainunicyclist.data.LocationsDB;
import com.brentandjody.mountainunicyclist.data.PhotoDB;
import com.parse.Parse;

/**
 * Created by brent on 07/04/15.
 */
public class Application extends android.app.Application{
    private LocationsDB _locations;
    private PhotoDB _photos;

    @Override
    public void onCreate()
    {
        super.onCreate();
        initSingletons();
        setupParse();
    }


    public void openDB() {
        _locations = new LocationsDB(this);
        _photos = new PhotoDB(this);
    }
    public void closeDB() {
        _locations.close();
        _photos.close();
    }

    public LocationsDB getLocations() {return _locations;}
    public PhotoDB getPhotos() {return _photos;}

    protected void initSingletons()
    {
        // Initialize the instance of MySingleton
        //MySingleton.initInstance();
    }

    private void setupParse() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "JjnmceWEGxFNaT4sRj2bRxpu9sThtQLM898SMWzB", "UaS1vfpaDrWm5EYCJCvKzilHLL20mwGBVuQRpW5g");
    }
}
