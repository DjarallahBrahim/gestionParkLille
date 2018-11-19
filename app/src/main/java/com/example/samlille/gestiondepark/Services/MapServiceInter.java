package com.example.samlille.gestiondepark.Services;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

public interface MapServiceInter {
    public void initMap();
    void getLocationPermission();
    public void OnrequestPermission(int requestCode,
                                    @NonNull String permissions[],
                                    @NonNull int[] grantResults);
    void updateLocationUI();
    void getDeviceLocation();


    String getLocationProblem() throws IOException;

    void cleanMap();
}
