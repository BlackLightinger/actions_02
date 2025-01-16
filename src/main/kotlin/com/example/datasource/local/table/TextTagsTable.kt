package com.example.datasource.local.table

import com.example.datasource.local.entity.TextTagEntity
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.statements.UpdateBuilder

object TextTagsTable : UUIDTable() {
    val textId = reference("text_id", ArticlesTable)
    val tagId = reference("tag_id", TagsTable)

    fun updateStatement(st: UpdateBuilder<Int>, entity: TextTagEntity) {
        st[textId] = entity.textId
        st[tagId] = entity.tagId
    }
}
