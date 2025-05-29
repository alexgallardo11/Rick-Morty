package com.example.rickmorty

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.rickmorty.presentation.AppNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
  var showSearch by remember { mutableStateOf(false) }
  var showFilter by remember { mutableStateOf(false) }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = { Text("Rick & Morty") },
        actions = {
          IconButton(onClick = { showSearch = !showSearch }) {
            Icon(Icons.Default.Search, contentDescription = "Buscar")
          }
          IconButton(onClick = { showFilter = true }) {
            Icon(Icons.Default.FilterList, contentDescription = "Filtrar")
          }
        }
      )
    }
  ) { padding ->
    AppNavHost(
      contentPadding = padding,
      showSearch = showSearch,
      showFilter = showFilter,
      onSearchDismiss = { showSearch = false },
      onFilterDismiss = { showFilter = false }
    )
  }
}
