package com.example.rickmorty.presentation.characterdetails

import com.example.rickmorty.domain.model.Character


sealed class CharacterDetailUiState {
    data object Loading : CharacterDetailUiState()
    data class Success(val character: Character) : CharacterDetailUiState()
    data class Error(val message: String) : CharacterDetailUiState()
}