package com.example.route.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class SearchRouteModel(
    @Contextual val id: UUID? = null,
    @SerialName("user_id") @Contextual val userId: UUID,
    val query: String,
    val createdAt: String
)
