package com.example.route.route

import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import com.example.domain.repository.IUsersRepository
import com.example.exceptions.InsertFailedException
import com.example.route.mapper.*
import com.example.route.model.UserRouteModel
import com.example.route.util.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

private const val USERS = "users"
private const val ID = "id"

fun Route.insertUser() {
    val userRep by inject<IUsersRepository>()
    post("/$USERS") {
        val user = call.receive<UserRouteModel>()
        val test = user.toDomain()

        val id = try {
            userRep.insert(user.toDomain())
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.BadRequest)
            null
        }

        if (id != null) {
            val locationUrl = "/$USERS/$id"
            call.response.headers.append(HttpHeaders.Location, locationUrl)
            call.respond(HttpStatusCode.Created)
        }
    }
}

fun Route.getUser() {
    val userRep by inject<IUsersRepository>()
    get("/$USERS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@get
        }

        val user = userRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound)
            return@get
        }

        call.respond(HttpStatusCode.OK, user.toRoute())
    }
}

fun Route.updateUser() {
    val userRep by inject<IUsersRepository>()
    put("/$USERS/{$ID}") {
        val user = call.receive<UserRouteModel>()
        val id = user.id ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@put
        }

        userRep.update(id, user.toDomain())
        call.respond(HttpStatusCode.OK)
    }
}

fun Route.deleteUser() {
    val userRep by inject<IUsersRepository>()
    delete("/$USERS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            return@delete
        }

        userRep.delete(id.toUUID())
        call.respond(HttpStatusCode.NoContent)
    }
}
