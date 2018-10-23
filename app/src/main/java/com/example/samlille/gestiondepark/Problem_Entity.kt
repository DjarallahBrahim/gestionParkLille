package com.example.samlille.gestiondepark

import android.accounts.AuthenticatorDescription
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Problem_Entity(@PrimaryKey(autoGenerate = true)  var uid: Long?,

          @ColumnInfo(name = "titre") var titre: String,
          @ColumnInfo(name = "description") var description: String,
          @ColumnInfo(name = "location") var location: String
){
    constructor():this(null,"","","")

}