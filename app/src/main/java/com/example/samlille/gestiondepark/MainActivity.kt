package com.example.samlille.gestiondepark

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
        this.cusomDataBaseService.fetchProblemDataFromDb(this, ::bindDataWithUI)

        add.setOnClickListener{
            val intent = Intent(this, ProblemDetail_Activity::class.java)
            startActivity(intent)

        }
        testMode.setOnClickListener{
            this.cusomDataBaseService.destroyDataBase()
            getLocation(50.626015, 3.049899,500,this)
            finish()
            startActivity(getIntent())
        }
        showProblems.setOnClickListener{
            val intent = Intent(this, map2Activity::class.java).putExtra("ShowProblems","ShowProblems")
            startActivity(intent)
        }
        deletDB.setOnClickListener{
            this.cusomDataBaseService.deletAllFromDB()
            finish()
            startActivity(getIntent())
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

}
