package com.example.route.route

import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import com.example.domain.repository.ISearchesRepository
import com.example.exceptions.InsertFailedException
import com.example.exceptions.UpdateFailedException
import com.example.exceptions.DeleteFailedException
import com.example.route.mapper.*
import com.example.route.model.SearchRouteModel
import com.example.route.util.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import com.example.route.util.* // Если у вас есть другие утилиты, добавьте их
import io.ktor.server.plugins.ContentTransformationException

private const val SEARCHES = "searches"
private const val ID = "id"
private const val API_V1 = "api/v1"
private const val API_V2 = "api/v2"

fun Route.insertSearch() {
    val searchRep by inject<ISearchesRepository>()

    post("/$API_V1/$SEARCHES") {
        val search = call.receive<SearchRouteModel>()
        val id = try {
            searchRep.insert(search.toDomain())
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
            logger().error("Insertion failed: ${e.message}")
            return@post
        }

        val locationUrl = "/$SEARCHES/$id"
        call.response.headers.append(HttpHeaders.Location, locationUrl)
        call.respond(HttpStatusCode.Created)
        logger().info("Inserted search with ID: $id")
    }

    post("/$API_V2/$SEARCHES") {
        try {
            val search = call.receive<SearchRouteModel>()
            val id = searchRep.insert(search.toDomain())

            val locationUrl = "/$SEARCHES/$id"
            call.response.headers.append(HttpHeaders.Location, locationUrl)
            call.respond(HttpStatusCode.Created)
            logger().info("(V2) Inserted search with ID: $id")
        } catch (e: ContentTransformationException) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) Invalid request format: ${e.message}")
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.UnprocessableEntity, "Error inserting search.")
            logger().error("(V2) Insertion failed: ${e.message}")
        }
    }
}

fun Route.getSearch() {
    val searchRep by inject<ISearchesRepository>()

    get("/$API_V1/$SEARCHES/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("GetSearch request error: missing ID")
            return@get
        }

        val search = searchRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound)
            logger().error("No search found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, search.toRoute())
        logger().info("Retrieved search with ID: $id")
    }

    get("/$API_V2/$SEARCHES/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) GetSearch request error: missing ID")
            return@get
        }

        val search = searchRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound, "No search found with such ID: $id")
            logger().error("(V2) No search found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, search.toRoute())
        logger().info("(V2) Retrieved search with ID: $id")
    }
}

fun Route.updateSearch() {
    val searchRep by inject<ISearchesRepository>()

    put("/$API_V1/$SEARCHES/{$ID}") {
        val search = call.receive<SearchRouteModel>()
        val id = search.id ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("UpdateSearch request error: missing ID")
            return@put
        }

        searchRep.update(id, search.toDomain())
        call.respond(HttpStatusCode.OK)
        logger().info("Updated search with ID: $id")
    }

    put("/$API_V2/$SEARCHES/{$ID}") {
        val search = call.receive<SearchRouteModel>()
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) UpdateSearch request error: missing ID")
            return@put
        }

        try {
            searchRep.update(id.toUUID(), search.toDomain())
            call.respond(HttpStatusCode.OK)
            logger().info("(V2) Updated search with ID: $id")
        } catch (e: UpdateFailedException) {
            call.respond(HttpStatusCode.UnprocessableEntity, "Error updating search.")
            logger().error("(V2) Update failed: ${e.message}")
        }
    }
}

fun Route.deleteSearch() {
    val searchRep by inject<ISearchesRepository>()

    delete("/$API_V1/$SEARCHES/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("DeleteSearch request error: missing ID")
            return@delete
        }

        searchRep.delete(id.toUUID())
        call.respond(HttpStatusCode.NoContent)
        logger().info("Deleted search with ID: $id")
    }

    delete("/$API_V2/$SEARCHES/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) DeleteSearch request error: missing ID")
            return@delete
        }

        try {
            searchRep.delete(id.toUUID())
            call.respond(HttpStatusCode.NoContent)
            logger().info("(V2) Deleted search with ID: $id")
        } catch (e: DeleteFailedException) {
            call.respond(HttpStatusCode.NotFound, "No search found with such ID.")
            logger().error("(V2) Deletion failed: ${e.message}")
        }
    }
}
