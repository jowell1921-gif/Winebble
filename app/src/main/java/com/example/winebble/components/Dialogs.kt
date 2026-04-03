package com.example.winebble.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.winebble.ui.theme.WinebbleTheme
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


/**
 * Project: Winebble
 * From: com.example.winebble.components
 * Created by: Joel Arturo Osorio
 * On: 18/03/2026 at 14: 45
 * All rights reserved 2026.
 */

@Preview
@Composable
fun DialogPreview() {
    WinebbleTheme {
        SortDialog(
            sortOptions = listOf("Precio: menor a mayor", "Precio: mayor a menor", "Nombre: A-Z"),
            selectedOption = "Precio: menor a mayor",
            initialSelection = "",
            onOptionSelected = {},
            onDismiss = {}
        )
    }
}
@Composable
fun SortDialog(sortOptions: List<String>,
               selectedOption: String,
               initialSelection: String,
               onOptionSelected: (String) -> Unit,
               onDismiss: () -> Unit,) {
    var tempSelection by remember { mutableStateOf(initialSelection) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Ordenar por") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                sortOptions.forEach { option ->
                    TextButton(
                        onClick = {
                            tempSelection = option
                            onOptionSelected(option)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row( modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(option)

                            if (option == tempSelection) {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Seleccionado",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()}) {
                Text("Cancelar")
            }
        }
    )
}


