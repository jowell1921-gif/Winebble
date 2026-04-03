package com.example.winebble.Views

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.core.view.WindowInsetsControllerCompat
import com.example.winebble.Constants
import com.example.winebble.SearchCard
import com.example.winebble.R
import com.example.winebble.components.ItemCard
import com.example.winebble.components.SortDialog
import com.example.winebble.getAllSearch
import com.example.winebble.ui.theme.WinebbleTheme
import kotlinx.coroutines.launch

/**
 * Project: Winebble
 * From: com.example.winebble.Views
 * Created by: Joel Arturo Osorio
 * On: 16/03/2026 at 19: 38
 * All rights reserved 2026.
 */
@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    WinebbleTheme {
        Search(
            favorites = emptyList(), // CAMBIO: EL PREVIEW USA UNA LISTA VACIA DE FAVORITOS PARA MANTENER LA FIRMA NUEVA.
            onToggleFavorite = {} // CAMBIO: EL PREVIEW PASA UNA ACCION VACIA PORQUE NO NECESITA GUARDAR FAVORITOS.
        )
    }
}

@Composable
fun Search(
    favorites: List<SearchCard>, // CAMBIO: RECIBE LOS FAVORITOS ACTUALES DESDE EL PADRE.
    onToggleFavorite: (SearchCard) -> Unit // CAMBIO: RECIBE LA ACCION PARA ANADIR O QUITAR FAVORITOS.
) {
    val context = LocalContext.current
    val view = LocalView.current
    val window = (view.context as? Activity)?.window

    var searchText by remember { mutableStateOf("") }
    var firstFilterText by remember { mutableStateOf("Ubicacion") }
    var secondFilterText by remember { mutableStateOf("Filtro") }
    var showSortDialog by remember { mutableStateOf(false) }
    var sortText by remember { mutableStateOf("Ordenar por:") }
    val searchItems = getAllSearch()
    val snackbarHostState = remember { SnackbarHostState() } // CAMBIO: CREA EL ESTADO DEL SNACKBAR PARA MOSTRAR MENSAJES AL GUARDAR FAVORITOS.
    val coroutineScope = rememberCoroutineScope() // CAMBIO: PERMITE LANZAR EL SNACKBAR DESDE EL CLICK DEL CORAZON.

    SideEffect {
        window?.statusBarColor = Color.White.toArgb()
        window?.let { WindowInsetsControllerCompat(it, view).isAppearanceLightStatusBars = true } // Fuerza iconos oscuros en la status bar.
    }

    val sortOptions = listOf(
        "Precio: menor a mayor",
        "Precio: mayor a menor",
        "Nombre: A-Z",
        "Nombre: Z-A"
    )

    Scaffold(
        containerColor = Color.White,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) } // CAMBIO: MONTA EL SNACKBAR EN LA PANTALLA SEARCH.
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.common_padding_default)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    //SEARCH
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        placeholder = { Text("Buscar") },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.icon_search),
                                contentDescription = "Search"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally) // Separamos los chips y mantenemos el grupo centrado.
                    ) {
                        //UBICATION
                        FilterChip(
                            selected = false,
                            onClick = {
                                val mapsIntent = Intent(
                                    Intent.ACTION_VIEW,
                                    Constants.URL_GOOGLE.toUri()
                                )
                                context.startActivity(mapsIntent)
                            },
                            label = { Text(firstFilterText) },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.icon_map),
                                    contentDescription = "Ubicacion",
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            shape = RoundedCornerShape(50)
                        )

                        //FILTER
                        FilterChip(
                            selected = false,
                            onClick = { },
                            label = { Text(secondFilterText) },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.icon_filter),
                                    contentDescription = "Filtro",
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            shape = RoundedCornerShape(50)
                        )

                        //SORT
                        FilterChip(
                            selected = sortText != "Ordenar por:",
                            onClick = { showSortDialog = true },
                            label = { Text(sortText) },
                            shape = RoundedCornerShape(50)
                        )
                    }
                }
            }

            items(searchItems) { search ->
                val isFavorite = favorites.any { favorite -> favorite.name == search.name } // CAMBIO: COMPRUEBA SI LA CARD YA ESTA GUARDADA EN FAVORITOS.
                ItemCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    imgUrl = search.imgUrl,
                    mainText = search.name,
                    secondaryText = search.description,
                    numberPhone = search.numberPhone,
                    direction = search.direction,
                    isFavorite = isFavorite, // CAMBIO: INFORMA A LA CARD DEL ESTADO VISUAL DEL CORAZON.
                    onFavoriteClick = {
                        val wasFavorite = isFavorite // CAMBIO: GUARDA EL ESTADO ANTERIOR PARA SABER QUE MENSAJE MOSTRAR.
                        onToggleFavorite(search) // CAMBIO: AL PULSAR EL CORAZON, ANADE O QUITA ESA SEARCHCARD.
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar( // CAMBIO: MUESTRA CONFIRMACION INMEDIATA DESPUES DE TOCAR FAVORITO.
                                if (wasFavorite)
                                    context.getString(R.string.remove_favorites)
                                else context.getString(
                                    R.string.add_favorites
                                )
                            )
                        }
                    }
                )
            }
        }

        //SHOW SORT DIALOG
        if (showSortDialog) {
            SortDialog(
                sortOptions = sortOptions,
                initialSelection = sortText,
                selectedOption = sortText,
                onOptionSelected = { option ->
                    sortText = option
                    showSortDialog = false
                },
                onDismiss = {
                    sortText = "Ordenar por:"
                    showSortDialog = false
                }
            )
        }
    }
}
