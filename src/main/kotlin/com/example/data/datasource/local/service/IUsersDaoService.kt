package com.example.data.datasource.local.service

import com.example.domain.model.User
import java.util.*

interface IUsersDaoService {
    suspend fun insert(model: User): UUID
    suspend fun insertAll(modelList: List<User>): List<UUID>
    suspend fun read(id: UUID): User?
    suspend fun read(email: String): User?
    suspend fun read(email: String, password: String): User?
    suspend fun readAll(): List<User>
    suspend fun update(id: UUID, model: User)
    suspend fun updateAll(modelMap: Map<UUID, User>)
    suspend fun delete(id: UUID)
    suspend fun deleteAll()
}
