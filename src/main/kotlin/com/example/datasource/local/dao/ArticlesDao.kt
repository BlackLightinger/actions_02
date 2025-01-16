package com.example.datasource.local.dao

import com.example.datasource.local.entity.ArticleEntity
import com.example.datasource.local.mapper.toArticleEntity
import com.example.datasource.local.table.ArticlesTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

abstract class ArticlesDao {
    fun insert(articleEntity: ArticleEntity): UUID = ArticlesTable.insert { insertStatement ->
        updateStatement(insertStatement, articleEntity)
    }[ArticlesTable.id].value

    fun batchInsert(articleEntitiesList: List<ArticleEntity>): List<UUID> =
        ArticlesTable.batchInsert(articleEntitiesList) { articleEntity ->
            ArticlesTable.updateStatement(this, articleEntity)
        }.map { res -> res[ArticlesTable.id].value }

    fun read(id: UUID): ArticleEntity? =
        ArticlesTable.select { ArticlesTable.id eq id }.singleOrNull()?.toArticleEntity()

    fun readAll(): List<ArticleEntity> = ArticlesTable.selectAll().map { it.toArticleEntity() }

    fun update(id: UUID, articleEntity: ArticleEntity) =
        ArticlesTable.update({ ArticlesTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, articleEntity)
        }

    fun updateAll(articleEntityMap: Map<UUID, ArticleEntity>) = articleEntityMap.forEach { (id, articleEntity) ->
        ArticlesTable.update({ ArticlesTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, articleEntity)
        }
    }

    fun delete(id: UUID) = ArticlesTable.deleteWhere { ArticlesTable.id eq id }

    fun deleteAll() = ArticlesTable.deleteAll()
}
