package com.huilong.zhang.mobilesafe117.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service {
    private static final String TAG = "LocationService";
    private LocationManager locationManager;
    private SharedPreferences sharedPreferences;
    private MyLocationListener myLocationListener;

    public LocationService() {
    }

    class MyLocationListener implements   LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.v(TAG, "onLocationChanged");
            String logcationstring = "Longitude"+location.getLongitude()+"Latitude"+location.getLatitude();
            sharedPreferences.edit().putString("location",logcationstring).commit();
            Log.v(TAG,logcationstring);
            stopSelf();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.v(TAG, "onStatusChanged");

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.v(TAG, "onProviderEnabled");

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.v(TAG, "onProviderDisabled");

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(myLocationListener);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria,true);
        myLocationListener = new MyLocationListener();
        locationManager.requestLocationUpdates(provider,0,0,myLocationListener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return  null;
    }
}
