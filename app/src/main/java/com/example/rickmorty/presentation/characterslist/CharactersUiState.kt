package com.example.rickmorty.presentation.characterslist

data class CharactersUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)