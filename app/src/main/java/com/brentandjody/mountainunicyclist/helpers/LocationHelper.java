package com.brentandjody.mountainunicyclist.helpers;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by brent on 11/04/15.
 */
public class LocationHelper {
    public static Location getGPS(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(
                Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);

        Location l = null;

        for (int i=providers.size()-1; i>=0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) break;
        }

//        LatLng result = null;
//        if (l != null) {
//            result = new LatLng(l.getLatitude(), l.getLongitude());
//        }
        return l;
    }
}
