package com.example.data.model

data class GpsLocation(
    val orderId: String,
    val riderId: String,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float = 5.0f,
    val speed: Float = 0.0f, // in m/s or km/h
    val bearing: Float = 0.0f, // in degrees 0-360
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)
