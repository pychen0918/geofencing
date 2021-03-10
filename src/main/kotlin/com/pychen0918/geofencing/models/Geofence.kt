package com.pychen0918.geofencing.models

import kotlinx.serialization.Serializable

@Serializable
enum class GeofenceEventType {
    enter,
    exit,
    inside,
    outside,
    crossover
}

@Serializable
data class Geofence(val id: String,
                    val collection: String,
                    val radius: Int,
                    val lat: Double,
                    val lng: Double,
                    val hook: String,
                    val events: List<GeofenceEventType> = listOf(GeofenceEventType.enter, GeofenceEventType.exit))