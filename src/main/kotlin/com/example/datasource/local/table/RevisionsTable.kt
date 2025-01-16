package com.example.datasource.local.table

import com.example.datasource.local.entity.RevisionEntity
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.kotlin.datetime.date


object RevisionsTable : UUIDTable() {
    val textId = reference("text_id", ArticlesTable)
    val authorId = reference("author_id", UsersTable)
    val createdAt = date("created_at")

    fun updateStatement(st: UpdateBuilder<Int>, entity: RevisionEntity) {
        st[textId] = entity.textId
        st[authorId] = entity.authorId
        st[createdAt] = entity.createdAt
    }
}
