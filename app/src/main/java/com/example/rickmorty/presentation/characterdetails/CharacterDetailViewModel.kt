package com.example.rickmorty.presentation.characterdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickmorty.domain.usecase.GetCharacterDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val getCharacterDetailUseCase: GetCharacterDetailUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<CharacterDetailUiState>(CharacterDetailUiState.Loading)
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    fun loadCharacter(id: Int) {
        _uiState.value = CharacterDetailUiState.Loading
        viewModelScope.launch {
            try {
                val character = getCharacterDetailUseCase(id)
                if (character != null) {
                    _uiState.value = CharacterDetailUiState.Success(character)
                } else {
                    _uiState.value = CharacterDetailUiState.Error("Sin conexión y sin datos locales para este personaje.")
                }
            } catch (e: Exception) {
                _uiState.value = CharacterDetailUiState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }
}
