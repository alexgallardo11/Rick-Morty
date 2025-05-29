package com.example.rickmorty.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickmorty.data.local.entity.LocationEntity

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(location: LocationEntity)

    @Query("SELECT * FROM locations WHERE name = :name LIMIT 1")
    suspend fun getLocationByName(name: String): LocationEntity?
}
