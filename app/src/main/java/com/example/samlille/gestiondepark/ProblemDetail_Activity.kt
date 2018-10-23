package com.example.samlille.gestiondepark

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast


import android.widget.ArrayAdapter
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner
import studio.carbonylgroup.textfieldboxes.ExtendedEditText
import android.widget.AdapterView.OnItemClickListener

class ProblemDetail_Activity : AppCompatActivity() {
    private var mDb: ProblemDataBase? = null

    private lateinit var mType: String
    private lateinit var mDescription: ExtendedEditText
    private lateinit var mLocation: ExtendedEditText
    private lateinit var mAdresse: ExtendedEditText
    private lateinit var sauvegarder: Button

    private lateinit var spinner: MaterialBetterSpinner

    private val mUiHandler = Handler()
    private lateinit var mDbWorkerThread: DbWorkerThread
    var SPINNERLIST = arrayOf("Android Material Design", "Material Design Spinner", "Spinner Using Material Library", "Material Spinner Example")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem_detail)

        //init Input
        initAttributeINPUT()
        initSpinner()


//        fab.setOnClickListener {
////            val problem_Entity = Problem_Entity()
//            //problem_Entity.titre = spinner.
////            problem_Entity.description = mDescription.text.toString()
////            problem_Entity.location = mLocation.text.toString()
////            Toast.makeText(this,"${problem_Entity.titre}+{${problem_Entity.description}}", Toast.LENGTH_LONG).show()
////
////            insertWeatherDataInDb(problem_Entity)
////            val intent = Intent(this, MainActivity::class.java)
////            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
////            startActivity(intent)
//        }
        sauvegarder.setOnClickListener{

            Toast.makeText(this, mDescription.text.toString(),Toast.LENGTH_LONG).show()
        }

    }

    fun initAttributeINPUT() {
        mDescription = findViewById(R.id.description_edit_text)
        mLocation = findViewById(R.id.location_edit_text)
        mAdresse = findViewById(R.id.adresse_edit_text)
        sauvegarder = findViewById(R.id.SauvegarderBTN)


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
    private fun insertWeatherDataInDb(problem_Entity: Problem_Entity) {
        val task = Runnable { mDb?.problemDataDao()?.insert(problem_Entity) }
        mDbWorkerThread.postTask(task)
    }


    
    fun saveProblem(view: View){
        if(view.id == R.id.SauvegarderBTN)
            Toast.makeText(this,"Sauvegarder",Toast.LENGTH_LONG).show()
        else
            Toast.makeText(this,"Annuler",Toast.LENGTH_LONG).show()

    }
    override fun onDestroy() {
        ProblemDataBase.destroyInstance()
        mDbWorkerThread.quit()
        super.onDestroy()
    }

    override fun onPause() {
        ProblemDataBase.destroyInstance()
        mDbWorkerThread.quit()
        super.onPause()
    }

    override fun onResume() {
        //Lancer le WorkerHandler
        mDbWorkerThread = DbWorkerThread("dbWorkerMainThread")
        mDbWorkerThread.start()
        //init DataBAse
        mDb = ProblemDataBase.getInstance(this)

        super.onResume()
    }

}
