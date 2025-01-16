package com.example.datasource.local.dao

import com.example.datasource.local.entity.UserEntity
import com.example.datasource.local.mapper.toUserEntity
import com.example.datasource.local.table.UsersTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.*

abstract class UsersDao {
    fun insert(userEntity: UserEntity): UUID = UsersTable.insert { insertStatement ->
        updateStatement(insertStatement, userEntity)
    }[UsersTable.id].value

    fun batchInsert(userEntitiesList: List<UserEntity>): List<UUID> =
        UsersTable.batchInsert(userEntitiesList) { userEntity ->
            UsersTable.updateStatement(this, userEntity)
        }.map { res -> res[UsersTable.id].value }

    fun read(id: UUID): UserEntity? =
        UsersTable.select { UsersTable.id eq id }.singleOrNull()?.toUserEntity()

    fun read(email: String): UserEntity? =
        UsersTable.select { UsersTable.email eq email }.singleOrNull()?.toUserEntity()

    fun read(email: String, password: String): UserEntity? =
        UsersTable.select { (UsersTable.email eq email) and (UsersTable.passwordHash eq password) }.singleOrNull()
            ?.toUserEntity()

    fun readAll(): List<UserEntity> = UsersTable.selectAll().map { it.toUserEntity() }

    fun update(id: UUID, userEntity: UserEntity) =
        UsersTable.update({ UsersTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, userEntity)
        }

    fun updateAll(userEntityMap: Map<UUID, UserEntity>) = userEntityMap.forEach { (id, userEntity) ->
        UsersTable.update({ UsersTable.id eq id }) { updateStatement ->
            updateStatement(updateStatement, userEntity)
        }
    }

    fun delete(id: UUID) = UsersTable.deleteWhere { UsersTable.id eq id }

    fun deleteAll() = UsersTable.deleteAll()
}
