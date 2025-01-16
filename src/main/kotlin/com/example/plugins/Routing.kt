package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.example.route.route.*
import io.ktor.server.auth.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.swagger.*


fun Application.configureRouting() {
    configureArticlesRouting()
    configureCommentsRouting()
    configureMetadataRouting()
    configureRevisionsRouting()
    configureSearchesRouting()
    configureSearchResultsRouting()
    configureTagsRouting()
    configureTextTagsRouting()
    configureUsersRouting()

    configureStartRouting()

    configureSwagger()
}

private fun Application.configureStartRouting() {
    routing {
        staticResources("/legacy/static", "static")
        staticResources("/legacy/api/v1/static", "static")
        staticResources("/api/v1/static", "static")
        staticResources("/static", "static")
        staticResources("/login/static", "static")
        staticResources("/articles/static", "static")
        staticResources("/comments/static", "static")
        staticResources("/searches/static", "static")
        staticResources("/tag/static", "static")
        staticResources("/texttags/static", "static")
        staticResources("/users/static", "static")
        mainPage()
    }
}

private fun Application.configureArticlesRouting() {
    routing {
        authenticate(AUTH_BASIC) {
            insertArticle()
            getArticle()
            updateArticle()
            deleteArticle()
        }
    }
}

private fun Application.configureCommentsRouting() {
    routing {
        authenticate(AUTH_BASIC) {
            insertComment()
            getComment()
            updateComment()
            deleteComment()
        }
    }
}

private fun Application.configureMetadataRouting() {
    routing {
        authenticate(AUTH_BASIC) {
            insertMetadata()
            getMetadata()
            updateMetadata()
            deleteMetadata()
        }
    }
}

private fun Application.configureRevisionsRouting() {
    routing {
        authenticate(AUTH_BASIC) {
            insertRevision()
            getRevision()
            updateRevision()
            deleteRevision()
        }
    }
}

private fun Application.configureSearchesRouting() {
    routing {
        authenticate(AUTH_BASIC) {
            insertSearch()
            getSearch()
            updateSearch()
            deleteSearch()
        }
    }
}

private fun Application.configureSearchResultsRouting() {
    routing {
        authenticate(AUTH_BASIC) {
            insertSearchResult()
            getSearchResult()
            updateSearchResult()
            deleteSearchResult()
        }
    }
}

private fun Application.configureTagsRouting() {
    routing {
        authenticate(AUTH_BASIC) {
            insertTag()
            getTag()
            updateTag()
            deleteTag()
        }
    }
}

private fun Application.configureTextTagsRouting() {
    routing {
        authenticate(AUTH_BASIC) {
            insertTextTag()
            getTextTag()
            deleteTextTag()
        }
    }
}

private fun Application.configureUsersRouting() {
    routing {
        insertUser()
        authenticate(AUTH_BASIC) {
            getUser()
            updateUser()
            deleteUser()
//            editUserForm()
        }
    }
}

private fun Application.configureSwagger() {
    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
    }
}