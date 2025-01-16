package com.example.data.repository

import com.example.data.datasource.local.service.ITextTagsDaoService
import com.example.domain.model.TextTag
import com.example.domain.repository.ITextTagsRepository
import java.util.*

class TextTagsRepository(private val textTagsDaoService: ITextTagsDaoService) : ITextTagsRepository {
        override suspend fun insert(model: TextTag): UUID = textTagsDaoService.insert(model)
        override suspend fun insertAll(modelList: List<TextTag>): List<UUID> = textTagsDaoService.insertAll(modelList)
        override suspend fun read(id: UUID): TextTag? = textTagsDaoService.read(id)
        override suspend fun readAll(): List<TextTag> = textTagsDaoService.readAll()
        override suspend fun delete(id: UUID) = textTagsDaoService.delete(id)
        override suspend fun deleteAll() = textTagsDaoService.deleteAll()
        }
