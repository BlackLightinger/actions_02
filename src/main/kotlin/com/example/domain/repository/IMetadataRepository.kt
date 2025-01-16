package com.example.domain.repository

import com.example.domain.model.Metadata
import java.util.*

interface IMetadataRepository {
    suspend fun insert(model: Metadata): UUID
    suspend fun insertAll(modelList: List<Metadata>): List<UUID>
    suspend fun read(id: UUID): Metadata?
    suspend fun readAll(): List<Metadata>
    suspend fun update(id: UUID, model: Metadata)
    suspend fun updateAll(modelMap: Map<UUID, Metadata>)
    suspend fun delete(id: UUID)
    suspend fun deleteAll()
}
