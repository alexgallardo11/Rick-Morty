package com.example.rickmorty.presentation.characterdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rickmorty.presentation.characterdetails.components.CharacterEpisodesInfo
import com.example.rickmorty.presentation.characterdetails.components.CharacterImage
import com.example.rickmorty.presentation.characterdetails.components.CharacterLocationInfo
import com.example.rickmorty.presentation.characterdetails.components.CharacterMainInfo
import com.example.rickmorty.presentation.components.ErrorMessage
import com.example.rickmorty.presentation.components.Loading

@OptIn(ExperimentalMaterial3Api::class)
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

  Box(
    modifier = modifier
      .fillMaxSize(),

    ) {
    when (uiState) {
      is CharacterDetailUiState.Loading -> {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          Loading()
        }
      }

      is CharacterDetailUiState.Error -> {
        val message = (uiState as CharacterDetailUiState.Error).message
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          contentAlignment = Alignment.Center
        ) {
          ErrorMessage(
            text = if (message.contains("Unable to resolve host", ignoreCase = true) ||
              message.contains("failed to connect", ignoreCase = true)
            ) {
              "Sin conexión y sin datos locales para este personaje."
            } else message
          )
        }
      }

      is CharacterDetailUiState.Success -> {
        val character = (uiState as CharacterDetailUiState.Success).character
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {

            CharacterImage(
              imageUrl = character.image,
              name = character.name,
            )

            CharacterMainInfo(
              character = character,
            )

          }
          Spacer(modifier = Modifier.height(8.dp))
          CharacterLocationInfo(
            character = character,
            modifier = Modifier.fillMaxWidth()
          )
          Spacer(modifier = Modifier.height(8.dp))
          CharacterEpisodesInfo(
            episodeCount = character.episode.size,
            modifier = Modifier.fillMaxWidth()
          )

          Spacer(modifier = Modifier.height(8.dp))

          Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(0.7f),
            colors = ButtonDefaults.buttonColors(
              containerColor = MaterialTheme.colorScheme.primary
            )
          ) {
            Text(
              text = "Volver a la lista",
              style = MaterialTheme.typography.labelLarge,
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }
  }

}
