package com.example.domain.repository

import com.example.domain.model.Comment
import java.util.*

interface ICommentsRepository {
    suspend fun insert(model: Comment): UUID
    suspend fun insertAll(modelList: List<Comment>): List<UUID>
    suspend fun read(id: UUID): Comment?
    suspend fun readAll(): List<Comment>
    suspend fun update(id: UUID, model: Comment)
    suspend fun updateAll(modelMap: Map<UUID, Comment>)
    suspend fun delete(id: UUID)
    suspend fun deleteAll()
}
