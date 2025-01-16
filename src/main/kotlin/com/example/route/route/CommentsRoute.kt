package com.example.route.route

import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import com.example.domain.repository.ICommentsRepository
import com.example.exceptions.InsertFailedException
import com.example.exceptions.UpdateFailedException
import com.example.exceptions.DeleteFailedException
import com.example.route.mapper.*
import com.example.route.model.CommentRouteModel
import com.example.route.util.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import com.example.route.util.*
import io.ktor.server.plugins.ContentTransformationException

private const val COMMENTS = "comments"
private const val ID = "id"
private const val API_V1 = "api/v1"
private const val API_V2 = "api/v2"

fun Route.insertComment() {
    val commentRep by inject<ICommentsRepository>()

    post("/$API_V1/$COMMENTS") {
        val comment = call.receive<CommentRouteModel>()
        val id = try {
            commentRep.insert(comment.toDomain())
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
            logger().error("Insertion failed: ${e.message}")
            return@post
        }

        val locationUrl = "/$COMMENTS/$id"
        call.response.headers.append(HttpHeaders.Location, locationUrl)
        call.respond(HttpStatusCode.Created)
        logger().info("Inserted comment with ID: $id")
    }

    post("/$API_V2/$COMMENTS") {
        try {
            val comment = call.receive<CommentRouteModel>()
            val id = commentRep.insert(comment.toDomain())

            val locationUrl = "/$COMMENTS/$id"
            call.response.headers.append(HttpHeaders.Location, locationUrl)
            call.respond(HttpStatusCode.Created)
            logger().info("(V2) Inserted comment with ID: $id")
        } catch (e: ContentTransformationException) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) Invalid request format: ${e.message}")
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.UnprocessableEntity, "Error inserting comment.")
            logger().error("(V2) Insertion failed: ${e.message}")
        }
    }
}

fun Route.getComment() {
    val commentRep by inject<ICommentsRepository>()

    get("/$API_V1/$COMMENTS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("GetComment request error: missing ID")
            return@get
        }

        val comment = commentRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound)
            logger().error("No comment found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, comment.toRoute())
        logger().info("Retrieved comment with ID: $id")
    }

    get("/$API_V2/$COMMENTS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) GetComment request error: missing ID")
            return@get
        }

        val comment = commentRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound, "No comment found with such ID: $id")
            logger().error("(V2) No comment found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, comment.toRoute())
        logger().info("(V2) Retrieved comment with ID: $id")
    }
}

fun Route.updateComment() {
    val commentRep by inject<ICommentsRepository>()

    put("/$API_V1/$COMMENTS/{$ID}") {
        val comment = call.receive<CommentRouteModel>()
        val id = comment.id ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("UpdateComment request error: missing ID")
            return@put
        }

        commentRep.update(id, comment.toDomain())
        call.respond(HttpStatusCode.OK)
        logger().info("Updated comment with ID: $id")
    }

    put("/$API_V2/$COMMENTS/{$ID}") {
        val comment = call.receive<CommentRouteModel>()
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) UpdateComment request error: missing ID")
            return@put
        }

        try {
            commentRep.update(id.toUUID(), comment.toDomain())
            call.respond(HttpStatusCode.OK)
            logger().info("(V2) Updated comment with ID: $id")
        } catch (e: UpdateFailedException) {
            call.respond(HttpStatusCode.UnprocessableEntity, "Error updating comment.")
            logger().error("(V2) Update failed: ${e.message}")
        }
    }
}

fun Route.deleteComment() {
    val commentRep by inject<ICommentsRepository>()

    delete("/$API_V1/$COMMENTS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("DeleteComment request error: missing ID")
            return@delete
        }

        commentRep.delete(id.toUUID())
        call.respond(HttpStatusCode.NoContent)
        logger().info("Deleted comment with ID: $id")
    }

    delete("/$API_V2/$COMMENTS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) DeleteComment request error: missing ID")
            return@delete
        }

        try {
            commentRep.delete(id.toUUID())
            call.respond(HttpStatusCode.NoContent)
            logger().info("(V2) Deleted comment with ID: $id")
        } catch (e: DeleteFailedException) {
            call.respond(HttpStatusCode.NotFound, "No comment found with such ID.")
            logger().error("(V2) Delete failed: ${e.message}")
        }
    }
}
