package com.example.datasource.local.dao

import com.example.datasource.local.entity.CommentEntity
import com.example.datasource.local.mapper.toCommentEntity
import com.example.datasource.local.table.CommentsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

abstract class CommentsDao {
    fun insert(commentEntity: CommentEntity): UUID = CommentsTable.insert { insertStatement ->
        updateStatement(insertStatement, commentEntity)
    }[CommentsTable.id].value

    fun batchInsert(commentEntitiesList: List<CommentEntity>): List<UUID> =
        CommentsTable.batchInsert(commentEntitiesList) { commentEntity ->
            CommentsTable.updateStatement(this, commentEntity)
        }.map { res -> res[CommentsTable.id].value }

    fun read(id: UUID): CommentEntity? =
        CommentsTable.select { CommentsTable.id eq id }.singleOrNull()?.toCommentEntity()

    fun readAll(): List<CommentEntity> = CommentsTable.selectAll().map { it.toCommentEntity() }

    fun update(id: UUID, commentEntity: CommentEntity) =
        CommentsTable.update({ CommentsTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, commentEntity)
        }

    fun updateAll(commentEntityMap: Map<UUID, CommentEntity>) = commentEntityMap.forEach { (id, commentEntity) ->
        CommentsTable.update({ CommentsTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, commentEntity)
        }
    }

    fun delete(id: UUID) = CommentsTable.deleteWhere { CommentsTable.id eq id }

    fun deleteAll() = CommentsTable.deleteAll()
}
