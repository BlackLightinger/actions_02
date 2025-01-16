package com.example.domain.model

import kotlinx.datetime.LocalDate
import java.util.UUID

data class Comment(
    val id: UUID? = null,
    val textId: UUID,
    val userId: UUID,
    val body: String,
    val createdAt: LocalDate
)
