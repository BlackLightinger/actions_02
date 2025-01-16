package com.example.datasource.local.entity

import java.util.UUID


data class MetadataEntity(
    val id: UUID? = null,
    val textId: UUID,
    val key: String,
    val value: String
)