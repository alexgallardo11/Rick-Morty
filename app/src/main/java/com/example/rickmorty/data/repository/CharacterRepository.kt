package com.example.rickmorty.data.repository

import com.example.rickmorty.data.RickAndMortyApiService
import com.example.rickmorty.data.mapper.toDomain
import com.example.rickmorty.domain.model.Character
import com.example.rickmorty.domain.model.CharacterList
import javax.inject.Inject

class CharacterRepository @Inject constructor(
    private val apiService: RickAndMortyApiService
) {
    suspend fun getCharactersPaged(
        page: Int = 1,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ): CharacterList {
        return apiService.getCharacters(page, name, status, species, gender).toDomain()
    }

    suspend fun getCharacterDetail(id: Int): Character {
        return apiService.getCharacterDetail(id).toDomain()
    }
}
