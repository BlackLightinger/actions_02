package com.example.domain.repository

import com.example.domain.model.TextTag
import java.util.*

interface ITextTagsRepository {
    suspend fun insert(model: TextTag): UUID
    suspend fun insertAll(modelList: List<TextTag>): List<UUID>
    suspend fun read(textId: UUID): TextTag?
    suspend fun readAll(): List<TextTag>
    suspend fun delete(textId: UUID)
    suspend fun deleteAll()
}
