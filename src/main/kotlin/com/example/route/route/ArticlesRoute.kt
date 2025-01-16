package com.example.route.route

import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import com.example.domain.repository.IArticlesRepository
import com.example.exceptions.InsertFailedException
import com.example.exceptions.UpdateFailedException
import com.example.exceptions.DeleteFailedException
import com.example.route.mapper.*
import com.example.route.model.ArticleRouteModel
import com.example.route.util.toUUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import com.example.route.util.*
import io.ktor.server.plugins.ContentTransformationException

private const val ARTICLES = "articles"
private const val ID = "id"
private const val API_V1 = "api/v1"
private const val API_V2 = "api/v2"

fun Route.insertArticle() {
    val articleRep by inject<IArticlesRepository>()

    post("/$API_V1/$ARTICLES") {
        val article = call.receive<ArticleRouteModel>()
        val id = try {
            articleRep.insert(article.toDomain())
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.BadRequest, e.message.toString())
            logger().error("Insertion failed: ${e.message}")
            return@post
        }

        val locationUrl = "/$ARTICLES/$id"
        call.response.headers.append(HttpHeaders.Location, locationUrl)
        call.respond(HttpStatusCode.Created)
        logger().info("Inserted article with ID: $id")
    }

    post("/$API_V2/$ARTICLES") {
        try {
            val article = call.receive<ArticleRouteModel>()
            val id = articleRep.insert(article.toDomain())

            val locationUrl = "/$ARTICLES/$id"
            call.response.headers.append(HttpHeaders.Location, locationUrl)
            call.respond(HttpStatusCode.Created)
            logger().info("(V2) Inserted article with ID: $id")
        } catch (e: ContentTransformationException) {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) Invalid request format: ${e.message}")
        } catch (e: InsertFailedException) {
            call.respond(HttpStatusCode.UnprocessableEntity, "Error inserting article.")
            logger().error("(V2) Insert failed: ${e.message}")
        }
    }
}

fun Route.getArticles() {
    val articleRep by inject<IArticlesRepository>()
    get("/$API_V2/$ARTICLES") {
        val articles: List<ArticleRouteModel> = articleRep.readAll().map { article -> article.toRoute() }
        call.respond(HttpStatusCode.OK, articles)
        logger().info("Retrieved all articles")
    }
}

fun Route.getArticle() {
    val articleRep by inject<IArticlesRepository>()

    get("/$API_V1/$ARTICLES/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("GetArticle request error: missing ID")
            return@get
        }

        val article = articleRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound)
            logger().error("No article found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, article.toRoute())
        logger().info("Retrieved article with ID: $id")
    }

    get("/$API_V2/$ARTICLES/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) GetArticle request error: missing ID")
            return@get
        }

        val article = articleRep.read(id.toUUID()) ?: run {
            call.respond(HttpStatusCode.NotFound, "No article found with ID: $id")
            logger().error("(V2) No article found with ID: $id")
            return@get
        }

        call.respond(HttpStatusCode.OK, article.toRoute())
        logger().info("(V2) Retrieved article with ID: $id")
    }
}

fun Route.updateArticle() {
    val articleRep by inject<IArticlesRepository>()

    put("/$API_V1/$ARTICLES/{$ID}") {
        val article = call.receive<ArticleRouteModel>()
        val id = article.id ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("UpdateArticle request error: missing ID")
            return@put
        }

        articleRep.update(id, article.toDomain())
        call.respond(HttpStatusCode.OK)
        logger().info("Updated article with ID: $id")
    }

    put("/$API_V2/$ARTICLES/{$ID}") {
        val article = call.receive<ArticleRouteModel>()
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) UpdateArticle request error: missing ID")
            return@put
        }

        try {
            articleRep.update(id.toUUID(), article.toDomain())
            call.respond(HttpStatusCode.OK)
            logger().info("(V2) Updated article with ID: $id")
        } catch (e: UpdateFailedException) {
            call.respond(HttpStatusCode.UnprocessableEntity, "Error updating article.")
            logger().error("(V2) Update failed: ${e.message}")
        }
    }
}

fun Route.deleteArticle() {
    val articleRep by inject<IArticlesRepository>()

    delete("/$API_V1/$ARTICLES/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest)
            logger().error("DeleteArticle request error: missing ID")
            return@delete
        }

        articleRep.delete(id.toUUID())
        call.respond(HttpStatusCode.NoContent)
        logger().info("Deleted article with ID: $id")
    }

    delete("/$API_V2/$ARTICLES/{$ID}") {
        val id = call.parameters[ID] ?: run {
            call.respond(HttpStatusCode.BadRequest, "Invalid request format. Please check your input.")
            logger().error("(V2) DeleteArticle request error: missing ID")
            return@delete
        }

        try {
            articleRep.delete(id.toUUID())
            call.respond(HttpStatusCode.NoContent)
            logger().info("(V2) Deleted article with ID: $id")
        } catch (e: DeleteFailedException) {
            call.respond(HttpStatusCode.NotFound, "No article found with such ID.")
            logger().error("(V2) Delete failed: ${e.message}")
        }
    }
}
