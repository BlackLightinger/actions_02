package com.example.data.repository

import com.example.data.datasource.local.service.ITagsDaoService
import com.example.domain.model.Tag
import com.example.domain.repository.ITagRepository
import java.util.*

class TagRepository(private val tagsDaoService: ITagsDaoService) : ITagRepository {
        override suspend fun insert(model: Tag): UUID = tagsDaoService.insert(model)
        override suspend fun insertAll(modelList: List<Tag>): List<UUID> = tagsDaoService.insertAll(modelList)
        override suspend fun read(id: UUID): Tag? = tagsDaoService.read(id)
        override suspend fun readAll(): List<Tag> = tagsDaoService.readAll()
        override suspend fun update(id: UUID, model: Tag) = tagsDaoService.update(id, model)
        override suspend fun updateAll(modelMap: Map<UUID, Tag>) = tagsDaoService.updateAll(modelMap)
        override suspend fun delete(id: UUID) = tagsDaoService.delete(id)
        override suspend fun deleteAll() = tagsDaoService.deleteAll()
        }
