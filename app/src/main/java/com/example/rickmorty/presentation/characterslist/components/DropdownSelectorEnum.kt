package com.example.rickmorty.presentation.characterslist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.rickmorty.domain.enums.CharacterFilters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropdownSelectorEnum(
    options: List<T?>,
    selected: T?,
    onSelected: (T?) -> Unit,
) where T : Enum<T> {
    var expanded by remember { mutableStateOf(false) }
    val displayLabel: (T?) -> String = { opt ->
        when (opt) {
            null -> "(Cualquiera)"
            is CharacterFilters.Status -> opt.label
            is CharacterFilters.Gender -> opt.label
            is CharacterFilters.Species -> opt.label
            else -> opt.toString()
        }
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = displayLabel(selected),
            onValueChange = {},
            readOnly = true,
            trailingIcon = { 
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Expandir menú",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 4.dp)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .alpha(0.95f)
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
                .clickable { expanded = true },
            shape = RoundedCornerShape(12.dp),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .clip(RoundedCornerShape(8.dp))
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { 
                        Text(
                            text = displayLabel(option),
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (option == selected) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurface
                        ) 
                    },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    },
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .background(
                            color = if (option == selected)
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                            else
                                MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
        }
    }
}