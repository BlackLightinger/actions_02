package com.example.datasource.local.table

import com.example.datasource.local.entity.UserEntity
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.kotlin.datetime.date


object UsersTable : UUIDTable() {
    val username = varchar("username", 128)
    val email = varchar("email", 128)
    val passwordHash = varchar("password_hash", 256)
    val role = varchar("role", 50)
    val registrationDate = date("registration_date")
    val lastLogin = date("last_login")

    fun updateStatement(st: UpdateBuilder<Int>, entity: UserEntity) {
        st[username] = entity.username
        st[email] = entity.email
        st[passwordHash] = entity.passwordHash
        st[role] = entity.role
        st[registrationDate] = entity.registrationDate
        st[lastLogin] = entity.lastLogin
    }
}
