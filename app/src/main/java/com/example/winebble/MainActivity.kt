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
import com.example.winebble.data.model.CheckoutProduct
import com.example.winebble.data.model.SearchCard
import com.example.winebble.ui.screens.FavoritesScreen
import com.example.winebble.ui.screens.MainScreen
import com.example.winebble.ui.screens.ProductCheckoutDialog
import com.example.winebble.ui.screens.Profile
import com.example.winebble.ui.screens.Search
import com.example.winebble.ui.screens.UserLogin
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

    // <-- Estado principal / Main state -->
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedItem by remember { mutableStateOf(1) }
    var logoutMessage by remember { mutableStateOf<String?>(null) }
    var selectedCheckoutProduct by remember { mutableStateOf<CheckoutProduct?>(null) }

    // <-- Favoritos compartidos / Shared favorites -->
    val favoriteSearch = remember { mutableStateListOf<SearchCard>() }
    val onToggleFavorite: (SearchCard) -> Unit = { card ->
        val existingCard = favoriteSearch.firstOrNull { it.name == card.name }
        if (existingCard != null) {
            favoriteSearch.remove(existingCard)
        } else {
            favoriteSearch.add(card)
        }
    }

    // <-- Mensajes globales / Global messages -->
    LaunchedEffect(authViewModel.user, authViewModel.successMessage, logoutMessage) {
        when {
            logoutMessage != null -> {
                selectedItem = 1
                snackbarHostState.showSnackbar(logoutMessage!!)
                logoutMessage = null
            }

            authViewModel.user != null && authViewModel.successMessage != null -> {
                selectedItem = 1
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
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        bottomBar = {
            if (authViewModel.user != null) {
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
                    .padding(paddingValues),
                authViewModel = authViewModel
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // <-- Contenido principal / Main content -->
                when (selectedItem) {
                    0 -> Search(
                        favorites = favoriteSearch,
                        onToggleFavorite = onToggleFavorite
                    )

                    1 -> MainScreen(
                        modifier = Modifier.fillMaxSize(),
                        onSelectedItem = { wine ->
                            selectedCheckoutProduct = CheckoutProduct(
                                id = "wine-${wine.name}",
                                name = wine.name,
                                description = wine.description,
                                origin = wine.origin,
                                imageUrl = wine.imgUrl,
                                price = wine.price,
                                type = "Vino"
                            )
                        },
                        onSelectedItemTwo = { licor ->
                            selectedCheckoutProduct = CheckoutProduct(
                                id = "licor-${licor.name}",
                                name = licor.name,
                                description = licor.description,
                                origin = licor.origin,
                                imageUrl = licor.imgUrl,
                                price = licor.price,
                                type = "Licor"
                            )
                        }
                    )

                    2 -> FavoritesScreen(
                        favorites = favoriteSearch,
                        onToggleFavorite = onToggleFavorite
                    )

                    3 -> Profile(
                        user = authViewModel.user,
                        onLogout = {
                            selectedItem = 1
                            logoutMessage = "Has cerrado sesión correctamente"
                            authViewModel.logout()
                        }
                    )
                }

                // <-- Checkout flotante / Floating checkout -->
                selectedCheckoutProduct?.let { product ->
                    ProductCheckoutDialog(
                        product = product,
                        onDismiss = { selectedCheckoutProduct = null }
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
    object Profile : BottomBarScreen("Profile", R.drawable.icon_person, "Perfil")
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
        MainScreen(
            modifier = Modifier.fillMaxSize(),
            onSelectedItem = { },
            onSelectedItemTwo = { }
        )
    }
}
