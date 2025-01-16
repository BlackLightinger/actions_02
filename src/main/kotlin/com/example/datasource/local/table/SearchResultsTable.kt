package com.example.datasource.local.table

import com.example.datasource.local.entity.SearchResultEntity
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object SearchResultsTable : UUIDTable() {
    val searchId = reference("search_id", SearchesTable)
    val textId = reference("text_id", ArticlesTable)
    val relevance = double("relevance")

    fun updateStatement(st: UpdateBuilder<Int>, entity: SearchResultEntity) {
        st[searchId] = entity.searchId
        st[textId] = entity.textId
        st[relevance] = entity.relevance
    }
}
