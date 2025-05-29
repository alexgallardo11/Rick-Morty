package com.example.rickmorty.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rickmorty.data.local.entity.CharacterEntity

@Dao
interface CharacterDao {


  @Query("SELECT * FROM characters WHERE (:name IS NULL OR name LIKE '%' || :name || '%') AND (:status IS NULL OR status = :status) AND (:species IS NULL OR species = :species) AND (:gender IS NULL OR gender = :gender) ORDER BY id ASC")
  fun getCharactersPagedFiltered(
    name: String?,
    status: String?,
    species: String?,
    gender: String?
  ): PagingSource<Int, CharacterEntity>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(characters: List<CharacterEntity>)

  @Query("DELETE FROM characters")
  suspend fun clearAll()

  @Query("SELECT * FROM characters WHERE id = :id LIMIT 1")
  suspend fun getCharacterById(id: Int): CharacterEntity?
}
