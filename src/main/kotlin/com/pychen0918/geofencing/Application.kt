package com.pychen0918.geofencing

import com.pychen0918.geofencing.routes.registerCustomerRoutes
import com.pychen0918.geofencing.routes.registerNearbyRoutes
import com.pychen0918.geofencing.routes.registerPointRoutes
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.serialization.json

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation){
        json()
    }
    registerCustomerRoutes()
    registerPointRoutes()
    registerNearbyRoutes()
}
