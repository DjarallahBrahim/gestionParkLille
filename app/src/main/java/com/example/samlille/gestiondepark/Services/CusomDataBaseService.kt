package com.example.samlille.gestiondepark.Services

import android.content.Context
import android.os.Handler
import android.widget.Toast
import com.example.samlille.gestiondepark.DataBase.DbWorkerThread
import com.example.samlille.gestiondepark.DataBase.ProblemDataBase
import com.example.samlille.gestiondepark.DataBase.Problem_Entity

/**
 * service to use our database
 */
class CusomDataBaseService : CusomDataBaseServiceItf {
     var mDb: ProblemDataBase? = null
     lateinit var mDbWorkerThread: DbWorkerThread
     val mUiHandler = Handler()


    /**
     * init data base
     * @param: context of our activity
     */
    override fun initDB(context: Context) {

        //Lancer le WorkerHandler
        mDbWorkerThread = DbWorkerThread("dbWorkerMainThread")
        mDbWorkerThread.start()
        //init DataBAse
        mDb = ProblemDataBase.getInstance(context)

    }

    /**
     * fetch dataFrom DB and bind it to view with bindDataWithUI reference
     */
    override fun fetchProblemDataFromDb(context: Context, bindDataWithUI: (m: List<Problem_Entity>) -> Unit) {
        val task = Runnable {
            val problemData =
                    mDb?.problemDataDao()?.getAllProblem()
            mUiHandler.post({
                if (problemData == null || problemData?.size == 0) {
                    Toast.makeText(context,"No Problem in data base", Toast.LENGTH_LONG).show()
                } else {
                    bindDataWithUI(problemData)
                }
            })
        }
        mDbWorkerThread.postTask(task)
    }

    /**
     * destroy our database instance
     */
    override fun destroyDataBase() {
        ProblemDataBase.destroyInstance()
        mDbWorkerThread.quit()
    }

    /**
     * fetch dataFrom DB and bind it to view with bindDataWithMap reference
     */
    override fun fetchLocationFromDB(context: Context, bindDataWithMap: (m: List<String>) -> Void) {
        val task = Runnable {
            val problemData =
                    mDb?.problemDataDao()?.getAllLocation()
            mUiHandler.post({
                if (problemData == null || problemData?.size == 0) {
                    Toast.makeText(context,"No Location in data base", Toast.LENGTH_LONG).show()
                } else {
                    bindDataWithMap(problemData)
                }
            })
        }
        mDbWorkerThread.postTask(task)
    }

    /**
     * instert problemEntity in database
     */
    override fun insertProblemDataInDb(problem_Entity: Problem_Entity) {
        val task = Runnable { mDb?.problemDataDao()?.insert(problem_Entity) }
        mDbWorkerThread.postTask(task)
    }

    /**
     * remove all problemEntity from database
     */
    override fun deletAllFromDB() {
        val task = Runnable {mDb?.problemDataDao()?.deleteAll() }
        mDbWorkerThread.postTask(task)

    }
    /**
     * remove by id problemEntity from database
     */
    override fun deletById(uid: Long?) {
        val task = Runnable {mDb?.problemDataDao()?.deleteById(uid) }
        mDbWorkerThread.postTask(task)
    }

    /**
     * get problemEntity from database by id and bind it with view
     */
    override fun getElementById(context: Context, uid: Long?, bindDataWithUI: (m: Problem_Entity) -> Void) {
        val task = Runnable {
            val problemData =
                    mDb?.problemDataDao()?.getElementById(uid)
            mUiHandler.post({
                if (problemData == null) {
                    Toast.makeText(context, "No Problem in data base", Toast.LENGTH_LONG).show()
                } else {
                    bindDataWithUI(problemData)
                }
            })
        }
        mDbWorkerThread.postTask(task)
    }
}
