package com.example.rickmorty.presentation.characterslist.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

@Composable
fun ColumnScope.SearchBar(
  showSearch: Boolean,
  searchQuery: String,
  onSearchQueryChange: (String) -> Unit,
  onSearchDismiss: () -> Unit
) {
  AnimatedVisibility(
    visible = showSearch,
    enter = fadeIn() + expandVertically(),
    exit = fadeOut() + shrinkVertically()
  ) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(showSearch) {
      if (showSearch) {
        focusRequester.requestFocus()
      }
    }
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .clip(RoundedCornerShape(12.dp))
        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
        .height(56.dp)
        .alpha(0.95f),
      contentAlignment = Alignment.CenterStart
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = "Icono de búsqueda",
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(start = 12.dp, end = 8.dp)
        )
        BasicTextField(
          value = searchQuery,
          onValueChange = onSearchQueryChange,
          singleLine = true,
          textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
          ),
          cursorBrush = Brush.verticalGradient(
            colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.primary)
          ),
          modifier = Modifier
            .weight(1f)
            .padding(vertical = 8.dp)
            .focusRequester(focusRequester),
          decorationBox = { innerTextField ->
            if (searchQuery.isEmpty()) {
              Text(
                text = "Buscar personaje",
                style = MaterialTheme.typography.bodyMedium.copy(
                  color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
              )
            }
            innerTextField()
          }
        )
        IconButton(onClick = {
          onSearchQueryChange("")
          onSearchDismiss()
        }) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Cerrar búsqueda",
            tint = MaterialTheme.colorScheme.primary
          )
        }
      }
    }
  }
}