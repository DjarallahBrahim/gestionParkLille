package com.example.samlille.gestiondepark.Services;

import android.support.annotation.NonNull;

public interface MapServiceInter {
    public void initMap();
    void getLocationPermission();
    public void OnrequestPermission(int requestCode,
                                    @NonNull String permissions[],
                                    @NonNull int[] grantResults);
    void updateLocationUI();
    void getDeviceLocation();
}
