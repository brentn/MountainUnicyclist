package com.brentandjody.mountainunicyclist.data;



/**
 * Created by brent on 07/04/15.
 */
public class DBContract {
    private static final int DELETED_FLAG = 0;
    private static final int DIRTY_FLAG = 1;

    public static final class Trailsystem implements IBaseColumns {
        public static final String TABLE_NAME = "trailsystem";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_PHOTO = "photo_id";
    }

    public static final class Trail implements IBaseColumns {
        public static final String TABLE_NAME = "trails";
        public static final String COLUMN_TRAILSYSTEM = "trailsystem_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_PHOTO = "photo_id";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_RATING = "rating";
    }

    public static final class Feature implements IBaseColumns {
        public static final String TABLE_NAME = "features";
        public static final String COLUMN_TRAIL = "trail_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LONG = "long";
        public static final String COLUMN_PHOTO = "photo_id";
    }

    public static final class Photos implements IBaseColumns {
        public static final String TABLE_NAME = "photos";
        public static final String COLUMN_OWNER_TYPE = "owner_type";
        public static final String COLUMN_OWNER_ID = "owner_id";
        public static final String COLUMN_DOMINANT_COLOR = "dominant_color";
        public static final String COLUMN_DATA = "data";
    }

    public static boolean isDeleted(int flags) { return (flags & (1<<DELETED_FLAG)) !=0; }
    public static boolean isDirty(int flags) { return (flags & (1<<DIRTY_FLAG)) !=0; }

    public static int setDeleted(int flags) {return flags | (1<<DELETED_FLAG);}
    public static int setDirty(int flags) {return flags | (1<<DIRTY_FLAG);}

    public static int clearDeleted(int flags) {return flags & ~(1<<DELETED_FLAG);}
    public static int clearDirty(int flags) {return flags & ~(1<<DIRTY_FLAG);}

    public static int Flags(boolean deleted, boolean dirty) {
        int result = 0;
        if (deleted) result &= DELETED_FLAG;
        if (dirty) result &= DIRTY_FLAG;
        return result;
    }
}
