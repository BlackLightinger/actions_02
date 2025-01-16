package com.example.domain.model

import kotlinx.datetime.LocalDate
import java.util.UUID

data class Revision(
    val id: UUID? = null,
    val textId: UUID,
    val authorId: UUID,
    val createdAt: LocalDate
)
