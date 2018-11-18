package com.example.samlille.gestiondepark.DataBase

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(Problem_Entity::class), version = 1)
abstract class ProblemDataBase : RoomDatabase(){

    abstract  fun problemDataDao(): Problem_DAO

    companion object {
        private var INSTANCE: ProblemDataBase? = null;

        fun getInstance(context: Context): ProblemDataBase? {

            if(INSTANCE == null){
                synchronized(ProblemDataBase::class){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ProblemDataBase::class.java, "problem_Park.db")
                            .build()
                }
            }
            return INSTANCE;
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}