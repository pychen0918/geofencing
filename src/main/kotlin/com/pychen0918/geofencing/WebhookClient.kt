package com.pychen0918.geofencing

import io.ktor.client.*
import io.ktor.client.request.*

object WebhookClient {
    private val client = HttpClient()

    private suspend fun post(url: String, data: String): String {
        return client.post<String>(url){
            body = data
        }
    }

    suspend fun postWebhook(url: String, data: String) {
        post(url, data)
    }
}