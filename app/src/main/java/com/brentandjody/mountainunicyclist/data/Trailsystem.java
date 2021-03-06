package com.brentandjody.mountainunicyclist.data;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.brentandjody.mountainunicyclist.R;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A class to manage groups of trails, centered on the parking location
 * Created by brent on 07/04/15.
 */
@ParseClassName("Trailsystem")
public class Trailsystem extends ParseObject {
    public static final String ID_EXTRA = "TRAILSYSTEM_ID";
    public static final String NONE = "NONE";
    public static final String NEW = "NEW";

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String DIRECTIONS = "directions";
    private static final String LAT = "latitude";
    private static final String LNG = "longitude";


    public Trailsystem(){}

    public static ParseQuery<Trailsystem> getQuery() { return ParseQuery.getQuery(Trailsystem.class); }

    public void setTitle(String title) {put(NAME, title);}
    public void setDescription(String description) {put(DESCRIPTION, description);}
    public void setDirections(String directions) {put(DIRECTIONS, directions);}
    public void setLocation(LatLng location) {
        put(LAT, location.latitude);
        put(LNG, location.longitude);
    }
    public void Delete() {
        Flags.Delete(getObjectId());
        //force_update();
    }

    public String ID() {return getObjectId();}
    public String Name() {return getString(NAME);}
    public String Description() {return getString(DESCRIPTION);}
    public String Directions() {return getString(DIRECTIONS);}
    public LatLng Location() {
        double lat = getDouble(LAT);
        double lng = getDouble(LNG);
        return new LatLng(lat, lng);
    }
    public boolean isDeleted() { return Flags.isDeleted(getObjectId()); }

    public static void LoadInto(String trailsystem_id, final TextView view) {
        ParseQuery<Trailsystem> query = Trailsystem.getQuery();
        query.fromLocalDatastore();
        query.getInBackground(trailsystem_id, new GetCallback<Trailsystem>() {
            @Override
            public void done(Trailsystem trailsystem, ParseException e) {
                if (e==null) {
                    view.setText(trailsystem.Name());
                }
            }
        });
    }

    public static void LoadAll(final Spinner spinner, final String trailsystem_id) {
        final ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        adapter.clear();
        adapter.add(new ListItem(adapter.getContext().getResources().getString(R.string.no_trailsystem), NONE));
        ParseQuery<Trailsystem> query = Trailsystem.getQuery();
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<Trailsystem>() {
            @Override
            public void done(List<Trailsystem> list, ParseException e) {
                if (e==null) {
                    for (Trailsystem ts : list) {
                        ListItem item = new ListItem(ts.Name(), ts.ID());
                        adapter.add(item);
                        if (ts.ID().equals(trailsystem_id)) {
                            Log.d("LoadAllTrailsystems", "Setting selected value");
                            spinner.setSelection(adapter.getPosition(item));
                        }
                        Log.d("LoadAllTrailsystems", "succeeded loading "+ list.size() + " trailsystems");
                    }
                    adapter.add(new ListItem(adapter.getContext().getResources().getString(R.string.new_trailsystem),NEW));
                    adapter.notifyDataSetChanged();
                } else Log.w("LoadAllTrailsystems", e.getMessage());
            }
        });
    }



}
