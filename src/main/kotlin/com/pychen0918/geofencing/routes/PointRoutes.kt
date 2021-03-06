package com.pychen0918.geofencing.routes

import com.pychen0918.geofencing.MyHttpClient
import com.pychen0918.geofencing.models.ErrorMessage
import com.pychen0918.geofencing.models.Point
import com.pychen0918.geofencing.models.pointsStorage
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import kotlinx.serialization.decodeFromString
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
            // TODO: lookup the collection in tile38 and handle error
            pointsStorage.find { it.collection == collection } ?: return@get call.respond(
                HttpStatusCode.NotFound,
                ErrorMessage(2, "The collection ID does not exist")
            )

            val id: String? = call.parameters["id"]

            // TODO: find the point in tile38
            val result = pointsStorage.find { it.collection == collection && it.id == id} ?: return@get call.respond(
                HttpStatusCode.NotFound,
                ErrorMessage(3, "Point not found")
            )

            call.respond(
                HttpStatusCode.OK,
                result
            )
        }
        post {
            val point = call.receive<Point>()
            val result = MyHttpClient.post("http://127.0.0.1:9851", "SET ${point.collection} ${point.id} POINT ${point.lat} ${point.lng}")
            println(result)

            val json = Json.parseToJsonElement(result)
            val ok = json.jsonObject["ok"]!!.jsonPrimitive.boolean
            if(ok){
                call.respond(
                    HttpStatusCode.Created,
                    buildJsonObject {
                        put("id", point.id)
                        put("collection", point.collection)
                    }
                )
            }
            else {
                call.respond(
                    HttpStatusCode.BadRequest,
                    ErrorMessage(4, json.jsonObject["err"]!!.jsonPrimitive.content)
                )
            }
        }
    }
}