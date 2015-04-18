package com.brentandjody.mountainunicyclist.data;



/**
 * Created by brent on 07/04/15.
 */
public class DBContract {

    public static final class Trailsystem implements IBaseColumns {
        public static final String TABLE_NAME = "trailsystem";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_PHOTO = "photoid";
    }

    public static final class Feature implements IBaseColumns {
        public static final String TABLE_NAME = "features";
        public static final String COLUMN_TRAIL = "trailid";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LONG = "long";
        public static final String COLUMN_PHOTO = "photoid";
    }



}
