package com.example.datasource.local.dao

import com.example.datasource.local.entity.RevisionEntity
import com.example.datasource.local.mapper.toRevisionEntity
import com.example.datasource.local.table.RevisionsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

abstract class RevisionsDao {
    fun insert(revisionEntity: RevisionEntity): UUID = RevisionsTable.insert { insertStatement ->
        updateStatement(insertStatement, revisionEntity)
    }[RevisionsTable.id].value

    fun batchInsert(revisionEntitiesList: List<RevisionEntity>): List<UUID> =
        RevisionsTable.batchInsert(revisionEntitiesList) { revisionEntity ->
            RevisionsTable.updateStatement(this, revisionEntity)
        }.map { res -> res[RevisionsTable.id].value }

    fun read(id: UUID): RevisionEntity? =
        RevisionsTable.select { RevisionsTable.id eq id }.singleOrNull()?.toRevisionEntity()

    fun readAll(): List<RevisionEntity> = RevisionsTable.selectAll().map { it.toRevisionEntity() }

    fun update(id: UUID, revisionEntity: RevisionEntity) =
        RevisionsTable.update({ RevisionsTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, revisionEntity)
        }

    fun updateAll(revisionEntityMap: Map<UUID, RevisionEntity>) = revisionEntityMap.forEach { (id, revisionEntity) ->
        RevisionsTable.update({ RevisionsTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, revisionEntity)
        }
    }

    fun delete(id: UUID) = RevisionsTable.deleteWhere { RevisionsTable.id eq id }

    fun deleteAll() = RevisionsTable.deleteAll()
}
