package com.example.domain.repository

import com.example.domain.model.SearchResult
import java.util.*

interface ISearchResultsRepository {
    suspend fun insert(model: SearchResult): UUID
    suspend fun insertAll(modelList: List<SearchResult>): List<UUID>
    suspend fun read(id: UUID): SearchResult?
    suspend fun readAll(): List<SearchResult>
    suspend fun update(id: UUID, model: SearchResult)
    suspend fun updateAll(modelMap: Map<UUID, SearchResult>)
    suspend fun delete(id: UUID)
    suspend fun deleteAll()
}
