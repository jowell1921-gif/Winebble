package com.example.winebble.Views

import androidx.activity.ComponentActivity // Necesario porque enableEdgeToEdge solo existe para ComponentActivity.
import androidx.activity.SystemBarStyle // API actual para estilizar la status bar.
import androidx.activity.enableEdgeToEdge // Permite configurar las barras del sistema desde la activity.
import androidx.compose.foundation.background // Aplica el fondo blanco a toda la pantalla.
import androidx.compose.foundation.layout.Arrangement // Centra el contenido verticalmente.
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column // Contenedor vertical de la pantalla vacia.
import androidx.compose.foundation.layout.fillMaxSize // Hace que el contenedor ocupe toda la pantalla.
import androidx.compose.foundation.layout.size // Define el tamano de la animacion y del icono.
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp // Unidad para definir tamanos en Compose.
import com.example.winebble.R
import com.example.winebble.SearchCard
import com.example.winebble.components.ItemCard
import com.example.winebble.ui.theme.WinebbleTheme
import com.airbnb.lottie.compose.LottieAnimation // Renderiza la animacion Lottie en pantalla.
import com.airbnb.lottie.compose.LottieCompositionSpec // Indica desde donde se carga la animacion.
import com.airbnb.lottie.compose.LottieConstants // Aporta constantes como el bucle infinito.
import com.airbnb.lottie.compose.animateLottieCompositionAsState // Anima el progreso de la composicion.
import com.airbnb.lottie.compose.rememberLottieComposition // Carga y memoriza el json de Lottie.

/**
 * Project: Winebble
 * From: com.example.winebble.Views
 * Created by: Joel Arturo Osorio
 * On: 07/03/2026 at 14: 20
 * All rights reserved 2026.
 */
