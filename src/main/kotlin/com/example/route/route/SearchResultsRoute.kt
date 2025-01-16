package com.example.route.route

import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import com.example.domain.repository.ISearchResultsRepository
import com.example.exceptions.InsertFailedException
import com.example.exceptions.UpdateFailedException
import com.example.exceptions.DeleteFailedException
import com.example.route.mapper.*
import com.example.route.model.SearchResultRouteModel
import com.example.route.util.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import com.example.route.util.* // Если есть другие утилиты, добавьте их
import io.ktor.server.plugins.ContentTransformationException

private const val SEARCH_RESULTS = "searchResults"
private const val ID = "id"
private const val API_V1 = "api/v1"
private const val API_V2 = "api/v2"

fun Route.insertSearchResult() {
    val searchResultRep by inject<ISearchResultsRepository>()

    post("/$API_V1/$SEARCH_RESULTS") {
        val searchResult = call.receive<SearchResultRouteModel>()
        val id = try {
            searchResultRep.insert(searchResult.toDomain())
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
            logger().error("Insertion failed: ${e.message}")
            return@post
        }

        val locationUrl = "/$SEARCH_RESULTS/$id"
        call.response.headers.append(HttpHeaders.Location, locationUrl)
        call.respond(HttpStatusCode.Created)
        logger().info("Inserted search result with ID: $id")
    }

    post("/$API_V2/$SEARCH_RESULTS") {
        try {
            val searchResult = call.receive<SearchResultRouteModel>()
            val id = searchResultRep.insert(searchResult.toDomain())

            val locationUrl = "/$SEARCH_RESULTS/$id"
            call.response.headers.append(HttpHeaders.Location, locationUrl)
            call.respond(HttpStatusCode.Created)
            logger().info("(V2) Inserted search result with ID: $id")
        } catch (e: ContentTransformationException) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) Invalid request format: ${e.message}")
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.UnprocessableEntity, "Error inserting search result.")
            logger().error("(V2) Insertion failed: ${e.message}")
        }
    }
}

fun Route.getSearchResult() {
    val searchResultRep by inject<ISearchResultsRepository>()

    get("/$API_V1/$SEARCH_RESULTS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("GetSearchResult request error: missing ID")
            return@get
        }

        val searchResult = searchResultRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound)
            logger().error("No search result found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, searchResult.toRoute())
        logger().info("Retrieved search result with ID: $id")
    }

    get("/$API_V2/$SEARCH_RESULTS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) GetSearchResult request error: missing ID")
            return@get
        }

        val searchResult = searchResultRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound, "No search result found with such ID: $id")
            logger().error("(V2) No search result found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, searchResult.toRoute())
        logger().info("(V2) Retrieved search result with ID: $id")
    }
}

fun Route.updateSearchResult() {
    val searchResultRep by inject<ISearchResultsRepository>()

    put("/$API_V1/$SEARCH_RESULTS/{$ID}") {
        val searchResult = call.receive<SearchResultRouteModel>()
        val id = searchResult.id ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("UpdateSearchResult request error: missing ID")
            return@put
        }

        searchResultRep.update(id, searchResult.toDomain())
        call.respond(HttpStatusCode.OK)
        logger().info("Updated search result with ID: $id")
    }

    put("/$API_V2/$SEARCH_RESULTS/{$ID}") {
        val searchResult = call.receive<SearchResultRouteModel>()
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) UpdateSearchResult request error: missing ID")
            return@put
        }

        try {
            searchResultRep.update(id.toUUID(), searchResult.toDomain())
            call.respond(HttpStatusCode.OK)
            logger().info("(V2) Updated search result with ID: $id")
        } catch (e: UpdateFailedException) {
            call.respond(HttpStatusCode.UnprocessableEntity, "Error updating search result.")
            logger().error("(V2) Update failed: ${e.message}")
        }
    }
}

fun Route.deleteSearchResult() {
    val searchResultRep by inject<ISearchResultsRepository>()

    delete("/$API_V1/$SEARCH_RESULTS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("DeleteSearchResult request error: missing ID")
            return@delete
        }

        searchResultRep.delete(id.toUUID())
        call.respond(HttpStatusCode.NoContent)
        logger().info("Deleted search result with ID: $id")
    }

    delete("/$API_V2/$SEARCH_RESULTS/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) DeleteSearchResult request error: missing ID")
            return@delete
        }

        try {
            searchResultRep.delete(id.toUUID())
            call.respond(HttpStatusCode.NoContent)
            logger().info("(V2) Deleted search result with ID: $id")
        } catch (e: DeleteFailedException) {
            call.respond(HttpStatusCode.NotFound, "No search result found with such ID.")
            logger().error("(V2) Deletion failed: ${e.message}")
        }
    }
}
