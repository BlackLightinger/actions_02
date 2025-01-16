package com.example.datasource.local.service

import com.example.data.datasource.local.service.ISearchesDaoService
import com.example.datasource.local.dao.SearchesDao
import com.example.datasource.local.mapper.toDomain
import com.example.datasource.local.mapper.toEntity
import com.example.domain.model.Search
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

class SearchesDaoService(
    private val searchesDao: SearchesDao
) : ISearchesDaoService {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun insert(model: Search): UUID = dbQuery { searchesDao.insert(model.toEntity()) }

    override suspend fun insertAll(modelList: List<Search>): List<UUID> =
        dbQuery { searchesDao.batchInsert(modelList.map { it.toEntity() }) }

    override suspend fun read(id: UUID): Search? = dbQuery { searchesDao.read(id)?.toDomain() }

    override suspend fun readAll(): List<Search> = dbQuery { searchesDao.readAll().map { it.toDomain() } }

    override suspend fun update(id: UUID, model: Search): Unit = dbQuery { searchesDao.update(id, model.toEntity()) }

    override suspend fun updateAll(modelMap: Map<UUID, Search>) =
        dbQuery { searchesDao.updateAll(modelMap.mapValues { it.value.toEntity() }) }

    override suspend fun delete(id: UUID): Unit = dbQuery { searchesDao.delete(id) }

    override suspend fun deleteAll(): Unit = dbQuery { searchesDao.deleteAll() }
}
