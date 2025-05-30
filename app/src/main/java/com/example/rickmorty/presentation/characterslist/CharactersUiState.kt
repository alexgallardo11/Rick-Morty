package com.example.rickmorty.presentation.characterslist

data class CharactersUiState(
    val searchQuery: String = "",
    val status: String? = null,
    val species: String? = null,
    val gender: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)