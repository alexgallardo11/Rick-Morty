package com.example.rickmorty.data.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.rickmorty.data.api.RickAndMortyApiService
import com.example.rickmorty.data.local.AppDatabase
import com.example.rickmorty.data.mapper.toDomain
import com.example.rickmorty.data.mapper.toEntity
import com.example.rickmorty.domain.model.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val apiService: RickAndMortyApiService,
    private val db: AppDatabase
) {
    suspend fun getCharacterDetail(id: Int): Character? {
        return try {
            val dto = apiService.getCharacterDetail(id)
            val entity = dto.toEntity()
            db.characterDao().insertAll(listOf(entity))
            entity.toDomain()
        } catch (e: IOException) {
            db.characterDao().getCharacterById(id)?.toDomain()
        } catch (e: Exception) {
            db.characterDao().getCharacterById(id)?.toDomain()
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getCharactersPagingFlow(
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ): Flow<PagingData<Character>> {
        Log.d("CharacterRepository", "getCharactersPagingFlow called with: name=$name, status=$status, species=$species, gender=$gender")
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                initialLoadSize = 20,
                prefetchDistance = 5
            ),
            remoteMediator = CharacterRemoteMediator(
                apiService = apiService,
                database = db,
                name = name,
                status = status,
                species = species,
                gender = gender
            ),
            pagingSourceFactory = {
                Log.d("CharacterRepository", "pagingSourceFactory invoked")
                db.characterDao().getCharactersPagedFiltered(name, status, species, gender)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }
}
