package com.example.datasource.local.dao

import com.example.datasource.local.entity.TextTagEntity
import com.example.datasource.local.mapper.toTextTagEntity
import com.example.datasource.local.table.TextTagsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

abstract class TextTagsDao {
    fun insert(textTagEntity: TextTagEntity): UUID = TextTagsTable.insert { insertStatement ->
        updateStatement(insertStatement, textTagEntity)
    }[TextTagsTable.id].value

    fun batchInsert(textTagEntitiesList: List<TextTagEntity>): List<UUID> =
        TextTagsTable.batchInsert(textTagEntitiesList) { textTagEntity ->
            TextTagsTable.updateStatement(this, textTagEntity)
        }.map { res -> res[TextTagsTable.id].value }

    fun read(id: UUID): TextTagEntity? =
        TextTagsTable.select { TextTagsTable.id eq id }.singleOrNull()?.toTextTagEntity()

    fun readAll(): List<TextTagEntity> = TextTagsTable.selectAll().map { it.toTextTagEntity() }

    fun update(id: UUID, textTagEntity: TextTagEntity) =
        TextTagsTable.update({ TextTagsTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, textTagEntity)
        }

    fun updateAll(textTagEntityMap: Map<UUID, TextTagEntity>) = textTagEntityMap.forEach { (id, textTagEntity) ->
        TextTagsTable.update({ TextTagsTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, textTagEntity)
        }
    }

    fun delete(id: UUID) = TextTagsTable.deleteWhere { TextTagsTable.id eq id }

    fun deleteAll() = TextTagsTable.deleteAll()
}
