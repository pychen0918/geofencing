package com.pychen0918.geofencing.tile38

import kotlinx.serialization.json.JsonObject

interface Tile38CommandInterface {
    suspend fun getPoint(key: String, id: String): JsonObject
    suspend fun setPoint(key: String, id: String, lat: Double, lng: Double): JsonObject
    suspend fun scanPoints(key: String, limit: Int = 20): JsonObject
    suspend fun getNearby(key: String, lat: Double, lng: Double, radius: Int, limit: Int = 20): JsonObject
    suspend fun nearbyFence(key: String, id: String, lat: Double, lng: Double, radius: Int, hook: String, events: List<String>): JsonObject
}