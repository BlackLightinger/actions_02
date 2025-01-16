package com.example.route.util

import java.util.*
import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import org.slf4j.Logger


fun String.toUUID(): UUID = UUID.fromString(this)

fun PipelineContext<Unit, ApplicationCall>.logger(): Logger = call.application.environment.log
