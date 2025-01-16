package com.example.datasource.local.entity

import java.util.UUID


data class SearchResultEntity(
    val id: UUID? = null,
    val searchId: UUID,
    val textId: UUID,
    val relevance: Double
)