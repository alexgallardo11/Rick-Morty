package com.example.rickmorty.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rickmorty.data.local.dao.CharacterDao
import com.example.rickmorty.data.local.dao.LocationDao
import com.example.rickmorty.data.local.dao.OriginDao
import com.example.rickmorty.data.local.entity.CharacterEntity
import com.example.rickmorty.data.local.entity.InfoEntity
import com.example.rickmorty.data.local.entity.LocationEntity
import com.example.rickmorty.data.local.entity.OriginEntity

@Database(
    entities = [CharacterEntity::class, LocationEntity::class, OriginEntity::class, InfoEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
    abstract fun locationDao(): LocationDao
    abstract fun originDao(): OriginDao
}
