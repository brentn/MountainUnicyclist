package com.brentandjody.mountainunicyclist;

import android.database.sqlite.SQLiteDatabase;

import com.brentandjody.mountainunicyclist.data.Database;
import com.brentandjody.mountainunicyclist.data.Photo;
import com.brentandjody.mountainunicyclist.data.Trail;
import com.brentandjody.mountainunicyclist.data.Trailsystem;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by brent on 07/04/15.
 */
public class Application extends android.app.Application{
    static final int NEW_LOCATION = 1;
    static final int EDIT_TRAIL = 2;
    static final int GET_PHOTO = 3;

    private Database mDatabase;

    @Override
    public void onCreate()
    {
        super.onCreate();
        initSingletons();
        setupParse();
    }

    public Database DB() {return mDatabase;}

    protected void initSingletons()
    {
        mDatabase  = new Database(this);
    }

    private void setupParse() {
        ParseObject.registerSubclass(Photo.class);
        ParseObject.registerSubclass(Trailsystem.class);
        ParseObject.registerSubclass(Trail.class);
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
