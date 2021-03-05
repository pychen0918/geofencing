package com.pychen0918.geofencing.routes

import com.pychen0918.geofencing.models.ErrorMessage
import com.pychen0918.geofencing.models.Point
import com.pychen0918.geofencing.models.pointsStorage
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import kotlinx.serialization.encodeToString
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
            // TODO: add to tile38
            pointsStorage.add(point)

            val result = buildJsonObject {
                put("id", point.id)
                put("collection", point.collection)
            }

            call.respond (
                HttpStatusCode.Created,
                result
            )
        }
    }
}