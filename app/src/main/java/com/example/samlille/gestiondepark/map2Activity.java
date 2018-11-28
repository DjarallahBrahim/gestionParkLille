package com.example.samlille.gestiondepark;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.samlille.gestiondepark.Services.MapService;

import java.io.IOException;

/**
 * our map to show/add problems locations
 */
public class map2Activity extends AppCompatActivity {
    private MapService mMapService;
    private static final int AdressRequest = 2;
    private boolean showinPloblem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_map2);

        try{
            if(null != getIntent().getStringExtra("ShowProblem") &&
                    !getIntent().getStringExtra("ShowProblem").isEmpty()){
                this.showinPloblem = true;
                LinearLayout mainLayout=(LinearLayout)this.findViewById(R.id.validationLayour);
                mainLayout.setVisibility(LinearLayout.GONE);

                this.mMapService = new MapService(this);
                this.mMapService.initMap(getIntent().getStringExtra("ShowProblem"),"");
            }else if(null != getIntent().getStringExtra("ShowProblems")){
                this.showinPloblem = true;
                LinearLayout mainLayout=(LinearLayout)this.findViewById(R.id.validationLayour);
                mainLayout.setVisibility(LinearLayout.GONE);

                this.mMapService = new MapService(this);
                this.mMapService.initMap("","ShowProblems");
            }else{
                this.mMapService = new MapService(this);
                this.mMapService.initMap(getIntent().getStringExtra(""),"");
            }
        }catch (Exception e){

        }





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

    @Override
    public void onBackPressed() {
        finish();
    }

}
