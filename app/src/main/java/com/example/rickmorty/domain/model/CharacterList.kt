package com.example.rickmorty.domain.model
import kotlinx.serialization.Serializable

@Serializable
data class CharacterList(
    val info: Info,
    val results: List<Character>
)
