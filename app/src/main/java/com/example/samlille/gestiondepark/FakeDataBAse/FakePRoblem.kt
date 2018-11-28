package com.example.samlille.gestiondepark.FakeDataBAse

import android.location.Location
import com.google.maps.model.LatLng
import java.util.*
import kotlin.collections.ArrayList
import android.content.Context
import com.example.samlille.gestiondepark.DataBase.Problem_Entity
import com.example.samlille.gestiondepark.Services.CusomDataBaseService

/**
 * method to generate a fakedataBAse
 */
private var SPINNERLIST = arrayOf("Arbre à tailler", "Arbre à abattre", "Détritus", "Haie à tailler", "Mauvaise herbe", "Autre")

fun getLocation(x0: Double, y0: Double, radius: Int, context: Context) {

    var cusomDataBaseService = CusomDataBaseService()
    cusomDataBaseService.initDB(context)
    cusomDataBaseService.deletAllFromDB()


    var point = LatLng(x0,y0)
    var randomPoints = ArrayList<LatLng>()
    val myLocation = Location("")
    myLocation.setLatitude(point.lat)
    myLocation.setLongitude(point.lng)

    for(i in 0..10) {
        val x0 = point.lat
        val y0 = point.lng

        val random = Random()

        // Convert radius from meters to degrees
        val radiusInDegrees = (radius / 111000f).toDouble()

        val u = random.nextDouble()
        val v = random.nextDouble()
        val w = radiusInDegrees * Math.sqrt(u)
        val t = 2.0 * Math.PI * v
        val x = w * Math.cos(t)
        val y = w * Math.sin(t)

        val new_x = x / Math.cos(y0)

        val foundLatitude = new_x + x0
        val foundLongitude = y + y0
        val randomLatLng = LatLng(foundLatitude, foundLongitude)
        randomPoints.add(randomLatLng)

        val problemEntity = Problem_Entity()
        problemEntity.type = SPINNERLIST[random.nextInt(5 - 0) + 0]
        problemEntity.location = "${foundLatitude}  , ${foundLongitude}"
        problemEntity.description = "une description de Type Test: ${i}"
        problemEntity.adresse = "Rue 1${i}5 rue xxx, Lille "
        cusomDataBaseService.insertProblemDataInDb(problemEntity)

    }
}