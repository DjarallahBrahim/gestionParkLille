package com.example.samlille.gestiondepark;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.samlille.gestiondepark.DataBase.Problem_Entity;
import com.example.samlille.gestiondepark.Services.CusomDataBaseService;

public class problemAfficher extends AppCompatActivity {
    private TextView type, description, location, adresse;
    private CusomDataBaseService cusomDataBaseService;
    private Problem_Entity problem_entity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problaim_afficher);

        initInput();


        if(null != getIntent().getStringExtra("ShowProblem") &&
                !getIntent().getStringExtra("ShowProblem").isEmpty()){


            fetchProblem();
        }

    }

    private void fetchProblem() {
        this.cusomDataBaseService.initDB(this);
        Handler handler = new Handler();
        handler.postDelayed(()->{
                this.cusomDataBaseService.getElementById(this,
                        getIntent().getLongExtra("id",0),
                        (problem_entity) -> {
                            type.setText(problem_entity.getType());
                            description.setText(problem_entity.getDescription());
                            location.setText(problem_entity.getLocation());
                            adresse.setText(problem_entity.getAdresse());
                            return null;
                        });
        }, 1500);

    }

    public void initInput(){
        type = (TextView)findViewById(R.id.typeView);
        description = (TextView)findViewById(R.id.descriptionView);
        location = (TextView)findViewById(R.id.locationView);
        adresse = (TextView)findViewById(R.id.addressView);
        this.cusomDataBaseService = new CusomDataBaseService();
    }


    public void showInMap(View view) {
        startActivity(new Intent(this, map2Activity.class).putExtra("ShowProblem",getIntent().getStringExtra("ShowProblem"))
                .putExtra("id",getIntent().getStringExtra("id")));
    }

    public void suppProblem(View view) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        //On donne un titre à l'AlertDialog
        adb.setTitle("Voulez-vous supprimer ce problème?");

        //On modifie l'icône de l'AlertDialog pour le fun ;)
        adb.setIcon(android.R.drawable.ic_dialog_alert);

        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
        adb.setPositiveButton("Oui", (dialog, which) -> {
            cusomDataBaseService.deletById(getIntent().getLongExtra("id",0));
            cusomDataBaseService.destroyDataBase();
            startActivity(new Intent(problemAfficher.this, MainActivity.class));
            finish();
        });

        adb.setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss());
        adb.show();
    }

    @Override
    public void onBackPressed() {
        this.cusomDataBaseService.destroyDataBase();
        startActivity(new Intent(problemAfficher.this, MainActivity.class));
        finish();
        super.onBackPressed();
    }
}
