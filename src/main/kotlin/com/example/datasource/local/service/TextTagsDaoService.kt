package com.example.datasource.local.service

import com.example.data.datasource.local.service.ITextTagsDaoService
import com.example.datasource.local.dao.TextTagsDao
import com.example.datasource.local.mapper.toDomain
import com.example.datasource.local.mapper.toEntity
import com.example.domain.model.TextTag
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

class TextTagsDaoService(
    private val textTagsDao: TextTagsDao
) : ITextTagsDaoService {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun insert(model: TextTag): UUID = dbQuery { textTagsDao.insert(model.toEntity()) }

    override suspend fun insertAll(modelList: List<TextTag>): List<UUID> =
        dbQuery { textTagsDao.batchInsert(modelList.map { it.toEntity() }) }

    override suspend fun read(id: UUID): TextTag? = dbQuery { textTagsDao.read(id)?.toDomain() }

    override suspend fun readAll(): List<TextTag> = dbQuery { textTagsDao.readAll().map { it.toDomain() } }

    override suspend fun update(id: UUID, model: TextTag): Unit = dbQuery { textTagsDao.update(id, model.toEntity()) }

    override suspend fun updateAll(modelMap: Map<UUID, TextTag>) =
        dbQuery { textTagsDao.updateAll(modelMap.mapValues { it.value.toEntity() }) }

    override suspend fun delete(id: UUID): Unit = dbQuery { textTagsDao.delete(id) }

    override suspend fun deleteAll(): Unit = dbQuery { textTagsDao.deleteAll() }
}
