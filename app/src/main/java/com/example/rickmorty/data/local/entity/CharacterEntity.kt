package com.example.rickmorty.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Embedded

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    @Embedded(prefix = "origin_") val origin: OriginEntity,
    @Embedded(prefix = "location_") val location: LocationEntity,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
)
