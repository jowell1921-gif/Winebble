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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.winebble.Views.FavoritesScreen
import com.example.winebble.Views.ProfileScreen
import com.example.winebble.Views.SearchScreen
import com.example.winebble.ui.theme.Playfair
import com.example.winebble.ui.theme.WinebbleTheme

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
    // --- CONTEXTO Y RECURSOS ---
    // Obtenemos el contexto para acceder a recursos como colores
    val context = LocalContext.current
    val colorBar = context.getColor(R.color.gold)

    // --- ESTADO DE LA APLICACIÓN ---
    // selectedItem: Controla qué item de la barra inferior está seleccionado
    var selectedItem by remember { mutableStateOf(1) }


    // --- ESTRUCTURA PRINCIPAL (SCAFFOLD) ---
    // Scaffold proporciona la estructura básica de Material Design
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(context.getColor(R.color.principal)),
        // --- BARRA INFERIOR ---
        // Se pasa el estado y el callback para manejar selecciones
        bottomBar = {
            MyBottomBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
        // --- CONTENIDO PRINCIPAL ---
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            when(selectedItem) {
                0 -> SearchScreen()
                1 -> MainScreen(
                    modifier = Modifier
                        .fillMaxSize(),
                    onSelectedItem  = { wine -> }
                )
                2 -> FavoritesScreen()
                3 -> ProfileScreen()
            }
        }
    }
}


// --- SEALD CLASS PARA LAS PANTALLAS DE LA BARRA INFERIOR ---
sealed class BottomBarScreen(val route: String, val icon: Int, val title: String) {
    object Search : BottomBarScreen("Search", R.drawable.icon_search, "Búsqueda")
    object Home : BottomBarScreen("Home", R.drawable.baseline_home_24, "Inicio")
    object Favorite : BottomBarScreen("Favorite", R.drawable.icon_favorite, "Favoritos")
    object Profile : BottomBarScreen("Search", R.drawable.icon_person, "Perfil")
}


// --- BARRA DE NAVEGACIÓN  ---
@Composable
fun MyBottomBar(
    // --- PARÁMETROS DE ENTRADA (STATE HOISTING) ---
    selectedItem: Int?,
    onItemSelected: (Int) -> Unit
) {
    // --- CONFIGURACIÓN DE ITEMS ---
    // Lista que define los elementos de la barra de navegación
    val items = listOf(
        BottomNavItem(R.drawable.icon_search, "Búsqueda"),
        BottomNavItem(R.drawable.baseline_home_24, "Inicio"),
        BottomNavItem(R.drawable.icon_favorite, "Favoritos"),
        BottomNavItem(R.drawable.icon_person, "Perfil")
    )

    val context = LocalContext.current
    // NavigationBar es el contenedor de la barra inferior
    NavigationBar(
        containerColor = Color(context.getColor(R.color.second))
    ) {
        // --- GENERACIÓN DINÁMICA DE ITEMS ---
        // Iteramos sobre la lista para crear cada NavigationBarItem
        items.forEachIndexed { index, item ->
            // --- ITEM DE NAVEGACIÓN INDIVIDUAL ---
            NavigationBarItem(
                // ¿Este item está seleccionado?
                selected = selectedItem == index,
                // Acción al hacer clic: notificamos al padre el índice seleccionado
                onClick = { onItemSelected(index) },

                // --- ICONO DEL ITEM ---
                icon = {
                    Icon(
                        painter = painterResource(item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(30.dp),
                        // Color: dorado si está seleccionado, blanco si no
                        tint = if (selectedItem == index) Color.Black else Color.White
                    )
                },
                // --- TEXTO DEL ITEM (OPCIONAL) ---
                label = {
                    Text(
                        text = item.title,
                        fontFamily = Playfair,
                        color = if (selectedItem == index) Color(0xFFFFD700) else Color.White
                    )
                }
            )
        }
    }
}

//MODELO DE DATOS
data class BottomNavItem(@DrawableRes val icon: Int, val title: String)


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WinebbleTheme {
        ContentMain()
    }
}