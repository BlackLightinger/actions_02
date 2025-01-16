package com.example.datasource.local.service

import com.example.data.datasource.local.service.IUsersDaoService
import com.example.datasource.local.dao.UsersDao
import com.example.datasource.local.mapper.toDomain
import com.example.datasource.local.mapper.toEntity
import com.example.domain.model.User
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

class UsersDaoService(
    private val usersDao: UsersDao
) : IUsersDaoService {

    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }

    override suspend fun insert(model: User): UUID = dbQuery { usersDao.insert(model.toEntity()) }

    override suspend fun insertAll(modelList: List<User>): List<UUID> =
        dbQuery { usersDao.batchInsert(modelList.map { it.toEntity() }) }

    override suspend fun read(id: UUID): User? = dbQuery { usersDao.read(id)?.toDomain() }

    override suspend fun read(email: String): User? = dbQuery { usersDao.read(email)?.toDomain() }

    override suspend fun read(email: String, password: String): User? =
        dbQuery { usersDao.read(email, password)?.toDomain() }

    override suspend fun readAll(): List<User> = dbQuery { usersDao.readAll().map { it.toDomain() } }

    override suspend fun update(id: UUID, model: User): Unit = dbQuery { usersDao.update(id, model.toEntity()) }

    override suspend fun updateAll(modelMap: Map<UUID, User>) =
        dbQuery { usersDao.updateAll(modelMap.mapValues { it.value.toEntity() }) }

    override suspend fun delete(id: UUID): Unit = dbQuery { usersDao.delete(id) }

    override suspend fun deleteAll(): Unit = dbQuery { usersDao.deleteAll() }
}
