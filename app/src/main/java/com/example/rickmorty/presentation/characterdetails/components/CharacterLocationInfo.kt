package com.example.rickmorty.presentation.characterdetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.rickmorty.domain.model.Character

@Composable
fun CharacterLocationInfo(character: Character, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "Origen: ${character.origin.name}")
        Text(text = "Ubicación: ${character.location.name}")
    }
}
