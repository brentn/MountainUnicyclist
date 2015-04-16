package com.brentandjody.mountainunicyclist;

import com.brentandjody.mountainunicyclist.data.Photo;
import com.brentandjody.mountainunicyclist.data.Trail;
import com.brentandjody.mountainunicyclist.data.Trailsystem;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by brent on 07/04/15.
 */
public class Application extends android.app.Application{

    @Override
    public void onCreate()
    {
        super.onCreate();
        initSingletons();
        setupParse();
    }

    protected void initSingletons()
    {
        // Initialize the instance of MySingleton
        //MySingleton.initInstance();
    }

    private void setupParse() {
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Photo.class);
        ParseObject.registerSubclass(Trailsystem.class);
        ParseObject.registerSubclass(Trail.class);
        Parse.initialize(this, "JjnmceWEGxFNaT4sRj2bRxpu9sThtQLM898SMWzB", "UaS1vfpaDrWm5EYCJCvKzilHLL20mwGBVuQRpW5g");
    }
}
