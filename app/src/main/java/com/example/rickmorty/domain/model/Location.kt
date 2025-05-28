package com.example.rickmorty.domain.model
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val name: String,
    val url: String
)
