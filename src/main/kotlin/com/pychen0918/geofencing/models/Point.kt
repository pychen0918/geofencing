package com.pychen0918.geofencing.models

import kotlinx.serialization.Serializable

val pointsStorage = mutableListOf<Point>()

@Serializable
data class Point(val collection: String, val id: String, val lat: Double, val lng: Double)