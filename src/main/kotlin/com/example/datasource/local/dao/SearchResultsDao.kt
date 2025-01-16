package com.example.datasource.local.dao

import com.example.datasource.local.entity.SearchResultEntity
import com.example.datasource.local.mapper.toSearchResultEntity
import com.example.datasource.local.table.SearchResultsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

abstract class SearchResultsDao {
    fun insert(searchResultEntity: SearchResultEntity): UUID = SearchResultsTable.insert { insertStatement ->
        updateStatement(insertStatement, searchResultEntity)
    }[SearchResultsTable.id].value

    fun batchInsert(searchResultEntitiesList: List<SearchResultEntity>): List<UUID> =
        SearchResultsTable.batchInsert(searchResultEntitiesList) { searchResultEntity ->
            SearchResultsTable.updateStatement(this, searchResultEntity)
        }.map { res -> res[SearchResultsTable.id].value }

    fun read(id: UUID): SearchResultEntity? =
        SearchResultsTable.select { SearchResultsTable.id eq id }.singleOrNull()?.toSearchResultEntity()

    fun readAll(): List<SearchResultEntity> = SearchResultsTable.selectAll().map { it.toSearchResultEntity() }

    fun update(id: UUID, searchResultEntity: SearchResultEntity) =
        SearchResultsTable.update({ SearchResultsTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, searchResultEntity)
        }

    fun updateAll(searchResultEntityMap: Map<UUID, SearchResultEntity>) = searchResultEntityMap.forEach { (id, searchResultEntity) ->
        SearchResultsTable.update({ SearchResultsTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, searchResultEntity)
        }
    }

    fun delete(id: UUID) = SearchResultsTable.deleteWhere { SearchResultsTable.id eq id }

    fun deleteAll() = SearchResultsTable.deleteAll()
}
