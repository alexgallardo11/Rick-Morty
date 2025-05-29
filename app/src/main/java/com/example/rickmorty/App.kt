package com.example.rickmorty

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rickmorty.presentation.AppNavHost
import com.example.rickmorty.presentation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
  var showSearch by remember { mutableStateOf(false) }
  var showFilter by remember { mutableStateOf(false) }
  var currentScreen by remember { mutableStateOf("characters_list") }
  var onNavigateBack: () -> Unit by remember { mutableStateOf({}) }


  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          if (!currentScreen.startsWith(Routes.CHARACTER_DETAIL.substringBefore("/{"))) {
            Image(
              painter = painterResource(id = R.drawable.rnm),
              contentDescription = "Logo",
              modifier = Modifier.size(140.dp)
            )
          }
        },
        navigationIcon = {
          if (currentScreen.startsWith(Routes.CHARACTER_DETAIL.substringBefore("/{"))) {
            IconButton(onClick = { onNavigateBack() }) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Atrás"
              )
            }
          }
        },
        actions = {
          AnimatedVisibility(
            visible = !currentScreen.startsWith(Routes.CHARACTER_DETAIL.substringBefore("/{")),
            enter = fadeIn(tween(220)),
            exit = fadeOut(tween(220))
          ) {
            Row {
              IconButton(onClick = { showSearch = !showSearch }) {
                Icon(Icons.Default.Search, contentDescription = "Buscar")
              }
              IconButton(onClick = { showFilter = true }) {
                Icon(Icons.Default.FilterList, contentDescription = "Filtrar")
              }
            }
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
      onFilterDismiss = { showFilter = false },
      onScreenChanged = { currentScreen = it },
      onNavigateBack = { onNavigateBack = it }
    )
  }
}
