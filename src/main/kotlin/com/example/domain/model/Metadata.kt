package com.example.domain.model
import java.util.UUID

data class Metadata(
    val id: UUID? = null,
    val textId: UUID,
    val key: String,
    val value: String
)