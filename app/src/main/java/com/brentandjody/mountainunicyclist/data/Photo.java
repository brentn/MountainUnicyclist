package com.brentandjody.mountainunicyclist.data;


/**
 * Created by brent on 10/04/15.
 */
public class Photo {
    public enum PHOTO_TYPE {TRAILSYSTEM, TRAIL, FEATURE, PERSON};

    private int _ID = -1;
    private int _UID = -1;
    private int _FLAGS = 0;
    private PHOTO_TYPE _owner_type;
    private int _owner_id;
    private String _dominant_color;
    private byte[] _data;

    public Photo(PHOTO_TYPE owner_type, int owner_id, byte[] data) {
        _owner_type = owner_type;
        _owner_id = owner_id;
        _data = data;
    }
    public Photo(int id, int uid, int flags, PHOTO_TYPE owner_type, int owner_id, String dom_color, byte[] data) {
        _ID = id;
        _UID = uid;
        _FLAGS = flags;
        _owner_type = owner_type;
        _owner_id = owner_id;
        _dominant_color = dom_color;
        _data = data;
    }

    public void setID(int id) {_ID = id;}
    public void setUID(int uid) {_UID=uid;}
    public void setFLAGS(int flags) {_FLAGS=flags;}
    public void setOwnerId(int id) {_owner_id = id;}
    public void setDominantColor(String color) {_dominant_color=color;}

    public int ID() {return _ID;}
    public int UID() {return _UID;}
    public int FLAGS() {return _FLAGS;}
    public PHOTO_TYPE OwnerType() {return _owner_type;}
    public int Owner() {return _owner_id;}
    public String DominantColor() {return _dominant_color;}
    public byte[] Data() {return _data;}

}

