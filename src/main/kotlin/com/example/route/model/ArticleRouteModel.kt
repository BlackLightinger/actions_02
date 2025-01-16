package com.example.route.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class ArticleRouteModel(
    @Contextual val id: UUID? = null,
    val title: String,
    val body: String,
    @SerialName("author_id") @Contextual val authorId: UUID,
    val createdAt: String,
    val updatedAt: String
)