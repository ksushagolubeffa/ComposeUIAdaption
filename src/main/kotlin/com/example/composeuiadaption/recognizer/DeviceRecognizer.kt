package com.example.composeuiadaption.recognizer

import kotlin.math.sqrt

object DeviceRecognizer {

    fun deviceRecognizer(width:Long, height: Long, inches: Double): String{
        val aspectRatio = height/ width
        val diagonal = sqrt((height * height + width * width).toDouble()).toFloat()
        return if(inches>6.9){
//        return if(aspectRatio >= 1.2 && diagonal >= 25.4){
            "Tablet"
        }else{
            "Phone"
        }
    }

}