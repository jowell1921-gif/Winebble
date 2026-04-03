package com.example.winebble

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.winebble.Views.FavoritesScreen
import com.example.winebble.Views.Profile
import com.example.winebble.Views.Search
import com.example.winebble.Views.UserLogin
import com.example.winebble.ui.theme.WinebbleTheme
import com.example.winebble.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WinebbleTheme {
                ContentMain()
            }
        }
    }
}

@Composable
fun ContentMain() {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val snackbarHostState = remember { SnackbarHostState() } // CAMBIO: CENTRALIZA LOS MENSAJES DE LOGIN, REGISTRO Y LOGOUT.

    var selectedItem by remember { mutableStateOf(1) } // CAMBIO: CONTROLA LA PESTAÑA SELECCIONADA DE LA BARRA INFERIOR.
    var logoutMessage by remember { mutableStateOf<String?>(null) } // CAMBIO: GUARDA EL MENSAJE TEMPORAL DE CIERRE DE SESION.

    val favoriteSearch = remember { mutableStateListOf<SearchCard>() } // CAMBIO: MANTIENE LOS FAVORITOS DE SEARCH COMPARTIDOS EN TODA LA APP.
    val onToggleFavorite: (SearchCard) -> Unit = { card -> // CAMBIO: CENTRALIZA EN EL PADRE EL ALTA Y BAJA DE FAVORITOS.
        val existingCard = favoriteSearch.firstOrNull { it.name == card.name }
        if (existingCard != null) {
            favoriteSearch.remove(existingCard)
        } else {
            favoriteSearch.add(card)
        }
    }

    LaunchedEffect(authViewModel.user, authViewModel.successMessage, logoutMessage) { // CAMBIO: MUESTRA SNACKBARS CUANDO CAMBIAN LOS EVENTOS DE AUTH.
        when {
            logoutMessage != null -> {
                snackbarHostState.showSnackbar(logoutMessage!!)
                logoutMessage = null
            }

            authViewModel.user != null && authViewModel.successMessage != null -> {
                snackbarHostState.showSnackbar(authViewModel.successMessage!!)
                authViewModel.clearSuccessMessage()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = if (authViewModel.user == null || selectedItem == 0) {
            Color.White
        } else {
            Color(context.getColor(R.color.principal))
        }, // CAMBIO: MANTIENE FONDO BLANCO EN LOGIN Y SEARCH, Y EL PRINCIPAL EN EL RESTO.
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) // CAMBIO: HOST GLOBAL PARA LOS MENSAJES DE AUTENTICACION.
        },
        bottomBar = {
            if (authViewModel.user != null) { // CAMBIO: SOLO MUESTRA LA BARRA INFERIOR CUANDO EL USUARIO YA ESTA DENTRO DE LA APP.
                MyBottomBar(
                    selectedItem = selectedItem,
                    onItemSelected = { selectedItem = it }
                )
            }
        }
    ) { paddingValues ->
        if (authViewModel.user == null) {
            UserLogin(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues), // CAMBIO: MANTIENE EL LOGIN DENTRO DEL MISMO SCAFFOLD PARA COMPARTIR SNACKBARS.
                authViewModel = authViewModel
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (selectedItem) {
                    0 -> Search(
                        favorites = favoriteSearch, // CAMBIO: PASA LA LISTA DE FAVORITOS A SEARCH.
                        onToggleFavorite = onToggleFavorite // CAMBIO: PASA LA ACCION DE FAVORITOS A SEARCH.
                    )

                    1 -> MainScreen(
                        modifier = Modifier.fillMaxSize(),
                        onSelectedItem = { },
                        onSelectedItemTwo = { }
                    )

                    2 -> FavoritesScreen(
                        favorites = favoriteSearch, // CAMBIO: PASA LOS FAVORITOS A FAVORITESSCREEN.
                        onToggleFavorite = onToggleFavorite // CAMBIO: PERMITE QUITAR FAVORITOS TAMBIEN DESDE FAVORITESSCREEN.
                    )

                    3 -> Profile(
                        onLogout = {
                            logoutMessage = "Has cerrado sesión correctamente" // CAMBIO: PREPARA EL MENSAJE DE LOGOUT ANTES DE CERRAR SESION.
                            authViewModel.logout() // CAMBIO: CIERRA SESION Y DEVUELVE AL LOGIN.
                        }
                    )
                }
            }
        }
    }
}

sealed class BottomBarScreen(val route: String, val icon: Int, val title: String) {
    object Search : BottomBarScreen("Search", R.drawable.icon_search, "Búsqueda")
    object Home : BottomBarScreen("Home", R.drawable.baseline_home_24, "Inicio")
    object Favorite : BottomBarScreen("Favorite", R.drawable.icon_favorite, "Favoritos")
    object Profile : BottomBarScreen("Search", R.drawable.icon_person, "Perfil")
}

@Composable
fun MyBottomBar(
    selectedItem: Int?,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf(
        BottomNavItem(R.drawable.icon_search, "Búsqueda"),
        BottomNavItem(R.drawable.baseline_home_24, "Inicio"),
        BottomNavItem(R.drawable.icon_favorite, "Favoritos"),
        BottomNavItem(R.drawable.icon_person, "Perfil")
    )

    val context = LocalContext.current

    NavigationBar(
        containerColor = Color(context.getColor(R.color.second))
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(30.dp),
                        tint = if (selectedItem == index) Color.Black else Color.White
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (selectedItem == index) Color(0xFFFFD700) else Color.White
                    )
                }
            )
        }
    }
}

data class BottomNavItem(@DrawableRes val icon: Int, val title: String)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WinebbleTheme {
        ContentMain()
    }
}
