package com.example.datasource.local.service

import com.example.data.datasource.local.service.ICommentsDaoService
import com.example.datasource.local.dao.CommentsDao
import com.example.datasource.local.mapper.toDomain
import com.example.datasource.local.mapper.toEntity
import com.example.domain.model.Comment
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

class CommentsDaoService(
    private val commentsDao: CommentsDao
) : ICommentsDaoService {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun insert(model: Comment): UUID = dbQuery { commentsDao.insert(model.toEntity()) }

    override suspend fun insertAll(modelList: List<Comment>): List<UUID> =
        dbQuery { commentsDao.batchInsert(modelList.map { it.toEntity() }) }

    override suspend fun read(id: UUID): Comment? = dbQuery { commentsDao.read(id)?.toDomain() }

    override suspend fun readAll(): List<Comment> = dbQuery { commentsDao.readAll().map { it.toDomain() } }

    override suspend fun update(id: UUID, model: Comment): Unit = dbQuery { commentsDao.update(id, model.toEntity()) }

    override suspend fun updateAll(modelMap: Map<UUID, Comment>) =
        dbQuery { commentsDao.updateAll(modelMap.mapValues { it.value.toEntity() }) }

    override suspend fun delete(id: UUID): Unit = dbQuery { commentsDao.delete(id) }

    override suspend fun deleteAll(): Unit = dbQuery { commentsDao.deleteAll() }
}
