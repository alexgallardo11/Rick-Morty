package com.example.rickmorty.presentation.characterdetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rickmorty.domain.model.Character

@Composable
fun CharacterMainInfo(character: Character, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = character.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "${character.status} - ${character.species}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Género: ${character.gender}", style = MaterialTheme.typography.bodyMedium)
    }
}
