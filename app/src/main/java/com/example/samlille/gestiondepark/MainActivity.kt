package com.example.samlille.gestiondepark

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.samlille.gestiondepark.DataBase.DbWorkerThread
import com.example.samlille.gestiondepark.DataBase.ProblemDataBase
import com.example.samlille.gestiondepark.DataBase.Problem_Entity
import com.example.samlille.gestiondepark.R.layout.problem_item
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mDb: ProblemDataBase? = null

    private lateinit var add: Button
//    private lateinit var mTitle: TextView
//    private lateinit var mDescription: TextView
//    private lateinit var mLocation: TextView

    private lateinit var mDbWorkerThread: DbWorkerThread

    private val mUiHandler = Handler()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init Input
        initAttributeINPUT()


        add.setOnClickListener{
            val intent = Intent(this, ProblemDetail_Activity::class.java)
            startActivity(intent)
        }
    }

    fun initAttributeINPUT() {
//        mTitle = findViewById(R.id.titleProblem)
//        mDescription = findViewById(R.id.descriptionProblem)
//        mLocation = findViewById(R.id.locationProblem)
        add = findViewById(R.id.add)



    }

//    fun getNewProblem(){
//        val problemTitle= intent.getStringExtra("title")
//        val problemDescription=intent.getStringExtra("description")
//        val problemLocation=intent.getStringExtra("location")
//
//       if(problemTitle != null && problemDescription != null && problemLocation != null) {
//           mTitle.text = problemTitle.toString()
//           mDescription.text = problemDescription.toString()
//           mLocation.text = problemLocation.toString()
//       }
//    }

    private fun bindDataWithUI(problemModel: List<Problem_Entity>){

        problem_list.layoutManager = LinearLayoutManager(this)
        problem_list.adapter = Problem_adapter(problemModel as ArrayList<Problem_Entity>,this)

    }

    private fun fetchProblemDataFromDb() {
        val task = Runnable {
            val problemData =
                    mDb?.problemDataDao()?.getAllProblem()
            mUiHandler.post({
                if (problemData == null || problemData?.size == 0) {
                    Toast.makeText(this,"No Problem in data base",Toast.LENGTH_LONG).show()
                } else {
                    bindDataWithUI(problemData)
                }
            })
        }
        mDbWorkerThread.postTask(task)
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
        fetchProblemDataFromDb()
        super.onResume()
    }
}
