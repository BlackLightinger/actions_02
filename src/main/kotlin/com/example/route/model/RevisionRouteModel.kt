package com.example.route.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class RevisionRouteModel(
    @Contextual val id: UUID? = null,
    @SerialName("text_id") @Contextual val textId: UUID,
    @SerialName("author_id") @Contextual val authorId: UUID,
    val createdAt: String
)