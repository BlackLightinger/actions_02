package com.example.data.repository

import com.example.data.datasource.local.service.IUsersDaoService
import com.example.domain.model.User
import com.example.domain.repository.IUsersRepository
import java.util.*

class UserRepository(private val usersDaoService: IUsersDaoService) : IUsersRepository {
        override suspend fun insert(model: User): UUID = usersDaoService.insert(model)
        override suspend fun insertAll(modelList: List<User>): List<UUID> = usersDaoService.insertAll(modelList)
        override suspend fun read(id: UUID): User? = usersDaoService.read(id)
        override suspend fun read(email: String): User? = usersDaoService.read(email)
        override suspend fun read(email: String, password: String): User? = usersDaoService.read(email, password)
        override suspend fun readAll(): List<User> = usersDaoService.readAll()
        override suspend fun update(id: UUID, model: User) = usersDaoService.update(id, model)
        override suspend fun updateAll(modelMap: Map<UUID, User>) = usersDaoService.updateAll(modelMap)
        override suspend fun delete(id: UUID) = usersDaoService.delete(id)
        override suspend fun deleteAll() = usersDaoService.deleteAll()
        }
