package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(test: Boolean = false) {
    configureBasicAuthentication()
    configureSerialization()
    configureDatabases(test)
    configureDi()
    configureRouting()
    configureTemplating()
}
