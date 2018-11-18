package com.example.samlille.gestiondepark;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.samlille.gestiondepark.Services.MapService;


public class map2Activity extends AppCompatActivity {
    private MapService mMapService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_map2);

        this.mMapService = new MapService(this);
        this.mMapService.initMap();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        this.mMapService.OnrequestPermission(requestCode, permissions, grantResults);
    }


}
