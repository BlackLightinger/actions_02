package com.example.route.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class CommentRouteModel(
    @Contextual val id: UUID? = null,
    @SerialName("text_id") @Contextual val textId: UUID,
    @SerialName("user_id") @Contextual val userId: UUID,
    val body: String,
    val createdAt: String
)
