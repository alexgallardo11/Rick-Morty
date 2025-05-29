package com.example.rickmorty.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.rickmorty.domain.usecase.GetCharactersPagedUseCase
import com.example.rickmorty.presentation.characterslist.CharactersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersPagedUseCase: GetCharactersPagedUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CharactersUiState())
    val uiState: StateFlow<CharactersUiState> = _uiState.asStateFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    val charactersPagingFlow = uiState
        .flatMapLatest { state ->
            getCharactersPagedUseCase(
                name = state.searchQuery.takeIf { it.isNotBlank() }
            )
        }
        .cachedIn(viewModelScope)

    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }
}
