package com.example.rickmorty.presentation.characterdetails.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun CharacterImage(imageUrl: String, name: String, modifier: Modifier = Modifier) {
  Box(modifier = modifier) {
    AsyncImage(
      model = imageUrl,
      contentDescription = name,
      modifier = Modifier
        .clip(CircleShape)
        .border(
          width = 4.dp,
          color = MaterialTheme.colorScheme.primary,
          shape = CircleShape
        )
        .size(140.dp),
      contentScale = ContentScale.Crop
    )
  }
}
