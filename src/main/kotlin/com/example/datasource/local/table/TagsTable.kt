package com.example.datasource.local.table

import com.example.datasource.local.entity.TagEntity
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object TagsTable : UUIDTable() {
    val name = varchar("name", 128)

    fun updateStatement(st: UpdateBuilder<Int>, entity: TagEntity) {
        st[name] = entity.name
    }
}
