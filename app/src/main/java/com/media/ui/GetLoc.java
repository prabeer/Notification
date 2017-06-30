package com.media.ui;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import static android.content.Context.LOCATION_SERVICE;
import static com.media.ui.Logger.logg;

/**
 * Created by prabeer.kochar on 03-03-2017.
 */

public class GetLoc {
    private static double lat = -1.0;

    private static double lon = -1.0;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    protected static String Gloc(Context paramContext) throws Exception {
        try {
            LocationManager locationManager = (LocationManager)
                    paramContext.getSystemService(LOCATION_SERVICE);
            // Criteria criteria = new Criteria();
            // String bestProvider = locationManager.getBestProvider(criteria, true);
            String locationProvider;
            Location location = null;
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    // Called when a new location is found by the network location provider
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationProvider = LocationManager.NETWORK_PROVIDER;
                if (location == null) {
                    locationManager.requestLocationUpdates(locationProvider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(locationProvider);
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }
                logg("GPS:From Network");
            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationProvider = LocationManager.GPS_PROVIDER;
                if (location == null) {
                    locationManager.requestLocationUpdates(locationProvider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(locationProvider);
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }
                logg("GPS:From GPS");
            } else if (locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
                locationProvider = LocationManager.PASSIVE_PROVIDER;
                if (location == null) {
                    locationManager.requestLocationUpdates(locationProvider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(locationProvider);
                        if (location != null) {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }
                logg("GPS:From Passive");
            } else {
                location = null;
                logg("GPS:null");
            }
            try {
                lat = location.getLatitude();
                lon = location.getLongitude();
            } catch (NullPointerException e) {
                lat = -1.0;
                lon = -1.0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Double.toString(lat) + "|" + Double.toString(lon);
    }
}
