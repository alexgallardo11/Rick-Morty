package com.example.rickmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickmorty.data.local.entity.OriginEntity

@Dao
interface OriginDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(origin: OriginEntity)

    @Query("SELECT * FROM origins WHERE name = :name LIMIT 1")
    suspend fun getOriginByName(name: String): OriginEntity?
}
