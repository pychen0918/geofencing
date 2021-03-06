package com.pychen0918.geofencing.tile38

import kotlinx.serialization.json.JsonObject

interface Tile38CommandInterface {
    suspend fun getPoint(key: String, id: String): JsonObject
    suspend fun setPoint(key: String, id: String, lat: Double, lng: Double): JsonObject
    suspend fun scanPoints(key: String, limit: Int = 20): JsonObject
}