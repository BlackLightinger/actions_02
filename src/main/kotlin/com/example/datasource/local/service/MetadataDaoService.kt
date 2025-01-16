package com.example.datasource.local.service

import com.example.data.datasource.local.service.IMetadataDaoService
import com.example.datasource.local.dao.MetadataDao
import com.example.datasource.local.mapper.toDomain
import com.example.datasource.local.mapper.toEntity
import com.example.domain.model.Metadata
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.UUID

class MetadataDaoService(
    private val metadataDao: MetadataDao
) : IMetadataDaoService {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun insert(model: Metadata): UUID = dbQuery { metadataDao.insert(model.toEntity()) }

    override suspend fun insertAll(modelList: List<Metadata>): List<UUID> =
        dbQuery { metadataDao.batchInsert(modelList.map { it.toEntity() }) }

    override suspend fun read(id: UUID): Metadata? = dbQuery { metadataDao.read(id)?.toDomain() }

    override suspend fun readAll(): List<Metadata> = dbQuery { metadataDao.readAll().map { it.toDomain() } }

    override suspend fun update(id: UUID, model: Metadata): Unit = dbQuery { metadataDao.update(id, model.toEntity()) }

    override suspend fun updateAll(modelMap: Map<UUID, Metadata>) =
        dbQuery { metadataDao.updateAll(modelMap.mapValues { it.value.toEntity() }) }

    override suspend fun delete(id: UUID): Unit = dbQuery { metadataDao.delete(id) }

    override suspend fun deleteAll(): Unit = dbQuery { metadataDao.deleteAll() }
}
