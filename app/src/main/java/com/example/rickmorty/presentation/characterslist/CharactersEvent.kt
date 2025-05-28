package com.example.rickmorty.presentation.characterslist

sealed class CharactersEvent {
    data class FetchCharacters(
        val page: Int = 1,
        val name: String? = null,
        val status: String? = null,
        val species: String? = null,
        val gender: String? = null
    ) : CharactersEvent()
    data class FetchCharacterDetail(val id: Int) : CharactersEvent()
}