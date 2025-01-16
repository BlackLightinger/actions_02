package com.example.data.repository

import com.example.data.datasource.local.service.IMetadataDaoService
import com.example.domain.model.Metadata
import com.example.domain.repository.IMetadataRepository
import java.util.*

class MetadataRepository(private val metadataDaoService: IMetadataDaoService) : IMetadataRepository {
        override suspend fun insert(model: Metadata): UUID = metadataDaoService.insert(model)
        override suspend fun insertAll(modelList: List<Metadata>): List<UUID> = metadataDaoService.insertAll(modelList)
        override suspend fun read(id: UUID): Metadata? = metadataDaoService.read(id)
        override suspend fun readAll(): List<Metadata> = metadataDaoService.readAll()
        override suspend fun update(id: UUID, model: Metadata) = metadataDaoService.update(id, model)
        override suspend fun updateAll(modelMap: Map<UUID, Metadata>) = metadataDaoService.updateAll(modelMap)
        override suspend fun delete(id: UUID) = metadataDaoService.delete(id)
        override suspend fun deleteAll() = metadataDaoService.deleteAll()
        }
