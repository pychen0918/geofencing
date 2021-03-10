package com.pychen0918.geofencing.routes

import com.pychen0918.geofencing.models.ErrorMessage
import com.pychen0918.geofencing.models.Geofence
import com.pychen0918.geofencing.tile38.Tile38Client
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.routing.get
import kotlinx.serialization.json.*

fun Application.registerHookPointRoutes(){
    routing {
        HookPointRouting()
    }
}

fun Route.HookPointRouting() {
    route("/hooks") {
        post {
            println("Received message ${call.receiveText()}")
            // TODO: parse and relay the message to correct hook

            // Respond 200 OK to tile38
            call.respond(status = HttpStatusCode.OK, "")
        }
    }
}
