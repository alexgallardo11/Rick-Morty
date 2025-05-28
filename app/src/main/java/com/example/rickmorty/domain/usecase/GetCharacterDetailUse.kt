package com.example.rickmorty.domain.usecase

import com.example.rickmorty.data.repository.CharacterRepository
import com.example.rickmorty.domain.model.Character
import javax.inject.Inject


interface GetCharacterDetailUseCase {
    suspend operator fun invoke(id: Int): Character
}


class GetCharacterDetailUseCaseImpl @Inject constructor(
    private val repository: CharacterRepository
) : GetCharacterDetailUseCase {
    override suspend fun invoke(id: Int): Character = repository.getCharacterDetail(id)
}
