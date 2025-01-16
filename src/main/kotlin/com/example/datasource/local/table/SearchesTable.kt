package com.example.datasource.local.table

import com.example.datasource.local.entity.SearchEntity
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.kotlin.datetime.date


object SearchesTable : UUIDTable() {
    val userId = reference("user_id", UsersTable)
    val query = text("query")
    val createdAt = date("created_at")

    fun updateStatement(st: UpdateBuilder<Int>, entity: SearchEntity) {
        st[userId] = entity.userId
        st[query] = entity.query
        st[createdAt] = entity.createdAt
    }
}
