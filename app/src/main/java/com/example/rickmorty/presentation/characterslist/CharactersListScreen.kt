package com.example.rickmorty.presentation.characterslist

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.example.rickmorty.R
import com.example.rickmorty.domain.enums.CharacterFilters
import com.example.rickmorty.domain.model.Character
import com.example.rickmorty.presentation.components.ErrorMessage
import com.example.rickmorty.presentation.components.Loading
import com.example.rickmorty.presentation.characterslist.components.SearchBar
import com.example.rickmorty.presentation.characterslist.components.FilterModalBottomSheet

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
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            SearchBar(
                showSearch = showSearch,
                searchQuery = uiState.searchQuery,
                onSearchQueryChange = viewModel::updateSearchQuery,
                onSearchDismiss = onSearchDismiss
            )

            CharactersListContent(
                pagingItems = pagingItems,
                onCharacterClick = onCharacterClick,
                error = uiState.error
            )
        }

        if (showFilter) {
            FilterModalBottomSheet(
                onFilterDismiss = onFilterDismiss,
                statusOptions = statusOptions,
                selectedStatus = selectedStatus,
                onStatusSelected = { selectedStatus = it },
                speciesOptions = speciesOptions,
                selectedSpecies = selectedSpecies,
                onSpeciesSelected = { selectedSpecies = it },
                genderOptions = genderOptions,
                selectedGender = selectedGender,
                onGenderSelected = { selectedGender = it },
                onApply = {
                    viewModel.updateFilters(
                        status = selectedStatus?.label,
                        species = selectedSpecies?.label,
                        gender = selectedGender?.label
                    )
                    onFilterDismiss()
                }
            )
        }
    }
}



@Composable
private fun CharactersListContent(
  pagingItems: LazyPagingItems<Character>,
  onCharacterClick: (Character) -> Unit,
  error: String?
) {
    when {
        (pagingItems.loadState.refresh is LoadState.Loading && pagingItems.itemCount == 0) ||
                (pagingItems.loadState.refresh is LoadState.Error && pagingItems.itemCount == 0 && !pagingItems.loadState.refresh.endOfPaginationReached) -> {
            Loading()
        }
        pagingItems.loadState.refresh is LoadState.Error && pagingItems.itemCount == 0 -> {
            val errorMsg = (pagingItems.loadState.refresh as? LoadState.Error)?.error?.message ?: "Error al cargar personajes"
            ErrorMessage(text = errorMsg)
        }
        pagingItems.itemCount > 0 -> {
            if (error != null) ErrorMessage(text = error)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(pagingItems.itemCount) { index ->
                    val character = pagingItems[index]
                    if (character != null) {
                        CharacterCard(character = character, onClick = { onCharacterClick(character) })
                    }
                }
                when (pagingItems.loadState.append) {
                    is LoadState.Loading -> {
                        item { Loading() }
                    }
                    is LoadState.Error -> {
                        item { ErrorMessage(text = "Error al cargar personajes") }
                    }
                    else -> {}
                }
            }
        }
        pagingItems.loadState.refresh is LoadState.NotLoading && pagingItems.itemCount == 0 -> {
            Loading()
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


