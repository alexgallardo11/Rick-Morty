package com.example.rickmorty.presentation.characterslist

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.rickmorty.domain.enums.CharacterFilters
import com.example.rickmorty.domain.model.Character


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersListScreen(
    modifier: Modifier = Modifier,
    onCharacterClick: (Character) -> Unit,
    showSearch: Boolean = false,
    showFilter: Boolean = false,
    onSearchDismiss: () -> Unit = {},
    onFilterDismiss: () -> Unit = {}
) {
    val viewModel: CharactersViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val pagingItems = viewModel.charactersPagingFlow.collectAsLazyPagingItems()
    val uiStatus = uiState.status
    val uiSpecies = uiState.species
    val uiGender = uiState.gender
    var selectedStatus by remember(showFilter, uiStatus) {
        mutableStateOf(CharacterFilters.Status.entries.find { it.label == uiStatus })
    }
    var selectedSpecies by remember(showFilter, uiSpecies) {
        mutableStateOf(CharacterFilters.Species.entries.find { it.label == uiSpecies })
    }
    var selectedGender by remember(showFilter, uiGender) {
        mutableStateOf(CharacterFilters.Gender.entries.find { it.label == uiGender })
    }
    val statusOptions = listOf<CharacterFilters.Status?>(null) + CharacterFilters.Status.entries
    val genderOptions = listOf<CharacterFilters.Gender?>(null) + CharacterFilters.Gender.entries
    val speciesOptions = listOf<CharacterFilters.Species?>(null) + CharacterFilters.Species.entries

    Box(modifier = modifier.fillMaxSize()) {
        Log.d("CharactersListScreen", "UIState: $uiState, PagingItemsCount: ${pagingItems.itemCount}, LoadState: ${pagingItems.loadState.refresh}")
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            AnimatedVisibility(
                visible = showSearch,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    label = { Text("Buscar personaje") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    trailingIcon = {
                        IconButton(onClick = { onSearchDismiss() }) {
                            Icon(Icons.Default.FilterList, contentDescription = "Cerrar búsqueda")
                        }
                    }
                )
            }

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
                pagingItems.loadState.refresh is LoadState.NotLoading && pagingItems.itemCount == 0 -> {
                    Log.d("CharactersListScreen", "Estado: NotLoading, lista vacía")
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }

        if (showFilter) {
            ModalBottomSheet(
                onDismissRequest = onFilterDismiss
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Filtrar personajes", style = MaterialTheme.typography.titleMedium)
                    Text("Estado", style = MaterialTheme.typography.labelMedium)
                    DropdownSelectorEnum(
                        options = statusOptions,
                        selected = selectedStatus,
                        onSelected = { selectedStatus = it },
                        label = "Estado"
                    )
                    Text("Especie", style = MaterialTheme.typography.labelMedium)
                    DropdownSelectorEnum(
                        options = speciesOptions,
                        selected = selectedSpecies,
                        onSelected = { selectedSpecies = it },
                        label = "Especie"
                    )
                    Text("Género", style = MaterialTheme.typography.labelMedium)
                    DropdownSelectorEnum(
                        options = genderOptions,
                        selected = selectedGender,
                        onSelected = { selectedGender = it },
                        label = "Género"
                    )
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = onFilterDismiss) { Text("Cancelar") }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = {
                            viewModel.updateFilters(
                                status = selectedStatus?.label,
                                species = selectedSpecies?.label,
                                gender = selectedGender?.label
                            )
                            onFilterDismiss()
                        }) { Text("Aplicar") }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownSelectorEnum(
    options: List<T?>,
    selected: T?,
    onSelected: (T?) -> Unit,
    label: String
) where T : Enum<T> {
    var expanded by remember { mutableStateOf(false) }
    val displayLabel: (T?) -> String = { opt ->
        when (opt) {
            null -> "(Cualquiera)"
            is CharacterFilters.Status -> opt.label
            is CharacterFilters.Gender -> opt.label
            is CharacterFilters.Species -> opt.label
            else -> opt.toString()
        }
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = displayLabel(selected),
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                .clickable { expanded = true }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(displayLabel(option)) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
