package com.example.datasource.local.table

import com.example.datasource.local.entity.CommentEntity
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.kotlin.datetime.date

import org.jetbrains.exposed.sql.statements.UpdateBuilder

object CommentsTable : UUIDTable() {
    val textId = reference("text_id", ArticlesTable)
    val userId = reference("user_id", UsersTable)
    val body = text("body")
    val createdAt = date("created_at")

    fun updateStatement(st: UpdateBuilder<Int>, entity: CommentEntity) {
        st[textId] = entity.textId
        st[userId] = entity.userId
        st[body] = entity.body
        st[createdAt] = entity.createdAt
    }
}
