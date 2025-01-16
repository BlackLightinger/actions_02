package com.example.data.repository

import com.example.data.datasource.local.service.IArticlesDaoService
import com.example.domain.model.Article
import com.example.domain.repository.IArticlesRepository
import java.util.*

class ArticleRepository(private val articlesDaoService: IArticlesDaoService) : IArticlesRepository {
        override suspend fun insert(model: Article): UUID = articlesDaoService.insert(model)
        override suspend fun insertAll(modelList: List<Article>): List<UUID> = articlesDaoService.insertAll(modelList)
        override suspend fun read(id: UUID): Article? = articlesDaoService.read(id)
        override suspend fun readAll(): List<Article> = articlesDaoService.readAll()
        override suspend fun update(id: UUID, model: Article) = articlesDaoService.update(id, model)
        override suspend fun updateAll(modelMap: Map<UUID, Article>) = articlesDaoService.updateAll(modelMap)
        override suspend fun delete(id: UUID) = articlesDaoService.delete(id)
        override suspend fun deleteAll() = articlesDaoService.deleteAll()
        }
