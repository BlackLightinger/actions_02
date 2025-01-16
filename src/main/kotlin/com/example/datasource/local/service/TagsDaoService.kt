package com.example.datasource.local.service

import com.example.data.datasource.local.service.ITagsDaoService
import com.example.datasource.local.dao.TagsDao
import com.example.datasource.local.mapper.toDomain
import com.example.datasource.local.mapper.toEntity
import com.example.domain.model.Tag
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

class TagsDaoService(
    private val tagsDao: TagsDao
) : ITagsDaoService {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun insert(model: Tag): UUID = dbQuery { tagsDao.insert(model.toEntity()) }

    override suspend fun insertAll(modelList: List<Tag>): List<UUID> =
        dbQuery { tagsDao.batchInsert(modelList.map { it.toEntity() }) }

    override suspend fun read(id: UUID): Tag? = dbQuery { tagsDao.read(id)?.toDomain() }

    override suspend fun readAll(): List<Tag> = dbQuery { tagsDao.readAll().map { it.toDomain() } }

    override suspend fun update(id: UUID, model: Tag): Unit = dbQuery { tagsDao.update(id, model.toEntity()) }

    override suspend fun updateAll(modelMap: Map<UUID, Tag>) =
        dbQuery { tagsDao.updateAll(modelMap.mapValues { it.value.toEntity() }) }

    override suspend fun delete(id: UUID): Unit = dbQuery { tagsDao.delete(id) }

    override suspend fun deleteAll(): Unit = dbQuery { tagsDao.deleteAll() }
}
