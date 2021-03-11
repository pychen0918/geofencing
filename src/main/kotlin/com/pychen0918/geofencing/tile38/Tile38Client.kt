package com.pychen0918.geofencing.tile38

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

fun String.toJsonObject(): JsonObject {
    return Json.parseToJsonElement(this).jsonObject
}

object Tile38Client : Tile38CommandInterface {
    private const val url = "http://127.0.0.1:9851"
    private val client = HttpClient()

    private suspend fun post(url: String, data: String): String {
        return client.post<String>(url){
            body = data
        }
    }

    private suspend fun get(url: String, data: String): String {
        return client.get<String>(url){
            body = data
        }
    }

    override suspend fun getPoint(key: String, id: String): JsonObject {
        return post(url, "GET $key $id POINT").toJsonObject()
    }

    override suspend fun setPoint(key: String, id: String, lat: Double, lng: Double): JsonObject {
        return post(url, "SET $key $id POINT $lat $lng").toJsonObject()
    }

    override suspend fun scanPoints(key: String, limit: Int): JsonObject {
        return post(url, "SCAN $key LIMIT $limit POINTS").toJsonObject()
    }

    override suspend fun getNearby(key: String, lat: Double, lng: Double, radius: Int, limit: Int): JsonObject {
        return get(url, "NEARBY $key LIMIT $limit POINT $lat $lng $radius").toJsonObject()
    }

    override suspend fun nearbyFence(
        key: String,
        id: String,
        lat: Double,
        lng: Double,
        radius: Int,
        hook: String,
        events: List<String>
    ): JsonObject {
        // We trigger the hook back to ourself, and forward it to user_hook later
        return post(url, "SETHOOK $id http://127.0.0.1:8080/hooks META hook $hook NEARBY $key FENCE DETECT ${events.joinToString(separator = ",")} POINT $lat $lng $radius").toJsonObject()
    }
}