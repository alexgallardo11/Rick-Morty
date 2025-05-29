package com.example.rickmorty.data.local

import androidx.room.TypeConverter

class RoomConverters {
    @TypeConverter
    fun fromStringList(list: List<String>?): String = list?.joinToString(",") ?: ""

    @TypeConverter
    fun toStringList(data: String?): List<String> = data?.split(",")?.filter { it.isNotEmpty() } ?: emptyList()
}
