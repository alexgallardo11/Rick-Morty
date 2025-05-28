package com.example.rickmorty.presentation.characterslist

import com.example.rickmorty.domain.model.CharacterList
import com.example.rickmorty.domain.model.Character

data class CharactersUiState(
    val characters: CharacterList? = null,
    val characterDetail: Character? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)