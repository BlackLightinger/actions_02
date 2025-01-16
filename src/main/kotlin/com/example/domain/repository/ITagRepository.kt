package com.example.domain.repository

import com.example.domain.model.Tag
import java.util.*

interface ITagRepository {
    suspend fun insert(model: Tag): UUID
    suspend fun insertAll(modelList: List<Tag>): List<UUID>
    suspend fun read(id: UUID): Tag?
    suspend fun readAll(): List<Tag>
    suspend fun update(id: UUID, model: Tag)
    suspend fun updateAll(modelMap: Map<UUID, Tag>)
    suspend fun delete(id: UUID)
    suspend fun deleteAll()
}
