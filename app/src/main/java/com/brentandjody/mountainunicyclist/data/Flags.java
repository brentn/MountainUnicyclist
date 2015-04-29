package com.brentandjody.mountainunicyclist.data;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by brent on 18/04/15.
 */
@ParseClassName("Flags")
public class Flags extends ParseObject {
    public static final String OBJECT = "object";
    public static final String FLAG = "flag";
    
    private static final int DELETED_FLAG = 0;
    private static final int DIRTY_FLAG = 1;
    private static final int TEMP_FLAG = 2;

    private int mFlags;

    public Flags() { mFlags=0; }
    public Flags(String object_id) {
        mFlags = 0;
        put(OBJECT, object_id);
        put(FLAG, 0);
        pinInBackground();
    }
    public Flags(String object_id, int f) {
        mFlags = f;
        put(OBJECT, object_id);
        put(FLAG, f);
        pinInBackground();
    }

    public static ParseQuery<Flags> getQuery() { return ParseQuery.getQuery(Flags.class); }


    public int toInt() {return mFlags;}
    public boolean isDeleted() { return (mFlags & (1<<DELETED_FLAG)) !=0; }
    public boolean isDirty() { return (mFlags & (1<<DIRTY_FLAG)) !=0; }
    public boolean isTemporary() {return (mFlags & (1<<TEMP_FLAG)) !=0; }

    public void setDeleted() {
        mFlags = mFlags | (1<<DELETED_FLAG);
        put(FLAG, mFlags);
    }
    public void setDirty() {
        mFlags = mFlags | (1<<DIRTY_FLAG);
        put(FLAG, mFlags);
    }
    public void setTemporary() {
        mFlags = mFlags | (1<<TEMP_FLAG);
        put(FLAG, mFlags);
    }

    public void clearDeleted() {
        mFlags = mFlags & ~(1<<DELETED_FLAG);
        put(FLAG, mFlags);
    }
    public void clearDirty() {
        mFlags = mFlags & ~(1<<DIRTY_FLAG);
        put(FLAG, mFlags);
    }
    public void clearTemporary() {
        mFlags = mFlags & ~(1<<TEMP_FLAG);
        put(FLAG, mFlags);
    }
}
