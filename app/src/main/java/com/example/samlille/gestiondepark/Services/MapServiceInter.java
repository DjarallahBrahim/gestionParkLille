package com.example.samlille.gestiondepark.Services;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;

public interface MapServiceInter {

    void initMap(String showProblem, String showproblems);
    void getLocationPermission();
    void OnrequestPermission(int requestCode,
                                    @NonNull String permissions[],
                                    @NonNull int[] grantResults);
    void updateLocationUI();
    void getDeviceLocation();
    String getLocationProblem() throws IOException;
    void cleanMap();

    void killDataBaseInstance();

    void showProblem(List<String> locations);
}
