package com.example.domain.model

import kotlinx.datetime.LocalDate
import java.util.UUID

data class User(
    val id: UUID? = null,
    val username: String,
    val email: String,
    val passwordHash: String,
    val role: String,
    val registrationDate: LocalDate,
    val lastLogin: LocalDate
)