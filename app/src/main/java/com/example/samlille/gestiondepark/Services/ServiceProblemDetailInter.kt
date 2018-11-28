package com.example.samlille.gestiondepark.Services

import android.content.Context
import com.example.samlille.gestiondepark.DataBase.Problem_Entity

interface ServiceProblemDetailInter {
    fun insertProblemDataInDb(problem_Entity: Problem_Entity)
    fun exitDataBase()
    fun openDataBase(context: Context)
    fun checkInput(type: String,  location: String): Boolean
}