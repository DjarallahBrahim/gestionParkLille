package com.example.samlille.gestiondepark.Services

import android.content.Context
import com.example.samlille.gestiondepark.DataBase.Problem_Entity

interface CusomDataBaseServiceItf {
    fun destroyDataBase()
    fun fetchProblemDataFromDb(context: Context, bindDataWithUI: (m: List<Problem_Entity>) -> Unit)
    fun initDB(context: Context)
    fun fetchLocationFromDB(context: Context, bindDataWithMap: (m: List<String>) -> Void)
    fun insertProblemDataInDb(problem_Entity: Problem_Entity)
    fun deletAllFromDB()
    fun deletById(uid: Long?)
    fun getElementById(context: Context, uid: Long?, bindDataWithUI: (m: Problem_Entity) -> Void)
}