@Preview(showBackground = true)
@Composable
fun FavoritePreview() {
    WinebbleTheme {
        FavoritesScreen(
            favorites = emptyList(), // CAMBIO: EL PREVIEW USA FAVORITOS VACIOS PARA MOSTRAR EL ESTADO INICIAL.
            onToggleFavorite = {} // CAMBIO: EL PREVIEW PASA UNA ACCION VACIA PARA LA FIRMA NUEVA.
        )
    }
}
@Composable
fun FavoritesScreen(
    favorites: List<SearchCard>, // CAMBIO: RECIBE DESDE EL PADRE LA LISTA REAL DE FAVORITOS GUARDADOS.
    onToggleFavorite: (SearchCard) -> Unit // CAMBIO: RECIBE LA ACCION PARA QUITAR O VOLVER A MARCAR FAVORITOS.
) {
    val view = LocalView.current
    val activity = view.context as? ComponentActivity // Obtiene la activity compatible con enableEdgeToEdge.
    val emptyComposition =
        rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_search)) // CAMBIO: MANTIENE LA ANIMACION DEL ESTADO VACIO CUANDO NO HAY FAVORITOS.
    val emptyProgress = animateLottieCompositionAsState(
        composition = emptyComposition.value, // CAMBIO: USA LA COMPOSICION DEL ESTADO VACIO.
        iterations = LottieConstants.IterateForever // Reproduce la animacion en bucle.
    )
    val favoriteHeaderComposition =
        rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.wine_favorite1)) // CAMBIO: CARGA LA NUEVA LOTTIE FIJA PARA LA PARTE SUPERIOR DE FAVORITOS.
    val favoriteHeaderProgress = animateLottieCompositionAsState(
        composition = favoriteHeaderComposition.value, // CAMBIO: USA LA COMPOSICION DE LA CABECERA DE FAVORITOS.
        iterations = LottieConstants.IterateForever // Reproduce la animacion en bucle.
    )

    SideEffect {
        activity?.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.White.toArgb(), Color.White.toArgb()) // Deja la status bar blanca con iconos oscuros.
        )
    }
    if (favorites.isEmpty()) { // CAMBIO: MUESTRA EL ESTADO VACIO SOLO CUANDO TODAVIA NO HAY FAVORITOS.
        Column(
            modifier = Modifier
                .fillMaxSize() // Hace que el estado vacio use toda la pantalla.
                .background(Color.White), // Mantiene el mismo fondo blanco que la status bar.
            horizontalAlignment = Alignment.CenterHorizontally, // Centra el contenido en horizontal.
            verticalArrangement = Arrangement.Center // Centra el contenido en vertical.
        ) {
            LottieAnimation(
                composition = emptyComposition.value, // CAMBIO: PINTA LA ANIMACION DE EMPTY SOLO CUANDO LA LISTA ESTA VACIA.
                progress = { emptyProgress.value }, // CAMBIO: USA EL PROGRESO DE LA ANIMACION VACIA.
                modifier = Modifier.size(280.dp) // Da un tamano visible a la animacion principal.
            )

            Icon(
                painter = painterResource(R.drawable.icon_favorite),
                contentDescription = null,
                tint = Color.Unspecified, // Respeta el color original del recurso vectorial.
                modifier = Modifier.size(48.dp) // Reduce el icono a un tamano secundario.
            )

            Text(
                text = stringResource(R.string.empty_favorite),
                color = Color.Black
            )
        }
    } else { // CAMBIO: SI YA HAY FAVORITOS, MUESTRA UNA CABECERA LOTTIE FIJA Y LA LISTA SE DESPLAZA POR DEBAJO.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White) // CAMBIO: USA UN CONTENEDOR PADRE PARA SUPERPONER LA LOTTIE FIJA Y LA LISTA SCROLLEABLE.
        ) {
            LottieAnimation(
                composition = favoriteHeaderComposition.value, // CAMBIO: DIBUJA LA LOTTIE SUPERIOR SOLO CUANDO HAY FAVORITOS.
                progress = { favoriteHeaderProgress.value }, // CAMBIO: ANIMA EN BUCLE LA CABECERA DE FAVORITOS.
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
                    .size(160.dp) // CAMBIO: FIJA LA LOTTIE ARRIBA Y CENTRADA SIN QUE DESAPAREZCA AL HACER SCROLL.
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 152.dp), // CAMBIO: EMPUJA LA LISTA HACIA ABAJO PARA QUE HAGA SCROLL POR DEBAJO DE LA LOTTIE FIJA.
                horizontalAlignment = Alignment.CenterHorizontally, // CAMBIO: MANTIENE LAS CARDS CENTRADAS COMO GRUPO DENTRO DE FAVORITOS.
                verticalArrangement = Arrangement.spacedBy(16.dp) // CAMBIO: CONSERVA LA DISTRIBUCION VERTICAL DEL LISTADO.
            ) {
                items(favorites) { favorite ->
                    ItemCard(
                        modifier = Modifier
                            .fillMaxWidth() // CAMBIO: RECUPERA EL ANCHO COMPLETO DE BORDE A BORDE COMO EN LA CARD ORIGINAL.
                            .padding(horizontal = 16.dp), // CAMBIO: MANTIENE SOLO EL MARGEN LATERAL NORMAL DE LA PANTALLA.
                        imgUrl = favorite.imgUrl,
                        mainText = favorite.name,
                        secondaryText = favorite.description,
                        numberPhone = favorite.numberPhone,
                        direction = favorite.direction,
                        isFavorite = true, // CAMBIO: EN FAVORITES EL CORAZON SALE MARCADO PORQUE EL ITEM YA ESTA GUARDADO.
                        onFavoriteClick = { onToggleFavorite(favorite) }, // CAMBIO: PERMITE QUITAR EL ELEMENTO DIRECTAMENTE DESDE FAVORITES.
                        compactMode = true // CAMBIO: ACTIVA LA VERSION COMPACTA DE LA CARD SOLO DENTRO DE FAVORITOS.
                    )
                }
            }
        }
    }
}
