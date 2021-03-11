package com.pychen0918.geofencing.routes

import com.pychen0918.geofencing.WebhookClient
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

fun Application.registerHookPointRoutes(){
    routing {
        HookPointRouting()
    }
}

fun Route.HookPointRouting() {
    route("/hooks") {
        post {
            // Respond 200 OK to tile38
            call.respond(status = HttpStatusCode.OK, "")

            // {"command":"set","group":"604a15f8b1861734ec2d5738","detect":"inside","hook":"around_school","meta":{"hook":"http://127.0.0.1:8080/hooks"},"key":"vehicles","time":"2021-03-11T21:07:04.8453707+08:00","id":"car3","object":{"type":"Point","coordinates":[121.47654685314934,25.0180434512876]}}
            //println("Received message ${call.receiveText()}")
            val message = Json.decodeFromString(GeofenceWebhookMessage.serializer(), call.receiveText())
            val response = buildJsonObject {
                put("id", message.hook)
                put("collection", message.key)
                put("type", message.detect)
                put("timestamp", message.time)
                putJsonObject("point"){
                    put("id", message.id)
                    put("lat", message.geoObject.coordinates[1])
                    put("lng", message.geoObject.coordinates[0])
                }
            }

            // Post to user's webhook url
            WebhookClient.postWebhook(message.meta.hook, Json.encodeToString(JsonObject.serializer(), response))
        }
    }
}

@Serializable
private data class GeofenceWebhookMeta(val hook: String)

@Serializable
private data class GeofenceWebhookObject(val type: String, val coordinates: List<Double>)

@Serializable
private data class GeofenceWebhookMessage(val command: String,
                                          val group: String,
                                          val detect: String,
                                          val hook: String,
                                          val meta: GeofenceWebhookMeta,
                                          val key: String,
                                          val time: String,
                                          val id: String,
                                          @SerialName("object") val geoObject: GeofenceWebhookObject)