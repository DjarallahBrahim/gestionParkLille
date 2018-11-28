package com.example.samlille.gestiondepark.Services

import android.content.Context
import com.example.samlille.gestiondepark.DataBase.Problem_Entity

class ServiceProblemDetailImp : ServiceProblemDetailInter {




    private  var cusomDataBaseService = CusomDataBaseService()

    /**
     * Ckeck user input
     */
    override fun checkInput(type: String,
                            location: String): Boolean {
        if(type.equals("NO TYPE")
                || location.isBlank() ){
            return false
        }
        return true
    }



    /**
     * Insert the Problem Entity to the Data base
     */
    override fun insertProblemDataInDb(problem_Entity: Problem_Entity) {
        this.cusomDataBaseService.insertProblemDataInDb(problem_Entity)
    }

    /**
     * Exit the data base and close it
     */
    override fun exitDataBase(){
        this.cusomDataBaseService.destroyDataBase()


    }

    /**
     * Open the data base
     */
    override fun openDataBase(context: Context) {
       this.cusomDataBaseService.initDB(context)
    }

}
