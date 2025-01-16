package com.example.datasource.local.entity

import kotlinx.datetime.LocalDate
import java.util.UUID


data class SearchEntity(
    val id: UUID? = null,
    val userId: UUID,
    val query: String,
    val createdAt: LocalDate
)