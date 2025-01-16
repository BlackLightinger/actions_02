package com.example.route.route

import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import com.example.domain.repository.ITagRepository
import com.example.exceptions.InsertFailedException
import com.example.exceptions.UpdateFailedException
import com.example.exceptions.DeleteFailedException
import com.example.route.mapper.*
import com.example.route.model.TagRouteModel
import com.example.route.util.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import com.example.route.util.* // Если есть другие утилиты, добавьте их
import io.ktor.server.plugins.ContentTransformationException

private const val TAGS = "tags"
private const val ID = "id"
private const val API_V1 = "api/v1"
private const val API_V2 = "api/v2"

fun Route.insertTag() {
    val tagRep by inject<ITagRepository>()

    post("/$API_V1/$TAGS") {
        val tag = call.receive<TagRouteModel>()
        val id = try {
            tagRep.insert(tag.toDomain())
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
            logger().error("Insertion failed: ${e.message}")
            return@post
        }

        val locationUrl = "/$TAGS/$id"
        call.response.headers.append(HttpHeaders.Location, locationUrl)
        call.respond(HttpStatusCode.Created)
        logger().info("Inserted tag with ID: $id")
    }

    post("/$API_V2/$TAGS") {
        try {
            val tag = call.receive<TagRouteModel>()
            val id = tagRep.insert(tag.toDomain())

            val locationUrl = "/$TAGS/$id"
            call.response.headers.append(HttpHeaders.Location, locationUrl)
            call.respond(HttpStatusCode.Created)
            logger().info("(V2) Inserted tag with ID: $id")
        } catch (e: ContentTransformationException) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) Invalid request format: ${e.message}")
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.UnprocessableEntity, "Error inserting tag.")
            logger().error("(V2) Insertion failed: ${e.message}")
        }
    }
}

fun Route.getTag() {
    val tagRep by inject<ITagRepository>()

    get("/$API_V1/$TAGS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("GetTag request error: missing ID")
            return@get
        }

        val tag = tagRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound)
            logger().error("No tag found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, tag.toRoute())
        logger().info("Retrieved tag with ID: $id")
    }

    get("/$API_V2/$TAGS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) GetTag request error: missing ID")
            return@get
        }

        val tag = tagRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound, "No tag found with such ID: $id")
            logger().error("(V2) No tag found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, tag.toRoute())
        logger().info("(V2) Retrieved tag with ID: $id")
    }
}

fun Route.updateTag() {
    val tagRep by inject<ITagRepository>()

    put("/$API_V1/$TAGS/{$ID}") {
        val tag = call.receive<TagRouteModel>()
        val id = tag.id ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("UpdateTag request error: missing ID")
            return@put
        }

        tagRep.update(id, tag.toDomain())
        call.respond(HttpStatusCode.OK)
        logger().info("Updated tag with ID: $id")
    }

    put("/$API_V2/$TAGS/{$ID}") {
        val tag = call.receive<TagRouteModel>()
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) UpdateTag request error: missing ID")
            return@put
        }

        try {
            tagRep.update(id.toUUID(), tag.toDomain())
            call.respond(HttpStatusCode.OK)
            logger().info("(V2) Updated tag with ID: $id")
        } catch (e: UpdateFailedException) {
            call.respond(HttpStatusCode.UnprocessableEntity, "Error updating tag.")
            logger().error("(V2) Update failed: ${e.message}")
        }
    }
}

fun Route.deleteTag() {
    val tagRep by inject<ITagRepository>()

    delete("/$API_V1/$TAGS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("DeleteTag request error: missing ID")
            return@delete
        }

        tagRep.delete(id.toUUID())
        call.respond(HttpStatusCode.NoContent)
        logger().info("Deleted tag with ID: $id")
    }

    delete("/$API_V2/$TAGS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) DeleteTag request error: missing ID")
            return@delete
        }

        try {
            tagRep.delete(id.toUUID())
            call.respond(HttpStatusCode.NoContent)
            logger().info("(V2) Deleted tag with ID: $id")
        } catch (e: DeleteFailedException) {
            call.respond(HttpStatusCode.NotFound, "No tag found with such ID.")
            logger().error("(V2) Deletion failed: ${e.message}")
        }
    }
}
