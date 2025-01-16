package com.example.domain.model
import java.util.UUID

data class SearchResult(
    val id: UUID? = null,
    val searchId: UUID,
    val textId: UUID,
    val relevance: Double
)