package com.example.domain.model

import kotlinx.datetime.LocalDate
import java.util.UUID

data class Article(
    val id: UUID? = null,
    val title: String,
    val body: String,
    val authorId: UUID,
    val createdAt: LocalDate,
    val updatedAt: LocalDate
)
