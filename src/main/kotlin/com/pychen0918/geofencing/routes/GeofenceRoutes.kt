package com.pychen0918.geofencing.routes

import com.pychen0918.geofencing.models.ErrorMessage
import com.pychen0918.geofencing.models.Geofence
import com.pychen0918.geofencing.tile38.Tile38Client
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.routing.get
import kotlinx.serialization.json.*

fun Application.registerGeofenceRoutes(){
    routing {
        GeofenceRouting()
    }
}

fun Route.GeofenceRouting() {
    route("/geofence") {
        post {
            val geofence = call.receive<Geofence>()

            val result = Tile38Client.nearbyFence(geofence.collection, geofence.id, geofence.lat, geofence.lng, geofence.radius, geofence.hook, geofence.events.map {it.name})
            println(result.toString())
            val ok = result["ok"]!!.jsonPrimitive.boolean
            if(ok){
                call.respond(
                    HttpStatusCode.Created,
                    buildJsonObject {
                        put("collection", geofence.collection)
                        put("id", geofence.id)
                    }
                )
            }
            else{
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorMessage(4, result["err"]!!.jsonPrimitive.content)
                )
            }
        }
    }
}
