package com.example.datasource.local.entity

import kotlinx.datetime.LocalDate
import java.util.UUID


data class ArticleEntity(
    val id: UUID? = null,
    val title: String,
    val body: String,
    val authorId: UUID,
    val createdAt: LocalDate,
    val updatedAt: LocalDate
)