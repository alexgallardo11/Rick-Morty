package com.example.rickmorty.data.repository

import com.example.rickmorty.data.api.RickAndMortyApiService
import com.example.rickmorty.data.mapper.toDomain
import com.example.rickmorty.domain.model.Character
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val apiService: RickAndMortyApiService
) {

    suspend fun getCharacterDetail(id: Int): Character {
        return apiService.getCharacterDetail(id).toDomain()
    }

    fun getCharactersPagingFlow(
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                CharacterPagingSource(
                    apiService = apiService,
                    name = name,
                    status = status,
                    species = species,
                    gender = gender
                )
            }
        ).flow
    }
}
