package com.example.rickmorty.domain.usecase

import com.example.rickmorty.data.repository.CharacterRepository
import com.example.rickmorty.domain.model.CharacterList
import javax.inject.Inject


interface GetCharactersPagedUseCase {
    suspend operator fun invoke(
        page: Int = 1,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ): CharacterList
}


class GetCharactersPagedUseCaseImpl @Inject constructor(
    private val repository: CharacterRepository
) : GetCharactersPagedUseCase {
    override suspend fun invoke(
        page: Int,
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): CharacterList = repository.getCharactersPaged(page, name, status, species, gender)
}
