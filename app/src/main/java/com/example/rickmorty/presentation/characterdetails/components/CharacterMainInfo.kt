package com.example.rickmorty.presentation.characterdetails.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.rickmorty.R
import com.example.rickmorty.domain.model.Character

@Composable
fun CharacterMainInfo(character: Character) {

  Column(
    modifier = Modifier
      .padding(16.dp).alpha(0.9f)
  ) {
    Text(
      text = character.name,
      style = MaterialTheme.typography.headlineSmall,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(8.dp))

    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      val statusColor = when (character.status.lowercase()) {
        "alive" -> MaterialTheme.colorScheme.primary
        "dead" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.tertiary
      }

      Icon(
        imageVector = Icons.Default.Person,
        contentDescription = null,
        tint = statusColor
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = stringResource(R.string.status_species_dash, character.status, character.species),
        style = MaterialTheme.typography.bodyLarge,
        color = statusColor
      )
    }

    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = stringResource(R.string.gender_prefix, character.gender),
      style = MaterialTheme.typography.bodyMedium,
      )
  }

}
