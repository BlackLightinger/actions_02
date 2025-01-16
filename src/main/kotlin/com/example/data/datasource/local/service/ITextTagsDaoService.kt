package com.example.data.datasource.local.service

import com.example.domain.model.TextTag
import java.util.*

interface ITextTagsDaoService {
    suspend fun insert(model: TextTag): UUID
    suspend fun insertAll(modelList: List<TextTag>): List<UUID>
    suspend fun read(id: UUID): TextTag?
    suspend fun readAll(): List<TextTag>
    suspend fun update(id: UUID, model: TextTag)
    suspend fun updateAll(modelMap: Map<UUID, TextTag>)
    suspend fun delete(id: UUID)
    suspend fun deleteAll()
}
