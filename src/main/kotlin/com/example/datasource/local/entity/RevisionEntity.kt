package com.example.datasource.local.entity

import kotlinx.datetime.LocalDate
import java.util.UUID


data class RevisionEntity(
    val id: UUID? = null,
    val textId: UUID,
    val authorId: UUID,
    val createdAt: LocalDate
)