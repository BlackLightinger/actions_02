package com.example.datasource.local.table

import com.example.datasource.local.entity.ArticleEntity
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object ArticlesTable : UUIDTable() {
    val title = varchar("title", 256)
    val body = text("body")
    val authorId = reference("author_id", UsersTable)
    val createdAt = date("created_at")
    val updatedAt = date("updated_at")

    fun updateStatement(st: UpdateBuilder<Int>, entity: ArticleEntity) {
        st[title] = entity.title
        st[body] = entity.body
        st[authorId] = entity.authorId
        st[createdAt] = entity.createdAt
        st[updatedAt] = entity.updatedAt
    }
}
