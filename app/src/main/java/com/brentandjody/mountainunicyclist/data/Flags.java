package com.brentandjody.mountainunicyclist.data;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * A class to keep track of bit flags such as "Deleted" or "Dirty"
 * Created by brent on 18/04/15.
 */
@ParseClassName("Flags")
public class Flags extends ParseObject {
    public static final String OBJECT = "object";
    public static final String FLAG = "flag";
    
    private static final int DELETED_FLAG = 0;
    private static final int DIRTY_FLAG = 1;
    private static final int TEMP_FLAG = 2;

    public Flags() {}
    public Flags(String object_id) {
        put(OBJECT, object_id);
        put(FLAG, 0);
        pinInBackground();
    }

    private static ParseQuery<Flags> getQuery() { return ParseQuery.getQuery(Flags.class); }

    public static void Delete(String object_id) {
        ParseQuery<Flags> query = Flags.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(OBJECT, object_id);
        try {
            Flags flags = query.getFirst();
            if (flags==null) flags = new Flags(object_id);
            flags.setDeleted();
            flags.pin();
        }
        catch (ParseException e) { }
    }
    public static boolean isDeleted(String object_id) {
        ParseQuery<Flags> query = Flags.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(OBJECT, object_id);
        try {
            int mFlags = query.getFirst().getInt(FLAG);
            return (mFlags & (1<<DELETED_FLAG)) !=0;
        }
        catch (ParseException e) { return false; }
    }
    public static void setTemporary(String object_id) {
        ParseQuery<Flags> query = Flags.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(OBJECT, object_id);
        try {
            Flags flags = query.getFirst();
            if (flags==null) flags = new Flags(object_id);
            flags.setTemporary();
            flags.pin();
        }
        catch (ParseException e) { }
    }
    public static boolean isTemporary(String object_id) {
        ParseQuery<Flags> query = Flags.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo(OBJECT, object_id);
        try {
            int mFlags = query.getFirst().getInt(FLAG);
            return (mFlags & (1<<TEMP_FLAG)) !=0;
        }
        catch (ParseException e) { return false; }
    }

    //public boolean isDeleted() { return (mFlags & (1<<DELETED_FLAG)) !=0; }
    //public boolean isDirty() { return (mFlags & (1<<DIRTY_FLAG)) !=0; }
    //public boolean isTemporary() {return (mFlags & (1<<TEMP_FLAG)) !=0; }

    private void setDeleted() {
        int mFlags = getInt(FLAG);
        mFlags = mFlags | (1<<DELETED_FLAG);
        put(FLAG, mFlags);
        pinInBackground();
    }
    private void setDirty() {
        int mFlags = getInt(FLAG);
        mFlags = mFlags | (1<<DIRTY_FLAG);
        put(FLAG, mFlags);
        pinInBackground();
    }
    private void setTemporary() {
        int mFlags = getInt(FLAG);
        mFlags = mFlags | (1<<TEMP_FLAG);
        put(FLAG, mFlags);
        pinInBackground();
    }

    private void clearDeleted() {
        int mFlags = getInt(FLAG);
        mFlags = mFlags & ~(1<<DELETED_FLAG);
        put(FLAG, mFlags);
        pinInBackground();
    }
    private void clearDirty() {
        int mFlags = getInt(FLAG);
        mFlags = mFlags & ~(1<<DIRTY_FLAG);
        put(FLAG, mFlags);
        pinInBackground();
    }
    private void clearTemporary() {
        int mFlags = getInt(FLAG);
        mFlags = mFlags & ~(1<<TEMP_FLAG);
        put(FLAG, mFlags);
        pinInBackground();
    }
}
