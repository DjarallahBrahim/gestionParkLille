package com.example.samlille.gestiondepark

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.getbase.floatingactionbutton.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager

import com.example.samlille.gestiondepark.DataBase.Problem_Entity
import com.example.samlille.gestiondepark.FakeDataBAse.getLocation
import com.example.samlille.gestiondepark.Services.CusomDataBaseService

import kotlinx.android.synthetic.main.activity_main.*

/**
 * Our Main Activity ==> showing all problems
 */
class MainActivity : AppCompatActivity() {

    private val cusomDataBaseService = CusomDataBaseService()
    private lateinit var add: FloatingActionButton
    private lateinit var showProblems: FloatingActionButton
    private lateinit var testMode: FloatingActionButton
    private lateinit var deletDB: FloatingActionButton



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init Input
        initAttributeINPUT()
        //init service/FB
        this.cusomDataBaseService.initDB(this)
        val handler = Handler()
        handler.postDelayed({
            this.cusomDataBaseService.fetchProblemDataFromDb(this, ::bindDataWithUI)
        }, 1000)


        add.setOnClickListener{
            val intent = Intent(this, ProblemDetail_Activity::class.java)
            startActivity(intent)

        }
        testMode.setOnClickListener{
            this.cusomDataBaseService.destroyDataBase()
            val handler = Handler()
            handler.postDelayed({
                getLocation(50.626015, 3.049899,500,this)
                finish()
                startActivity(getIntent())
            }, 1500)

        }
        showProblems.setOnClickListener{
            val intent = Intent(this, map2Activity::class.java).putExtra("ShowProblems","ShowProblems")
            startActivity(intent)
        }
        deletDB.setOnClickListener{
            this.cusomDataBaseService.deletAllFromDB()
            val handler = Handler()
            handler.postDelayed({
                finish()
                startActivity(getIntent())
            }, 1500)

        }
    }

    fun initAttributeINPUT() {
        add = findViewById(R.id.add)
        showProblems = findViewById(R.id.showProblems)
        testMode = findViewById(R.id.testMode)
        deletDB = findViewById(R.id.suppDB)
    }

    fun bindDataWithUI(problemModel: List<Problem_Entity>){
        problem_list.layoutManager = LinearLayoutManager(this)
        problem_list.adapter = Problem_adapter(problemModel as ArrayList<Problem_Entity>,this, this.cusomDataBaseService)

    }

    override fun onDestroy() {
       this.cusomDataBaseService.destroyDataBase()
        super.onDestroy()
    }

    override fun onPause() {
        this.cusomDataBaseService.destroyDataBase()
        super.onPause()
    }

    override fun onBackPressed() {

        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
        finish()
    }

}
