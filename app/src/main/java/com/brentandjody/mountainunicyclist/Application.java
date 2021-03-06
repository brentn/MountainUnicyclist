package com.brentandjody.mountainunicyclist;

import com.brentandjody.mountainunicyclist.data.Flags;
import com.brentandjody.mountainunicyclist.data.Person;
import com.brentandjody.mountainunicyclist.data.Photo;
import com.brentandjody.mountainunicyclist.data.Ride;
import com.brentandjody.mountainunicyclist.data.Trail;
import com.brentandjody.mountainunicyclist.data.Trailsystem;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Application
 * Created by brent on 07/04/15.
 */
public class Application extends android.app.Application{
    static final int NEW_LOCATION = 1;
    static final int NEW_TRAILSYSTEM = 2;
    static final int EDIT_TRAIL = 3;
    static final int GET_PHOTO = 4;

    @Override
    public void onCreate()
    {
        super.onCreate();
        initSingletons();
        setupParse();
    }

    protected void initSingletons()
    {

    }

    private void setupParse() {
        ParseObject.registerSubclass(Photo.class);
        ParseObject.registerSubclass(Trailsystem.class);
        ParseObject.registerSubclass(Trail.class);
        ParseObject.registerSubclass(Flags.class);
        ParseObject.registerSubclass(Person.class);
        ParseObject.registerSubclass(Ride.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "JjnmceWEGxFNaT4sRj2bRxpu9sThtQLM898SMWzB", "UaS1vfpaDrWm5EYCJCvKzilHLL20mwGBVuQRpW5g");
        ParseUser.enableAutomaticUser();
        ParseUser.getCurrentUser().saveInBackground();
        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
    }
}
