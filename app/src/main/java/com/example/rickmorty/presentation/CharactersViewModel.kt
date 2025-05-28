package com.example.rickmorty.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmorty.domain.usecase.GetCharactersPagedUseCase
import com.example.rickmorty.domain.usecase.GetCharacterDetailUseCase
import com.example.rickmorty.presentation.characterslist.CharactersEvent
import com.example.rickmorty.presentation.characterslist.CharactersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersPagedUseCase: GetCharactersPagedUseCase,
    private val getCharacterDetailUseCase: GetCharacterDetailUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()

    fun onEvent(event: CharactersEvent) {
        when (event) {
            is CharactersEvent.FetchCharacters -> fetchCharacters(
                event.page, event.name, event.status, event.species, event.gender
            )
            is CharactersEvent.FetchCharacterDetail -> fetchCharacterDetail(event.id)
        }
    }

    private fun fetchCharacters(
        page: Int = 1,
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = getCharactersPagedUseCase(page, name, status, species, gender)
                _uiState.value = _uiState.value.copy(
                    characters = result,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun fetchCharacterDetail(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = getCharacterDetailUseCase(id)
                _uiState.value = _uiState.value.copy(
                    characterDetail = result,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}
