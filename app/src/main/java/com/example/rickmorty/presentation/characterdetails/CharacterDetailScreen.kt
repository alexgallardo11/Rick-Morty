package com.example.rickmorty.presentation.characterdetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rickmorty.presentation.components.ErrorMessage
import com.example.rickmorty.presentation.components.Loading
import com.example.rickmorty.presentation.characterdetails.components.CharacterImage
import com.example.rickmorty.presentation.characterdetails.components.CharacterMainInfo
import com.example.rickmorty.presentation.characterdetails.components.CharacterLocationInfo
import com.example.rickmorty.presentation.characterdetails.components.CharacterEpisodesInfo

@Composable
fun CharacterDetailScreen(
    modifier: Modifier = Modifier,
    characterId: Int,
    onBack: () -> Unit = {}
) {

    val viewModel: CharacterDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(characterId) {
        viewModel.loadCharacter(characterId)
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            is CharacterDetailUiState.Loading -> Loading()
            is CharacterDetailUiState.Error -> {
                val message = (uiState as CharacterDetailUiState.Error).message
                ErrorMessage(
                    text = if (message.contains("Unable to resolve host", ignoreCase = true) || message.contains("failed to connect", ignoreCase = true)) {
                        "Sin conexión y sin datos locales para este personaje."
                    } else message
                )
            }
            is CharacterDetailUiState.Success -> {
                val character = (uiState as CharacterDetailUiState.Success).character
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CharacterImage(
                        imageUrl = character.image,
                        name = character.name,
                        modifier = Modifier.size(220.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CharacterMainInfo(character = character)
                    Spacer(modifier = Modifier.height(8.dp))
                    CharacterLocationInfo(character = character)
                    Spacer(modifier = Modifier.height(8.dp))
                    CharacterEpisodesInfo(episodeCount = character.episode.size)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onBack) {
                        Text("Volver")
                    }
                }
            }
        }
    }
}
