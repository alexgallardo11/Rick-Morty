package com.example.rickmorty.domain.usecase

import androidx.paging.PagingData
import com.example.rickmorty.domain.model.Character
import kotlinx.coroutines.flow.Flow
import com.example.rickmorty.data.repository.CharacterRepository
import javax.inject.Inject

interface GetCharactersPagedUseCase {
    operator fun invoke(
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ): Flow<PagingData<Character>>
}

class GetCharactersPagedUseCaseImpl @Inject constructor(
    private val repository: CharacterRepository
) : GetCharactersPagedUseCase {
    override fun invoke(
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): Flow<PagingData<Character>> =
        repository.getCharactersPagingFlow(name, status, species, gender)
}
