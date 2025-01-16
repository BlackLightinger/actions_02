package com.example.datasource.local.dao

import com.example.datasource.local.entity.TagEntity
import com.example.datasource.local.mapper.toTagEntity
import com.example.datasource.local.table.TagsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

abstract class TagsDao {
    fun insert(tagEntity: TagEntity): UUID = TagsTable.insert { insertStatement ->
        updateStatement(insertStatement, tagEntity)
    }[TagsTable.id].value

    fun batchInsert(tagEntitiesList: List<TagEntity>): List<UUID> =
        TagsTable.batchInsert(tagEntitiesList) { tagEntity ->
            TagsTable.updateStatement(this, tagEntity)
        }.map { res -> res[TagsTable.id].value }

    fun read(id: UUID): TagEntity? =
        TagsTable.select { TagsTable.id eq id }.singleOrNull()?.toTagEntity()

    fun readAll(): List<TagEntity> = TagsTable.selectAll().map { it.toTagEntity() }

    fun update(id: UUID, tagEntity: TagEntity) =
        TagsTable.update({ TagsTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, tagEntity)
        }

    fun updateAll(tagEntityMap: Map<UUID, TagEntity>) = tagEntityMap.forEach { (id, tagEntity) ->
        TagsTable.update({ TagsTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, tagEntity)
        }
    }

    fun delete(id: UUID) = TagsTable.deleteWhere { TagsTable.id eq id }

    fun deleteAll() = TagsTable.deleteAll()
}
