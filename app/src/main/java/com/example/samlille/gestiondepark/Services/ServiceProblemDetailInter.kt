package com.example.samlille.gestiondepark.Services

import android.content.Context
import com.example.samlille.gestiondepark.DataBase.Problem_Entity

interface ServiceProblemDetailInter {
    fun insertProblemDataInDb(problem_Entity: Problem_Entity)
    fun exitDataBase()
    fun openDataBase(context: Context)
    fun creatProblemEntity(type: String, description: String, location: String, adresse: String): Problem_Entity
    fun checkInput(type: String, description: String, location: String, adresse: String): Boolean
}