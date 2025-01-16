package com.example.route.model

import kotlinx.serialization.Serializable


@Serializable
data class UserLoginRouteModel(
    val email: String,
    val password: String
)