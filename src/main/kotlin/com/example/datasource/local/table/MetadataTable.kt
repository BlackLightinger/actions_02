package com.example.datasource.local.table

import com.example.datasource.local.entity.MetadataEntity
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object MetadataTable : UUIDTable() {
    val textId = reference("text_id", ArticlesTable)
    val key = varchar("key", 256)
    val value = text("value")

    fun updateStatement(st: UpdateBuilder<Int>, entity: MetadataEntity) {
        st[textId] = entity.textId
        st[key] = entity.key
        st[value] = entity.value
    }
}
