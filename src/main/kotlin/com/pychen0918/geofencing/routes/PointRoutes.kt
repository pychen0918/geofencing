package com.pychen0918.geofencing.routes

import com.pychen0918.geofencing.tile38.Tile38Client
import com.pychen0918.geofencing.models.ErrorMessage
import com.pychen0918.geofencing.models.Point
import com.pychen0918.geofencing.models.pointsStorage
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import kotlinx.serialization.json.*

fun Application.registerPointRoutes(){
    routing {
        pointRouting()
    }
}

fun Route.pointRouting(){
    route("/points"){
        get {
            val collection = call.parameters["collection"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                ErrorMessage(1, "Collection ID is required")
            )
            val id: String? = call.parameters["id"]
            if (id != null && id.isNotEmpty()) {
                val result = Tile38Client.getPoint(collection, id)
                //{"ok":true,"point":{"lat":123.456,"lon":123.444},"elapsed":"133.1µs"}

                val ok = result["ok"]!!.jsonPrimitive.boolean
                if(ok){
                    call.respond(
                        HttpStatusCode.OK,
                        buildJsonObject {
                            put("collection", collection)
                            put("count", 1)
                            putJsonArray("points"){
                                addJsonObject {
                                    val point = result["point"]!!.jsonObject
                                    put("id", id)
                                    put("lat", point["lat"]!!.jsonPrimitive.content)
                                    put("lng", point["lon"]!!.jsonPrimitive.content)
                                }
                            }
                        }
                    )
                }
                else{
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorMessage(4, result["err"]!!.jsonPrimitive.content)
                    )
                }
            }
            else {
                // {"ok":true,"points":[{"id":"cat","point":{"lat":123.456,"lon":123.444}},{"id":"dog1","point":{"lat":123.456,"lon":123.444}}],"count":2,"cursor":0,"elapsed":"4.2687ms"}
                val result = Tile38Client.scanPoints(collection)
                val ok = result["ok"]!!.jsonPrimitive.boolean
                if(ok){
                    call.respond(
                        HttpStatusCode.OK,
                        buildJsonObject {
                            put("collection", collection)
                            put("count", result["count"]!!.jsonPrimitive.content)
                            putJsonArray("points"){
                                for(point in result["points"]!!.jsonArray) {
                                    addJsonObject {
                                        put("id", point.jsonObject["id"]!!.jsonPrimitive.content)
                                        put("lat", point.jsonObject["point"]!!.jsonObject["lat"]!!.jsonPrimitive.content)
                                        put("lng", point.jsonObject["point"]!!.jsonObject["lon"]!!.jsonPrimitive.content)
                                    }
                                }
                            }
                        }
                    )
                }
                else{
                    call.respond(
                        HttpStatusCode.NotFound,
                        ErrorMessage(4, result["err"]!!.jsonPrimitive.content)
                    )
                }
            }
        }
        post {
            val point = call.receive<Point>()

            // Send to tile38
            val result = Tile38Client.setPoint(point.collection, point.id, point.lat, point.lng)
            val ok = result["ok"]!!.jsonPrimitive.boolean
            if(ok){
                // Tile38 report OK
                call.respond(
                    HttpStatusCode.Created,
                    buildJsonObject {
                        put("id", point.id)
                        put("collection", point.collection)
                    }
                )
            }
            else {
                // Something went wrong
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorMessage(4, result["err"]!!.jsonPrimitive.content)
                )
            }
        }
    }
}