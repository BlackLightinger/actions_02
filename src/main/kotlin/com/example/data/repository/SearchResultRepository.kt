package com.example.data.repository

import com.example.data.datasource.local.service.ISearchResultsDaoService
import com.example.domain.model.SearchResult
import com.example.domain.repository.ISearchResultsRepository
import java.util.*

class SearchResultRepository(private val searchResultsDaoService: ISearchResultsDaoService) : ISearchResultsRepository {
        override suspend fun insert(model: SearchResult): UUID = searchResultsDaoService.insert(model)
        override suspend fun insertAll(modelList: List<SearchResult>): List<UUID> = searchResultsDaoService.insertAll(modelList)
        override suspend fun read(id: UUID): SearchResult? = searchResultsDaoService.read(id)
        override suspend fun readAll(): List<SearchResult> = searchResultsDaoService.readAll()
        override suspend fun update(id: UUID, model: SearchResult) = searchResultsDaoService.update(id, model)
        override suspend fun updateAll(modelMap: Map<UUID, SearchResult>) = searchResultsDaoService.updateAll(modelMap)
        override suspend fun delete(id: UUID) = searchResultsDaoService.delete(id)
        override suspend fun deleteAll() = searchResultsDaoService.deleteAll()
        }
