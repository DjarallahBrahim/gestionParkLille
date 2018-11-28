package com.example.samlille.gestiondepark.DataBase

import android.accounts.AuthenticatorDescription
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Problem_Entity TABLE
 */
@Entity
data class Problem_Entity(@PrimaryKey(autoGenerate = true)  var uid: Long?,
          @ColumnInfo(name = "type") var type: String,
          @ColumnInfo(name = "description") var description: String,
          @ColumnInfo(name = "adresse") var adresse: String,
          @ColumnInfo(name = "location") var location: String
){
    constructor():this(null,"","","","")

}