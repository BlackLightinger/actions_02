package com.example.datasource.local.service

import com.example.data.datasource.local.service.ISearchResultsDaoService
import com.example.datasource.local.dao.SearchResultsDao
import com.example.datasource.local.mapper.toDomain
import com.example.datasource.local.mapper.toEntity
import com.example.domain.model.SearchResult
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

class SearchResultsDaoService(
    private val searchResultsDao: SearchResultsDao
) : ISearchResultsDaoService {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun insert(model: SearchResult): UUID = dbQuery { searchResultsDao.insert(model.toEntity()) }

    override suspend fun insertAll(modelList: List<SearchResult>): List<UUID> =
        dbQuery { searchResultsDao.batchInsert(modelList.map { it.toEntity() }) }

    override suspend fun read(id: UUID): SearchResult? = dbQuery { searchResultsDao.read(id)?.toDomain() }

    override suspend fun readAll(): List<SearchResult> = dbQuery { searchResultsDao.readAll().map { it.toDomain() } }

    override suspend fun update(id: UUID, model: SearchResult): Unit = dbQuery { searchResultsDao.update(id, model.toEntity()) }

    override suspend fun updateAll(modelMap: Map<UUID, SearchResult>) =
        dbQuery { searchResultsDao.updateAll(modelMap.mapValues { it.value.toEntity() }) }

    override suspend fun delete(id: UUID): Unit = dbQuery { searchResultsDao.delete(id) }

    override suspend fun deleteAll(): Unit = dbQuery { searchResultsDao.deleteAll() }
}
