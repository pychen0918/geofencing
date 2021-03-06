package com.pychen0918.geofencing.tile38

interface Tile38CommandInterface {
    suspend fun getPoint(key: String, id: String): String
    suspend fun setPoint(key: String, id: String, lat: Double, lng: Double): String
    suspend fun scanPoints(key: String, limit: Int = 20): String
}