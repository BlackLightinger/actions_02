package com.example.datasource.local.service

import com.example.data.datasource.local.service.IArticlesDaoService
import com.example.datasource.local.dao.ArticlesDao
import com.example.datasource.local.mapper.toDomain
import com.example.datasource.local.mapper.toEntity
import com.example.domain.model.Article
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

class ArticlesDaoService(
    private val articlesDao: ArticlesDao
) : IArticlesDaoService {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun insert(model: Article): UUID = dbQuery { articlesDao.insert(model.toEntity()) }

    override suspend fun insertAll(modelList: List<Article>): List<UUID> =
        dbQuery { articlesDao.batchInsert(modelList.map { it.toEntity() }) }

    override suspend fun read(id: UUID): Article? = dbQuery { articlesDao.read(id)?.toDomain() }

    override suspend fun readAll(): List<Article> = dbQuery { articlesDao.readAll().map { it.toDomain() } }

    override suspend fun update(id: UUID, model: Article): Unit = dbQuery { articlesDao.update(id, model.toEntity()) }

    override suspend fun updateAll(modelMap: Map<UUID, Article>) =
        dbQuery { articlesDao.updateAll(modelMap.mapValues { it.value.toEntity() }) }

    override suspend fun delete(id: UUID): Unit = dbQuery { articlesDao.delete(id) }

    override suspend fun deleteAll(): Unit = dbQuery { articlesDao.deleteAll() }
}
