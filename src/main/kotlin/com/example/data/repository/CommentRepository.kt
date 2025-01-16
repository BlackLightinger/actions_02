package com.example.data.repository

import com.example.data.datasource.local.service.ICommentsDaoService
import com.example.domain.model.Comment
import com.example.domain.repository.ICommentsRepository
import java.util.*

class CommentRepository(private val commentsDaoService: ICommentsDaoService) : ICommentsRepository {
        override suspend fun insert(model: Comment): UUID = commentsDaoService.insert(model)
        override suspend fun insertAll(modelList: List<Comment>): List<UUID> = commentsDaoService.insertAll(modelList)
        override suspend fun read(id: UUID): Comment? = commentsDaoService.read(id)
        override suspend fun readAll(): List<Comment> = commentsDaoService.readAll()
        override suspend fun update(id: UUID, model: Comment) = commentsDaoService.update(id, model)
        override suspend fun updateAll(modelMap: Map<UUID, Comment>) = commentsDaoService.updateAll(modelMap)
        override suspend fun delete(id: UUID) = commentsDaoService.delete(id)
        override suspend fun deleteAll() = commentsDaoService.deleteAll()
        }
