package com.example.route.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class TextTagRouteModel(
    @SerialName("text_id") @Contextual val textId: UUID,
    @SerialName("tag_id") @Contextual val tagId: UUID
)