package com.example.data.repository

import com.example.data.datasource.local.service.IRevisionsDaoService
import com.example.domain.model.Revision
import com.example.domain.repository.IRevisionsRepository
import java.util.*

class RevisionRepository(private val revisionsDaoService: IRevisionsDaoService) : IRevisionsRepository {
        override suspend fun insert(model: Revision): UUID = revisionsDaoService.insert(model)
        override suspend fun insertAll(modelList: List<Revision>): List<UUID> = revisionsDaoService.insertAll(modelList)
        override suspend fun read(id: UUID): Revision? = revisionsDaoService.read(id)
        override suspend fun readAll(): List<Revision> = revisionsDaoService.readAll()
        override suspend fun update(id: UUID, model: Revision) = revisionsDaoService.update(id, model)
        override suspend fun updateAll(modelMap: Map<UUID, Revision>) = revisionsDaoService.updateAll(modelMap)
        override suspend fun delete(id: UUID) = revisionsDaoService.delete(id)
        override suspend fun deleteAll() = revisionsDaoService.deleteAll()
        }
