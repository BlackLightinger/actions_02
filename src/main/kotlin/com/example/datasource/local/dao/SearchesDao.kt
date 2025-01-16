package com.example.datasource.local.dao

import com.example.datasource.local.entity.SearchEntity
import com.example.datasource.local.mapper.toSearchEntity
import com.example.datasource.local.table.SearchesTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

abstract class SearchesDao {
    fun insert(searchEntity: SearchEntity): UUID = SearchesTable.insert { insertStatement ->
        updateStatement(insertStatement, searchEntity)
    }[SearchesTable.id].value

    fun batchInsert(searchEntitiesList: List<SearchEntity>): List<UUID> =
        SearchesTable.batchInsert(searchEntitiesList) { searchEntity ->
            SearchesTable.updateStatement(this, searchEntity)
        }.map { res -> res[SearchesTable.id].value }

    fun read(id: UUID): SearchEntity? =
        SearchesTable.select { SearchesTable.id eq id }.singleOrNull()?.toSearchEntity()

    fun readAll(): List<SearchEntity> = SearchesTable.selectAll().map { it.toSearchEntity() }

    fun update(id: UUID, searchEntity: SearchEntity) =
        SearchesTable.update({ SearchesTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, searchEntity)
        }

    fun updateAll(searchEntityMap: Map<UUID, SearchEntity>) = searchEntityMap.forEach { (id, searchEntity) ->
        SearchesTable.update({ SearchesTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, searchEntity)
        }
    }

    fun delete(id: UUID) = SearchesTable.deleteWhere { SearchesTable.id eq id }

    fun deleteAll() = SearchesTable.deleteAll()
}
