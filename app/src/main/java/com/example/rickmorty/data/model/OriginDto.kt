package com.example.rickmorty.data.model

import kotlinx.serialization.Serializable

@Serializable
data class OriginDto(
    val name: String,
    val url: String
)
