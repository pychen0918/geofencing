package com.pychen0918.geofencing.routes

import com.pychen0918.geofencing.models.ErrorMessage
import com.pychen0918.geofencing.tile38.Tile38Client
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import kotlinx.serialization.json.*

fun Application.registerNearbyRoutes(){
    routing {
        nearbyRouting()
    }
}

fun Route.nearbyRouting() {
    route("/nearby") {
        get {
            val collection = call.parameters["collection"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                ErrorMessage(1, "Collection ID is required")
            )
            val lat = call.parameters["lat"]?.toDouble() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                ErrorMessage(1, "Lat coordinate is required")
            )
            val lng = call.parameters["lng"]?.toDouble() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                ErrorMessage(1, "Lng coordinate is required")
            )
            val radius = call.parameters["radius"]?.toInt() ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                ErrorMessage(1, "Radius is required")
            )

            val result = Tile38Client.getNearby(collection, lat, lng, radius)
            //{"ok":true,"objects":[{"id":"pig","object":{"type":"Point","coordinates":[121.47136482533266,25.018733725551005]}}],"count":1,"cursor":0,"elapsed":"77.5µs"}
            val ok = result["ok"]!!.jsonPrimitive.boolean
            if(ok){
                call.respond(
                    HttpStatusCode.OK,
                    buildJsonObject {
                        put("collection", collection)
                        put("count", result["count"]!!.jsonPrimitive.content)
                        put("lat", lat)
                        put("lng", lng)
                        putJsonArray("points"){
                            for(point in result["objects"]!!.jsonArray) {
                                addJsonObject {
                                    put("id", point.jsonObject["id"]!!.jsonPrimitive.content)
                                    put("lat", point.jsonObject["object"]!!.jsonObject["coordinates"]!!.jsonArray[1].jsonPrimitive.content)
                                    put("lng", point.jsonObject["object"]!!.jsonObject["coordinates"]!!.jsonArray[0].jsonPrimitive.content)
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
}
