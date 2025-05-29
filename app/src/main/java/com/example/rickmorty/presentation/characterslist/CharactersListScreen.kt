package com.example.rickmorty.presentation.characterslist

import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.example.rickmorty.R
import com.example.rickmorty.domain.model.Character

@Composable
fun CharactersListScreen(
    modifier: Modifier = Modifier,
    onCharacterClick: (Character) -> Unit,
    onFilterClick: () -> Unit
) {
    val viewModel: CharactersViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val pagingItems = viewModel.charactersPagingFlow.collectAsLazyPagingItems()

    Box(modifier = modifier.fillMaxSize()) {
        Log.d("CharactersListScreen", "UIState: $uiState, PagingItemsCount: ${pagingItems.itemCount}, LoadState: ${pagingItems.loadState.refresh}")
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        when {
            (pagingItems.loadState.refresh is LoadState.Loading && pagingItems.itemCount == 0) ||
            (pagingItems.loadState.refresh is LoadState.Error && pagingItems.itemCount == 0 && !pagingItems.loadState.refresh.endOfPaginationReached) -> {
                Log.d("CharactersListScreen", "Estado: Loading inicial o error transitorio, itemCount=0")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            pagingItems.loadState.refresh is LoadState.Error && pagingItems.itemCount == 0 -> {
                val error = (pagingItems.loadState.refresh as LoadState.Error).error
                val isNetworkError = error is java.io.IOException || error.message?.contains("Unable to resolve host") == true
                Log.d("CharactersListScreen", "Estado: Error final, isNetworkError=$isNetworkError, itemCount=${pagingItems.itemCount}, errorMsg=${error.message}")
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isNetworkError) "Sin conexión y sin datos locales." else "Error al cargar personajes",
                        color = Color.Red
                    )
                }
            }
            pagingItems.itemCount > 0 -> {
                Log.d("CharactersListScreen", "Estado: Mostrando lista, itemCount=${pagingItems.itemCount}")
                Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
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

                    if (uiState.error != null) {
                        Log.d("CharactersListScreen", "UI Error: ${uiState.error}")
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = uiState.error ?: "", color = Color.Red)
                        }
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        items(pagingItems.itemCount) { index ->
                            val character = pagingItems[index]
                            if (character != null) {
                                Log.d("CharactersListScreen", "Pintando personaje: ${character.name}")
                                CharacterCard(character = character, onClick = { onCharacterClick(character) })
                            }
                        }
                        when (pagingItems.loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    Log.d("CharactersListScreen", "Estado: Append Loading")
                                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                                    }
                                }
                            }
                            is LoadState.Error -> {
                                item {
                                    Log.d("CharactersListScreen", "Estado: Append Error")
                                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                        Text("Error al cargar más personajes", color = Color.Red)
                                    }
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }
            pagingItems.loadState.refresh is LoadState.NotLoading && pagingItems.itemCount == 0 -> {
                Log.d("CharactersListScreen", "Estado: NotLoading, lista vacía")
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
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
