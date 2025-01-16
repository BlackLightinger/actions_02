package com.example.route.route

import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import com.example.domain.repository.IRevisionsRepository
import com.example.exceptions.InsertFailedException
import com.example.exceptions.UpdateFailedException
import com.example.exceptions.DeleteFailedException
import com.example.route.mapper.*
import com.example.route.model.RevisionRouteModel
import com.example.route.util.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import com.example.route.util.* // Если есть другие утилиты, добавьте их
import io.ktor.server.plugins.ContentTransformationException

private const val REVISIONS = "revisions"
private const val ID = "id"
private const val API_V1 = "api/v1"
private const val API_V2 = "api/v2"

fun Route.insertRevision() {
    val revisionRep by inject<IRevisionsRepository>()

    post("/$API_V1/$REVISIONS") {
        val revision = call.receive<RevisionRouteModel>()
        val id = try {
            revisionRep.insert(revision.toDomain())
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
            logger().error("Insertion failed: ${e.message}")
            return@post
        }

        val locationUrl = "/$REVISIONS/$id"
        call.response.headers.append(HttpHeaders.Location, locationUrl)
        call.respond(HttpStatusCode.Created)
        logger().info("Inserted revision with ID: $id")
    }

    post("/$API_V2/$REVISIONS") {
        try {
            val revision = call.receive<RevisionRouteModel>()
            val id = revisionRep.insert(revision.toDomain())

            val locationUrl = "/$REVISIONS/$id"
            call.response.headers.append(HttpHeaders.Location, locationUrl)
            call.respond(HttpStatusCode.Created)
            logger().info("(V2) Inserted revision with ID: $id")
        } catch (e: ContentTransformationException) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) Invalid request format: ${e.message}")
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.UnprocessableEntity, "Error inserting revision.")
            logger().error("(V2) Insertion failed: ${e.message}")
        }
    }
}

fun Route.getRevision() {
    val revisionRep by inject<IRevisionsRepository>()

    get("/$API_V1/$REVISIONS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("GetRevision request error: missing ID")
            return@get
        }

        val revision = revisionRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound)
            logger().error("No revision found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, revision.toRoute())
        logger().info("Retrieved revision with ID: $id")
    }

    get("/$API_V2/$REVISIONS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) GetRevision request error: missing ID")
            return@get
        }

        val revision = revisionRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound, "No revision found with such ID: $id")
            logger().error("(V2) No revision found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, revision.toRoute())
        logger().info("(V2) Retrieved revision with ID: $id")
    }
}

fun Route.updateRevision() {
    val revisionRep by inject<IRevisionsRepository>()

    put("/$API_V1/$REVISIONS/{$ID}") {
        val revision = call.receive<RevisionRouteModel>()
        val id = revision.id ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("UpdateRevision request error: missing ID")
            return@put
        }

        revisionRep.update(id, revision.toDomain())
        call.respond(HttpStatusCode.OK)
        logger().info("Updated revision with ID: $id")
    }

    put("/$API_V2/$REVISIONS/{$ID}") {
        val revision = call.receive<RevisionRouteModel>()
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) UpdateRevision request error: missing ID")
            return@put
        }

        try {
            revisionRep.update(id.toUUID(), revision.toDomain())
            call.respond(HttpStatusCode.OK)
            logger().info("(V2) Updated revision with ID: $id")
        } catch (e: UpdateFailedException) {
            call.respond(HttpStatusCode.UnprocessableEntity, "Error updating revision.")
            logger().error("(V2) Update failed: ${e.message}")
        }
    }
}

fun Route.deleteRevision() {
    val revisionRep by inject<IRevisionsRepository>()

    delete("/$API_V1/$REVISIONS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("DeleteRevision request error: missing ID")
            return@delete
        }

        revisionRep.delete(id.toUUID())
        call.respond(HttpStatusCode.NoContent)
        logger().info("Deleted revision with ID: $id")
    }

    delete("/$API_V2/$REVISIONS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) DeleteRevision request error: missing ID")
            return@delete
        }

        try {
            revisionRep.delete(id.toUUID())
            call.respond(HttpStatusCode.NoContent)
            logger().info("(V2) Deleted revision with ID: $id")
        } catch (e: DeleteFailedException) {
            call.respond(HttpStatusCode.NotFound, "No revision found with such ID.")
            logger().error("(V2) Deletion failed: ${e.message}")
        }
    }
}
