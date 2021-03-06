package com.pychen0918.geofencing

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post

object MyHttpClient {
    val client = HttpClient(CIO)

    suspend fun post(url: String, data: String): String {
        return client.post<String>(url){
            body = data
        }
    }
}