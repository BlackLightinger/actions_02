package com.example.route.route

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*


fun Route.mainPage() {
    get("/legacy/") {
        call.respond(ThymeleafContent("main", emptyMap()))
    }
}