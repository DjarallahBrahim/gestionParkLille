package com.example.samlille.gestiondepark.DataBase

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query

@Dao
interface Problem_DAO{

    @Query("SELECT * from Problem_Entity")
    fun getAllProblem(): List<Problem_Entity>

    @Query("SELECT * from Problem_Entity where uid = :uid ")
    fun getElementById(uid : Long?): Problem_Entity

    @Insert(onConflict = REPLACE)
    fun insert(problem: Problem_Entity)

    @Query("DELETE from Problem_Entity where uid = :uid")
    fun deleteById(uid : Long?)

    @Query("DELETE from Problem_Entity")
    fun deleteAll()

    @Query("SELECT location from Problem_Entity")
    fun getAllLocation(): List<String>


}