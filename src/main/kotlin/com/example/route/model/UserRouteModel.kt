package com.example.route.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class UserRouteModel(
    @Contextual val id: UUID? = null,
    val username: String,
    val email: String,
    val passwordHash: String,
    val role: String,
    val registrationDate: String,
    val lastLogin: String
)
