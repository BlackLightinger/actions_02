package com.example.route.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class MetadataRouteModel(
    @Contextual val id: UUID? = null,
    @SerialName("text_id") @Contextual val textId: UUID,
    val key: String,
    val value: String
)
