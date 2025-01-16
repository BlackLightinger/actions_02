package com.example.datasource.local.service

import com.example.data.datasource.local.service.IRevisionsDaoService
import com.example.datasource.local.dao.RevisionsDao
import com.example.datasource.local.mapper.toDomain
import com.example.datasource.local.mapper.toEntity
import com.example.domain.model.Revision
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

class RevisionsDaoService(
    private val revisionsDao: RevisionsDao
) : IRevisionsDaoService {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun insert(model: Revision): UUID = dbQuery { revisionsDao.insert(model.toEntity()) }

    override suspend fun insertAll(modelList: List<Revision>): List<UUID> =
        dbQuery { revisionsDao.batchInsert(modelList.map { it.toEntity() }) }

    override suspend fun read(id: UUID): Revision? = dbQuery { revisionsDao.read(id)?.toDomain() }

    override suspend fun readAll(): List<Revision> = dbQuery { revisionsDao.readAll().map { it.toDomain() } }

    override suspend fun update(id: UUID, model: Revision): Unit = dbQuery { revisionsDao.update(id, model.toEntity()) }

    override suspend fun updateAll(modelMap: Map<UUID, Revision>) =
        dbQuery { revisionsDao.updateAll(modelMap.mapValues { it.value.toEntity() }) }

    override suspend fun delete(id: UUID): Unit = dbQuery { revisionsDao.delete(id) }

    override suspend fun deleteAll(): Unit = dbQuery { revisionsDao.deleteAll() }
}
