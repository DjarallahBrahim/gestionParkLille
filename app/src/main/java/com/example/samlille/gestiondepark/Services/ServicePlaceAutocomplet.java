package com.example.samlille.gestiondepark.Services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.samlille.gestiondepark.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ServicePlaceAutocomplet implements  GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, PlaceSelectionListener {

    private String TAG = "PlaceLocation";
    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private static final int DEFAULT_ZOOM = 15;

    public void callPlaceSearchIntent(Activity context) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(context);
            context.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

    }

    public void contectmGoogleApiClient(){
        mGoogleApiClient.connect();
    }

    public void discontectmGoogleApiClient(){
        mGoogleApiClient.disconnect();
    }

    public void place(Context context){
        this.mGoogleApiClient = new GoogleApiClient
                .Builder(context)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onPlaceSelected(Place place) {
        Log.i(TAG, "Place Selected: " + place.getName());

        if(place.isDataValid()){
            this.mMap.clear();
            this.mMap.addMarker(new MarkerOptions().
                    position(place.getLatLng()
                    ).title("Problème location"));
            this.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(),DEFAULT_ZOOM));
        }
    }

    @Override
    public void onError(Status status) {

    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

}
