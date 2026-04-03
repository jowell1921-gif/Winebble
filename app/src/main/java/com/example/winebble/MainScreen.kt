package com.example.winebble

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.winebble.components.ItemList
import com.example.winebble.ui.theme.WinebbleTheme


/**
 * Project: Winebble
 * From: com.example.winebble
 * Created by: Joel Arturo Osorio
 * On: 03/03/2026 at 13: 55
 * All rights reserved 2026.
 */

@Preview(showBackground = true)
@Composable
fun MainPreview()  {
    WinebbleTheme {
        MainScreen(Modifier.padding(16.dp),
            onSelectedItem = {}, onSelectedItemTwo = {})
    }
}

@Composable
fun MainScreen ( modifier: Modifier,
                 onSelectedItem: (WineData) -> Unit,
                 onSelectedItemTwo: (LicorData) -> Unit) {
    val wines = getAllWine()
    val licors = getAllLicor()

    val buttons = listOf(
        stringResource((R.string.licor_list)),
        stringResource((R.string.wines_list))
    )

    var selectedIndex by remember { mutableIntStateOf(1) }

    Scaffold( // Separa la cabecera del contenido para dejar el selector arriba y la lista debajo sin usar weight.
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.principal)),
        containerColor = colorResource(R.color.principal), // Mantiene el fondo principal en toda la pantalla.
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.common_padding_default)), // Da margen al selector superior.
                contentAlignment = Alignment.Center
            ) {
                SingleChoiceSegmentedButtonRow(
                    Modifier.padding(vertical = dimensionResource(R.dimen.common_padding_min)) // Conserva el espaciado vertical del selector.
                ) {
                    buttons.forEachIndexed { index, label ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = buttons.size
                            ),
                            onClick = { selectedIndex = index }, // Cambia entre Destilados y Vinos.
                            selected = index == selectedIndex,
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = colorResource(R.color.gold),
                                inactiveContainerColor = Color.White
                            ),
                            label = { Text(label) }
                        )
                    }
                }
            }
        }
) { innerPadding ->
    LazyColumn(
        modifier = Modifier.fillMaxSize(), // Mantiene una unica lista ocupando toda la zona de contenido.
        contentPadding = innerPadding // Coloca la lista por debajo del topBar del Scaffold.
    ) {
        when (selectedIndex) {
            0 -> items(licors) { licor -> // Cuando se pulsa Destilados, pinta los elementos de getAllLicor().
                ItemList(
                    modifier = Modifier.clickable { onSelectedItemTwo(licor) }, // Llama al callback correcto de destilados.
                    mainText = licor.name,
                    secondaryText = licor.description,
                    imgUrl = licor.imgUrl,
                    icon = Icons.Default.AddShoppingCart,
                    overlineText = licor.origin,
                    showDivider = true,
                    price = licor.price
                )
            }
            1 -> items(wines) { wine -> // Cuando se pulsa Vinos, pinta los elementos de getAllWine().
                ItemList(
                    modifier = Modifier.clickable { onSelectedItem(wine) }, // Mantiene el callback original de vinos.
                    mainText = wine.name,
                    secondaryText = wine.description,
                    imgUrl = wine.imgUrl,
                    icon = Icons.Default.AddShoppingCart,
                    overlineText = wine.origin,
                    showDivider = true,
                    price = wine.price
                )
            }
        }
    }
}
}
