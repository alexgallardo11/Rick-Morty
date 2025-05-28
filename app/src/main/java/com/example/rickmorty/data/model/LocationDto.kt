package com.example.rickmorty.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    val name: String,
    val url: String
)
