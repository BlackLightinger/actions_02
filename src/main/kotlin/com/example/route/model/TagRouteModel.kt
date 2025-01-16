package com.example.route.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class TagRouteModel(
    @Contextual val id: UUID? = null,
    val name: String
)
