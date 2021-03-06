package com.pychen0918.geofencing.tile38

import io.ktor.client.HttpClient
import io.ktor.client.request.post

object Tile38Client : Tile38CommandInterface {
    private const val url = "http://127.0.0.1:9851"
    private val client = HttpClient()

    private suspend fun post(url: String, data: String): String {
        return client.post<String>(url){
            body = data
        }
    }

    override suspend fun getPoint(key: String, id: String): String {
        return post(url, "GET $key $id POINT")
    }

    override suspend fun setPoint(key: String, id: String, lat: Double, lng: Double): String {
        return post(url, "SET $key $id POINT $lat $lng")
    }

    override suspend fun scanPoints(key: String, limit: Int): String {
        return post(url, "SCAN $key LIMIT $limit POINTS")
    }
}