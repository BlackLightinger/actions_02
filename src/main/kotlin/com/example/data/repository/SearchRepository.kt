package com.example.data.repository

import com.example.data.datasource.local.service.ISearchesDaoService
import com.example.domain.model.Search
import com.example.domain.repository.ISearchesRepository
import java.util.*

class SearchRepository(private val searchesDaoService: ISearchesDaoService) : ISearchesRepository {
        override suspend fun insert(model: Search): UUID = searchesDaoService.insert(model)
        override suspend fun insertAll(modelList: List<Search>): List<UUID> = searchesDaoService.insertAll(modelList)
        override suspend fun read(id: UUID): Search? = searchesDaoService.read(id)
        override suspend fun readAll(): List<Search> = searchesDaoService.readAll()
        override suspend fun update(id: UUID, model: Search) = searchesDaoService.update(id, model)
        override suspend fun updateAll(modelMap: Map<UUID, Search>) = searchesDaoService.updateAll(modelMap)
        override suspend fun delete(id: UUID) = searchesDaoService.delete(id)
        override suspend fun deleteAll() = searchesDaoService.deleteAll()
        }
