package com.example.plugins

import com.example.datasource.local.table.*
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

private const val BASE_INFO_PATH = "db"
private const val URL_PATH = "url"
private const val USER_PATH = "user"
private const val DRIVER_PATH = "driver"
private const val PASSWORD_PATH = "password"

fun Application.configureDatabases(test: Boolean = false) {
    val envUrl = environment.config.property("$BASE_INFO_PATH.$URL_PATH").getString()
    val url = if (test) envUrl.replace("test", "test-${UUID.randomUUID()}") else envUrl
    val user = environment.config.property("$BASE_INFO_PATH.$USER_PATH").getString()
    val driver = environment.config.property("$BASE_INFO_PATH.$DRIVER_PATH").getString()
    val password = environment.config.property("$BASE_INFO_PATH.$PASSWORD_PATH").getString()

    val databaseConnect = Database.connect(
        url = url,
        user = user,
        driver = driver,
        password = password
    )

    createTables()
}

private fun createTables() = transaction {
    SchemaUtils.create(
        UsersTable,
        ArticlesTable,
        MetadataTable,
        TagsTable,
        CommentsTable,
        RevisionsTable,
        SearchesTable,
        SearchResultsTable,
        TextTagsTable,
    )
}
