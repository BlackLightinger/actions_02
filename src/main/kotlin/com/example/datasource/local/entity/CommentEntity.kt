package com.example.datasource.local.entity

import kotlinx.datetime.LocalDate
import java.util.UUID


data class CommentEntity(
    val id: UUID? = null,
    val textId: UUID,
    val userId: UUID,
    val body: String,
    val createdAt: LocalDate
)