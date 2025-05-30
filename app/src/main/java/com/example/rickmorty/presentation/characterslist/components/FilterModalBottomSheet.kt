package com.example.rickmorty.presentation.characterslist.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.rickmorty.R
import com.example.rickmorty.domain.enums.CharacterFilters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterModalBottomSheet(
  onFilterDismiss: () -> Unit,
  statusOptions: List<CharacterFilters.Status?>,
  selectedStatus: CharacterFilters.Status?,
  onStatusSelected: (CharacterFilters.Status?) -> Unit,
  speciesOptions: List<CharacterFilters.Species?>,
  selectedSpecies: CharacterFilters.Species?,
  onSpeciesSelected: (CharacterFilters.Species?) -> Unit,
  genderOptions: List<CharacterFilters.Gender?>,
  selectedGender: CharacterFilters.Gender?,
  onGenderSelected: (CharacterFilters.Gender?) -> Unit,
  state: SheetState,
  onApply: () -> Unit
) {
  ModalBottomSheet(onDismissRequest = onFilterDismiss, sheetState = state) {
    Column(modifier = Modifier.padding(24.dp)) {
      Text(
        stringResource(R.string.filter_characters),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
      )
      Spacer(modifier = Modifier.height(16.dp))
      Text(stringResource(R.string.status), style = MaterialTheme.typography.labelMedium)
      Spacer(modifier = Modifier.height(8.dp))
      DropdownSelectorEnum(
        options = statusOptions,
        selected = selectedStatus,
        onSelected = onStatusSelected,
      )
      Spacer(modifier = Modifier.height(16.dp))
      Text(stringResource(R.string.species), style = MaterialTheme.typography.labelMedium)
      Spacer(modifier = Modifier.height(8.dp))
      DropdownSelectorEnum(
        options = speciesOptions,
        selected = selectedSpecies,
        onSelected = onSpeciesSelected,
      )
      Spacer(modifier = Modifier.height(16.dp))
      Text(stringResource(R.string.gender), style = MaterialTheme.typography.labelMedium)
      Spacer(modifier = Modifier.height(8.dp))
      DropdownSelectorEnum(
        options = genderOptions,
        selected = selectedGender,
        onSelected = onGenderSelected,
      )
      Spacer(modifier = Modifier.height(16.dp))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        TextButton(onClick = onFilterDismiss) { Text(stringResource(R.string.cancel)) }
        Spacer(modifier = Modifier.width(8.dp))
        TextButton(onClick = onApply) { Text(stringResource(R.string.apply)) }
      }
    }
  }
}