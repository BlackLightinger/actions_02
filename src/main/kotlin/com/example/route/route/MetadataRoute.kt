package com.example.route.route

import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import com.example.domain.repository.IMetadataRepository
import com.example.exceptions.InsertFailedException
import com.example.exceptions.UpdateFailedException
import com.example.exceptions.DeleteFailedException
import com.example.route.mapper.*
import com.example.route.model.MetadataRouteModel
import com.example.route.util.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import com.example.route.util.* // Если есть другие утилиты, добавьте их
import io.ktor.server.plugins.ContentTransformationException

private const val METADATA = "metadata"
private const val ID = "id"
private const val API_V1 = "api/v1"
private const val API_V2 = "api/v2"

fun Route.insertMetadata() {
    val metadataRep by inject<IMetadataRepository>()

    post("/$API_V1/$METADATA") {
        val metadata = call.receive<MetadataRouteModel>()
        val id = try {
            metadataRep.insert(metadata.toDomain())
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
            logger().error("Insertion failed: ${e.message}")
            return@post
        }

        val locationUrl = "/$METADATA/$id"
        call.response.headers.append(HttpHeaders.Location, locationUrl)
        call.respond(HttpStatusCode.Created)
        logger().info("Inserted metadata with ID: $id")
    }

    post("/$API_V2/$METADATA") {
        try {
            val metadata = call.receive<MetadataRouteModel>()
            val id = metadataRep.insert(metadata.toDomain())

            val locationUrl = "/$METADATA/$id"
            call.response.headers.append(HttpHeaders.Location, locationUrl)
            call.respond(HttpStatusCode.Created)
            logger().info("(V2) Inserted metadata with ID: $id")
        } catch (e: ContentTransformationException) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) Invalid request format: ${e.message}")
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.UnprocessableEntity, "Error inserting metadata.")
            logger().error("(V2) Insertion failed: ${e.message}")
        }
    }
}

fun Route.getMetadata() {
    val metadataRep by inject<IMetadataRepository>()

    get("/$API_V1/$METADATA/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("GetMetadata request error: missing ID")
            return@get
        }

        val metadata = metadataRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound)
            logger().error("No metadata found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, metadata.toRoute())
        logger().info("Retrieved metadata with ID: $id")
    }

    get("/$API_V2/$METADATA/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) GetMetadata request error: missing ID")
            return@get
        }

        val metadata = metadataRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound, "No metadata found with such ID: $id")
            logger().error("(V2) No metadata found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, metadata.toRoute())
        logger().info("(V2) Retrieved metadata with ID: $id")
    }
}

fun Route.updateMetadata() {
    val metadataRep by inject<IMetadataRepository>()

    put("/$API_V1/$METADATA/{$ID}") {
        val metadata = call.receive<MetadataRouteModel>()
        val id = metadata.id ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("UpdateMetadata request error: missing ID")
            return@put
        }

        metadataRep.update(id, metadata.toDomain())
        call.respond(HttpStatusCode.OK)
        logger().info("Updated metadata with ID: $id")
    }

    put("/$API_V2/$METADATA/{$ID}") {
        val metadata = call.receive<MetadataRouteModel>()
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) UpdateMetadata request error: missing ID")
            return@put
        }

        try {
            metadataRep.update(id.toUUID(), metadata.toDomain())
            call.respond(HttpStatusCode.OK)
            logger().info("(V2) Updated metadata with ID: $id")
        } catch (e: UpdateFailedException) {
            call.respond(HttpStatusCode.UnprocessableEntity, "Error updating metadata.")
            logger().error("(V2) Update failed: ${e.message}")
        }
    }
}

fun Route.deleteMetadata() {
    val metadataRep by inject<IMetadataRepository>()

    delete("/$API_V1/$METADATA/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("DeleteMetadata request error: missing ID")
            return@delete
        }

        metadataRep.delete(id.toUUID())
        call.respond(HttpStatusCode.NoContent)
        logger().info("Deleted metadata with ID: $id")
    }

    delete("/$API_V2/$METADATA/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) DeleteMetadata request error: missing ID")
            return@delete
        }

        try {
            metadataRep.delete(id.toUUID())
            call.respond(HttpStatusCode.NoContent)
            logger().info("(V2) Deleted metadata with ID: $id")
        } catch (e: DeleteFailedException) {
            call.respond(HttpStatusCode.NotFound, "No metadata found with such ID.")
            logger().error("(V2) Deletion failed: ${e.message}")
        }
    }
}
