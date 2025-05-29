package com.example.rickmorty.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "origins")
data class OriginEntity(
    @PrimaryKey val name: String,
    val url: String
)
