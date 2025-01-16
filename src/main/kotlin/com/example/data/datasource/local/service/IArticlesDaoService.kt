package com.example.data.datasource.local.service

import com.example.domain.model.Article
import java.util.UUID

interface IArticlesDaoService {
    suspend fun insert(model: Article): UUID
    suspend fun insertAll(modelList: List<Article>): List<UUID>
    suspend fun read(id: UUID): Article?
    suspend fun readAll(): List<Article>
    suspend fun update(id: UUID, model: Article)
    suspend fun updateAll(modelMap: Map<UUID, Article>)
    suspend fun delete(id: UUID)
    suspend fun deleteAll()
}
