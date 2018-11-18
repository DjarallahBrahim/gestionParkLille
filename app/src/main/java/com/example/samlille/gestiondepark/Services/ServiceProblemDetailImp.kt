package com.example.samlille.gestiondepark.Services

import android.content.Context
import android.os.Handler
import com.example.samlille.gestiondepark.DataBase.DbWorkerThread
import com.example.samlille.gestiondepark.DataBase.ProblemDataBase
import com.example.samlille.gestiondepark.DataBase.Problem_Entity

class ServiceProblemDetailImp : ServiceProblemDetailInter {



    private var mDb: ProblemDataBase? = null
    private val mUiHandler = Handler()
    private lateinit var mDbWorkerThread: DbWorkerThread


    /**
     * Ckeck user input
     */
    override fun checkInput(type: String, description: String,
                            location: String, adresse: String): Boolean {
        if(description.isBlank() || type.equals("NO TYPE")
                || location.isBlank() || adresse.isBlank()){
            return false
        }
        return true
    }

    /**
     * Creat Problem Entity (object) from user inPut
     */
    override fun creatProblemEntity(type: String, description: String,
                                    location: String, adresse: String): Problem_Entity {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Insert the Problem Entity to the Data base
     */
    override fun insertProblemDataInDb(problem_Entity: Problem_Entity) {
        val task = Runnable { mDb?.problemDataDao()?.insert(problem_Entity) }
        mDbWorkerThread.postTask(task)
    }

    /**
     * Exit the data base and close it
     */
    override fun exitDataBase(){
        ProblemDataBase.destroyInstance()
        mDbWorkerThread.quit()


    }

    /**
     * Open the data base
     */
    override fun openDataBase(context: Context) {
        //Lancer le WorkerHandler
        mDbWorkerThread = DbWorkerThread("dbWorkerMainThread")
        mDbWorkerThread.start()
        //init DataBAse
        mDb = ProblemDataBase.getInstance(context)
    }

}
