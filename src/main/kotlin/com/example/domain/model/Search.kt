package com.example.domain.model

import kotlinx.datetime.LocalDate
import java.util.UUID

data class Search(
    val id: UUID? = null,
    val userId: UUID,
    val query: String,
    val createdAt: LocalDate
)