package com.example.datasource.local.dao

import com.example.datasource.local.entity.MetadataEntity
import com.example.datasource.local.mapper.toMetadataEntity
import com.example.datasource.local.table.MetadataTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

abstract class MetadataDao {
    fun insert(metadataEntity: MetadataEntity): UUID = MetadataTable.insert { insertStatement ->
        updateStatement(insertStatement, metadataEntity)
    }[MetadataTable.id].value

    fun batchInsert(metadataEntitiesList: List<MetadataEntity>): List<UUID> =
        MetadataTable.batchInsert(metadataEntitiesList) { metadataEntity ->
            MetadataTable.updateStatement(this, metadataEntity)
        }.map { res -> res[MetadataTable.id].value }

    fun read(id: UUID): MetadataEntity? =
        MetadataTable.select { MetadataTable.id eq id }.singleOrNull()?.toMetadataEntity()

    fun readAll(): List<MetadataEntity> = MetadataTable.selectAll().map { it.toMetadataEntity() }

    fun update(id: UUID, metadataEntity: MetadataEntity) =
        MetadataTable.update({ MetadataTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, metadataEntity)
        }

    fun updateAll(metadataEntityMap: Map<UUID, MetadataEntity>) = metadataEntityMap.forEach { (id, metadataEntity) ->
        MetadataTable.update({ MetadataTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, metadataEntity)
        }
    }

    fun delete(id: UUID) = MetadataTable.deleteWhere { MetadataTable.id eq id }

    fun deleteAll() = MetadataTable.deleteAll()
}
