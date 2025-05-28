package com.example.rickmorty.presentation.characterslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.example.rickmorty.domain.model.Character
import com.example.rickmorty.presentation.CharactersViewModel

@Composable
fun CharactersListScreen(
  modifier: Modifier = Modifier,
  onCharacterClick: (Character) -> Unit = {},
  onFilterClick: () -> Unit = {}
) {
  val viewModel: CharactersViewModel = hiltViewModel()
  val uiState by viewModel.uiState.collectAsState()
  var searchQuery by remember { mutableStateOf("") }
  val charactersList = uiState.characters
  val characters = charactersList?.results ?: emptyList()

  LaunchedEffect(Unit) {
    viewModel.onEvent(CharactersEvent.FetchCharacters())
  }



  Column(modifier = modifier.fillMaxSize()) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = {
          searchQuery = it
          viewModel.onEvent(
            CharactersEvent.FetchCharacters(
              page = 1,
              name = searchQuery
            )
          )
        },
        modifier = Modifier.weight(1f),
        label = { Text("Buscar personaje") },
        singleLine = true
      )
      IconButton(onClick = { onFilterClick() }) {
        Icon(
          imageVector = Icons.Default.FilterList,
          contentDescription = "Filtrar"
        )
      }
    }

    if (uiState.isLoading && (characters.isEmpty())) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
      }
    } else if (uiState.error != null) {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = uiState.error ?: "", color = Color.Red)
      }
    } else {
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
      ) {
        itemsIndexed(characters) { index, character ->
          CharacterCard(character = character, onClick = { onCharacterClick(character) })
          if (
            index == characters.lastIndex &&
            !uiState.isLoading &&
            charactersList?.info?.next != null
          ) {
            val nextPage = charactersList.info.next
              .substringAfter("page=")
              .substringBefore("&")
              .toIntOrNull()
            if (nextPage != null) {
              viewModel.onEvent(
                CharactersEvent.FetchCharacters(
                  page = nextPage,
                  name = searchQuery
                )
              )
            }
          }
        }
        if (uiState.isLoading && characters.isNotEmpty()) {
          item {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
              CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
          }
        }
      }
    }
  }
}

@Composable
fun CharacterCard(character: Character, onClick: () -> Unit) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
      .clickable { onClick() },
    elevation = CardDefaults.cardElevation(2.dp)
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(8.dp)
    ) {
      AsyncImage(
        model = character.image,
        contentDescription = character.name,
        modifier = Modifier.size(60.dp)
      )
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(text = character.name, style = MaterialTheme.typography.titleMedium)
        Text(
          text = "${character.status} • ${character.species}",
          style = MaterialTheme.typography.bodyMedium
        )
      }
    }
  }
}
