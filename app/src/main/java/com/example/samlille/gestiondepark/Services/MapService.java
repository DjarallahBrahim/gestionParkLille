package com.example.samlille.gestiondepark.Services;


import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.samlille.gestiondepark.R;
import com.example.samlille.gestiondepark.map2Activity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class MapService implements OnMapReadyCallback, MapServiceInter {


    private static final String TAG = map2Activity.class.getSimpleName();
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private boolean mLocationPermissionGranted;
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Location mLastKnownLocation;
    private LatLng problemLocation = new LatLng(0, 0);
    private AppCompatActivity context;
    String showproblem = "";
    String showproblems = "";
    boolean drowMarker;

    private CusomDataBaseService cusomDataBaseService;


    /**
     * consterctor
     *
     * @param context
     */
    public MapService(AppCompatActivity context) {
        this.context = context;
        this.cusomDataBaseService = new CusomDataBaseService();
    }

    /**
     * init the map with the nessacery informations
     *
     * @param showProblem
     * @param showproblems
     */
    @Override
    public void initMap(String showProblem, String showproblems) {
        SupportMapFragment mapFragment = (SupportMapFragment) this.context.getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        this.showproblem = showProblem;
        this.showproblems = showproblems;

        if ((null != this.showproblem && !this.showproblem.isEmpty()) ||
                (null != this.showproblems && !this.showproblems.isEmpty())) {
            this.drowMarker = false;
        }

    }


    /**
     * onMapReady
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        getLocationPermission();
        mMap = googleMap;

        updateLocationUI();
        getDeviceLocation();

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Problem Location"));
                problemLocation = latLng;
            }
        });

        if (null != this.showproblem && !this.showproblem.isEmpty()) {
            mMap.clear();
            showproblem.replaceAll("\\s", "");
            String[] location = showproblem.split(",");
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])))
                    .title("Problem Location"));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])), DEFAULT_ZOOM));

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {

                }
            });
        } else if (null != this.showproblems && !this.showproblems.isEmpty()) {
            this.cusomDataBaseService.initDB(this.context);
            this.cusomDataBaseService.fetchLocationFromDB(this.context, (locationsMap) -> {
                for (String location : locationsMap) {
                    location.replaceAll("\\s", "");
                    String[] locatio = location.split(",");
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(locatio[0]), Double.parseDouble(locatio[1])))
                            .title("Problem Location"));
                }
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                    }
                });
                return null;
            });
        }


    }


    /**
     * getLocation
     */
    @Override
    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.context.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this.context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * requestPermission
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void OnrequestPermission(int requestCode,
                                    @NonNull String permissions[],
                                    @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    /**
     * updateLocationUI
     */
    @Override
    public void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {

                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * getDeviceLocation
     */

    @Override
    public void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this.context, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = (Location) task.getResult();
                            if (null == showproblem)
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            problemLocation = new LatLng(mLastKnownLocation.getLatitude(),
                                    mLastKnownLocation.getLongitude());
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * getLocationProblem
     *
     * @return
     * @throws IOException
     */
    @Override
    public String getLocationProblem() throws IOException {

        if (null != problemLocation) {

            return problemLocation.latitude + ", " + problemLocation.longitude;
        }

        return "";

    }

    @Override
    public void cleanMap() {
        if (null != this.mMap)
            this.mMap.clear();
    }

    @Override
    public void killDataBaseInstance() {
        if(null != this.cusomDataBaseService)
            this.cusomDataBaseService.destroyDataBase();
    }

    @Override
    public void showProblem(List<String> locations) {
        for (String location : locations) {
            mMap.clear();
            location.replaceAll("\\s", "");
            String[] locatio = location.split(",");
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(locatio[0]), Double.parseDouble(locatio[1])))
                    .title("Problem Location"));
        }
    }
}
