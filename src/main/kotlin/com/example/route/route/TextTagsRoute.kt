package com.example.route.route

import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import com.example.domain.repository.ITextTagsRepository
import com.example.exceptions.InsertFailedException
import com.example.exceptions.DeleteFailedException
import com.example.route.mapper.*
import com.example.route.model.TextTagRouteModel
import com.example.route.util.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import com.example.route.util.* // Если есть другие утилиты, добавьте их
import io.ktor.server.plugins.ContentTransformationException

private const val TEXT_TAGS = "text-tags"
private const val ID = "id"
private const val API_V1 = "api/v1"
private const val API_V2 = "api/v2"

fun Route.insertTextTag() {
    val textTagRep by inject<ITextTagsRepository>()

    post("/$API_V1/$TEXT_TAGS") {
        val textTag = call.receive<TextTagRouteModel>()
        val id = try {
            textTagRep.insert(textTag.toDomain())
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
            logger().error("Insertion failed: ${e.message}")
            return@post
        }

        val locationUrl = "/$TEXT_TAGS/$id"
        call.response.headers.append(HttpHeaders.Location, locationUrl)
        call.respond(HttpStatusCode.Created)
        logger().info("Inserted text tag with ID: $id")
    }

    post("/$API_V2/$TEXT_TAGS") {
        try {
            val textTag = call.receive<TextTagRouteModel>()
            val id = textTagRep.insert(textTag.toDomain())

            val locationUrl = "/$TEXT_TAGS/$id"
            call.response.headers.append(HttpHeaders.Location, locationUrl)
            call.respond(HttpStatusCode.Created)
            logger().info("(V2) Inserted text tag with ID: $id")
        } catch (e: ContentTransformationException) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) Invalid request format: ${e.message}")
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.UnprocessableEntity, "Error inserting text tag.")
            logger().error("(V2) Insertion failed: ${e.message}")
        }
    }
}

fun Route.getTextTag() {
    val textTagRep by inject<ITextTagsRepository>()

    get("/$API_V1/$TEXT_TAGS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("GetTextTag request error: missing ID")
            return@get
        }

        val textTag = textTagRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound)
            logger().error("No text tag found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, textTag.toRoute())
        logger().info("Retrieved text tag with ID: $id")
    }

    get("/$API_V2/$TEXT_TAGS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) GetTextTag request error: missing ID")
            return@get
        }

        val textTag = textTagRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound, "No text tag found with such ID: $id")
            logger().error("(V2) No text tag found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, textTag.toRoute())
        logger().info("(V2) Retrieved text tag with ID: $id")
    }
}

fun Route.deleteTextTag() {
    val textTagRep by inject<ITextTagsRepository>()

    delete("/$API_V1/$TEXT_TAGS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("DeleteTextTag request error: missing ID")
            return@delete
        }

        textTagRep.delete(id.toUUID())
        call.respond(HttpStatusCode.NoContent)
        logger().info("Deleted text tag with ID: $id")
    }

    delete("/$API_V2/$TEXT_TAGS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) DeleteTextTag request error: missing ID")
            return@delete
        }

        try {
            textTagRep.delete(id.toUUID())
            call.respond(HttpStatusCode.NoContent)
            logger().info("(V2) Deleted text tag with ID: $id")
        } catch (e: DeleteFailedException) {
            call.respond(HttpStatusCode.NotFound, "No text tag found with such ID.")
            logger().error("(V2) Deletion failed: ${e.message}")
        }
    }
}
