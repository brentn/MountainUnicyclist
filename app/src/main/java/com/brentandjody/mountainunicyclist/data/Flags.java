package com.brentandjody.mountainunicyclist.data;

/**
 * Created by brent on 18/04/15.
 */
public class Flags {
    private static final int DELETED_FLAG = 0;
    private static final int DIRTY_FLAG = 1;
    private static final int TEMP_FLAG = 2;

    private int _flags = 0;

    public Flags() {}
    public Flags(int f) {_flags = f;}

    public int toInt() {return _flags;}
    public boolean isDeleted() { return (_flags & (1<<DELETED_FLAG)) !=0; }
    public boolean isDirty() { return (_flags & (1<<DIRTY_FLAG)) !=0; }
    public boolean isTemporary() {return (_flags & (1<<TEMP_FLAG)) !=0; }

    public int setDeleted() {return _flags | (1<<DELETED_FLAG);}
    public int setDirty() {return _flags | (1<<DIRTY_FLAG);}
    public int setTemporary() {return _flags | (1<<TEMP_FLAG);}

    public int clearDeleted() {return _flags & ~(1<<DELETED_FLAG);}
    public int clearDirty() {return _flags & ~(1<<DIRTY_FLAG);}
    public int clearTemporary() {return _flags & ~(1<<TEMP_FLAG);}
}
