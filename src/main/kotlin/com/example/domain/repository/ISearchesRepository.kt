package com.example.domain.repository

import com.example.domain.model.Search
import java.util.*

interface ISearchesRepository {
    suspend fun insert(model: Search): UUID
    suspend fun insertAll(modelList: List<Search>): List<UUID>
    suspend fun read(id: UUID): Search?
    suspend fun readAll(): List<Search>
    suspend fun update(id: UUID, model: Search)
    suspend fun updateAll(modelMap: Map<UUID, Search>)
    suspend fun delete(id: UUID)
    suspend fun deleteAll()
}
