package com.example.rickmorty.presentation.characterdetails.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
fun CharacterImage(imageUrl: String, name: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = imageUrl,
        contentDescription = name,
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}
