package com.pychen0918.geofencing.routes

import com.pychen0918.geofencing.models.Customer
import com.pychen0918.geofencing.models.customerStorage
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*

fun Application.registerCustomerRoutes(){
    routing {
        customerRouting()
    }
}

fun Route.customerRouting(){
    route("/customer"){
        get{
            if(customerStorage.isNotEmpty()){
                call.respond(customerStorage)
            }
            else{
                call.respondText("No customers found", status = HttpStatusCode.NotFound)
            }
        }
        get("{id}"){
            val id = call.parameters["id"] ?: return@get call.respondText(
                text = "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val customer = customerStorage.find { it.id == id } ?: return@get call.respondText(
                text = "No customer with id $id",
                status = HttpStatusCode.NotFound
            )
            call.respond(customer)
        }
        post {
            val customer = call.receive<Customer>()
            customerStorage.add(customer)
            call.respondText(
                text = "Customer stored correctly",
                status = HttpStatusCode.Created
            )
        }
        delete("{id}"){
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (customerStorage.removeIf {it.id == id}){
                call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
            }
            else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }
}