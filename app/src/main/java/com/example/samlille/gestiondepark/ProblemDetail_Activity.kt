package com.example.samlille.gestiondepark

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


import android.widget.ArrayAdapter
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import studio.carbonylgroup.textfieldboxes.ExtendedEditText
import android.widget.AdapterView.OnItemClickListener
import com.example.samlille.gestiondepark.DataBase.Problem_Entity
import com.example.samlille.gestiondepark.Services.ServicePlaceAutocomplet
import com.example.samlille.gestiondepark.Services.ServiceProblemDetailImp
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes

class ProblemDetail_Activity : AppCompatActivity() {

    private var mType = "NO TYPE"
    private lateinit var mDescription: ExtendedEditText
    private lateinit var mLocation: ExtendedEditText
    private lateinit var mAdresse : ExtendedEditText
    private lateinit var findAdressField : TextFieldBoxes
    private lateinit var findLocationField : TextFieldBoxes
    private lateinit var sauvegarder: Button
    private lateinit var spinner: MaterialBetterSpinner
    private var SPINNERLIST = arrayOf("Arbre à tailler", "Arbre à abattre", "Détritus", "Haie à tailler", "Mauvaise herbe", "Autre")

    private val PLACE_AUTOCOMPLETE_REQUEST_CODE = 1
    val AdressRequest = 2

    private val servicePlaceAutocomplet = ServicePlaceAutocomplet()

    private var serviceProblemDetail = ServiceProblemDetailImp()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem_detail)

        //init Input
        initAttributeINPUT()
        initSpinner()

        servicePlaceAutocomplet.place( this)

        sauvegarder.setOnClickListener{
            getInfoAndSaveIt()

        }

        findAdressField.getEndIconImageButton().setOnClickListener{
            servicePlaceAutocomplet.callPlaceSearchIntent(this)
        }

        findLocationField.getEndIconImageButton().setOnClickListener{
            startActivity(Intent(this, map2Activity::class.java))
        }


    }

    private fun getInfoAndSaveIt() {
        val description = mDescription.text.toString()
        val location = mLocation.text.toString()
        val adresse = mAdresse.text.toString()


        if (!serviceProblemDetail.checkInput(mType,
                        location)) {
            showDialogError()


        } else {
            val problemEntity = Problem_Entity()
            problemEntity.description = description
            problemEntity.type = mType
            problemEntity.location = location
            problemEntity.adresse = location
            serviceProblemDetail.insertProblemDataInDb(problemEntity)
            Toast.makeText(this, "Le problème est sauvegardé ", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun initAttributeINPUT() {
        mDescription = findViewById(R.id.description_edit_text)
        mLocation = findViewById(R.id.location_edit_text)
        mAdresse = findViewById(R.id.adresse_edit_text)
        sauvegarder = findViewById(R.id.SauvegarderBTN)

        findAdressField = findViewById(R.id.adresse_field_boxes)
        findLocationField = findViewById(R.id.location_field_boxes)

    }

    fun initSpinner() {
        val arrayAdapter = ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, SPINNERLIST)
        spinner = findViewById<View>(R.id.typeSpinner) as MaterialBetterSpinner
        spinner.setAdapter(arrayAdapter)

        spinner.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            this.mType = (view as TextView).text.toString()
        }
        spinner.setBackgroundColor(Color.WHITE)
    }

    fun showDialogError(){
        // Initialize a new instance of
        val builder = AlertDialog.Builder(this@ProblemDetail_Activity)

        // Set the alert dialog title
        builder.setTitle("Erreur avec vos informatios !")

        // Display a message on alert dialog
        builder.setMessage("Remplir tous les champs SVP !")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("OK"){dialog, which ->
            // Do something when user press the positive button
            Toast.makeText(applicationContext,"Ok",Toast.LENGTH_SHORT).show()

            // Change the app background color

        }
        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data)
                this.mAdresse.setText(place.address.toString())
                Toast.makeText(applicationContext,place.toString(),Toast.LENGTH_SHORT).show()
                Log.i("TAGPlace", "Place:" + place.address)

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(this, data)
                Toast.makeText(applicationContext,status.toString(),Toast.LENGTH_SHORT).show()

            } else if (resultCode == RESULT_CANCELED) {

            }
        }else if(resultCode == AdressRequest){
            intent = getIntent() as Intent
            var problemlocation = intent.getStringExtra("problemLocation") as String
            this.mLocation.setText(problemlocation)
            Toast.makeText(applicationContext,problemlocation,Toast.LENGTH_SHORT).show()

        }
    }
    

    override fun onDestroy() {
        super.onDestroy()
        serviceProblemDetail.exitDataBase()

    }

    override fun onPause() {
        super.onPause()
        serviceProblemDetail.exitDataBase()
    }

    override fun onResume() {
        super.onResume()
        serviceProblemDetail.openDataBase(this)

        intent = getIntent() as Intent
        val problemlocation = intent.getStringExtra("problemLocation")
        if(null != problemlocation) {
            this.mLocation.setText(problemlocation)
            Toast.makeText(applicationContext, problemlocation, Toast.LENGTH_SHORT).show()
        }
    }

}
