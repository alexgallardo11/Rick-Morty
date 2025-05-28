package com.example.rickmorty.domain.model
import kotlinx.serialization.Serializable

@Serializable
data class Origin(
    val name: String,
    val url: String
)
