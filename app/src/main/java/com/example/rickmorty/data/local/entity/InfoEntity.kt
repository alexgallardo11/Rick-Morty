package com.example.rickmorty.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "info")
data class InfoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)
