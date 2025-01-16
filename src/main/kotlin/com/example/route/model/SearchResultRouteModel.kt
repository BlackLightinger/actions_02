package com.example.route.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class SearchResultRouteModel(
    @Contextual val id: UUID? = null,
    @SerialName("search_id") @Contextual val searchId: UUID,
    @SerialName("text_id") @Contextual val textId: UUID,
    val relevance: Double
)
