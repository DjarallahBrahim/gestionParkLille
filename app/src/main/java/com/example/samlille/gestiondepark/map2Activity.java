package com.example.samlille.gestiondepark;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.samlille.gestiondepark.Services.MapService;

import java.io.IOException;


public class map2Activity extends AppCompatActivity {
    private MapService mMapService;
    public static final int AdressRequest = 2;
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


    public void validerLocation(View view) throws IOException {
        String adresse = this.mMapService.getLocationProblem();
        Intent intent = new Intent(this,
                ProblemDetail_Activity.class);
        intent.putExtra("problemLocation", adresse);
        startActivityForResult(intent , AdressRequest);
        finish();
    }

    public void supppLocation(View view) {
        this.mMapService.cleanMap();
    }
}
