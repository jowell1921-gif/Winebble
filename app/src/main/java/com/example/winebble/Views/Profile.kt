package com.example.winebble.Views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.winebble.R
import com.example.winebble.getProfileItems
import com.example.winebble.ui.theme.WinebbleTheme

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    WinebbleTheme {
        Profile(onLogout = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile(onLogout: () -> Unit) {
    val composition = rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.profile_child)) // Carga la animacion del avatar desde raw.
    val progress = animateLottieCompositionAsState( // Reproduce la animacion en bucle para que el avatar tenga movimiento continuo.
        composition = composition.value,
        iterations = LottieConstants.IterateForever
    )
    val items = getProfileItems()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White, // Unifica el fondo de la pantalla con la cabecera.
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White, // Deja la cabecera completamente blanca.
                shadowElevation = 8.dp // Añade una sombra suave para separar visualmente la cabecera del contenido.
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp), // Da aire a la cabecera para que la Lottie pueda crecer sin romper el layout.
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.user_name),
                        color = Color.Black, // Mejora el contraste del nombre sobre fondo blanco.
                        fontSize = 28.sp, // Mantiene a Joel Osorio como el foco principal de la cabecera.
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f) // Reserva el espacio flexible para que el nombre no sea empujado por el bloque derecho.
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.width(112.dp), // Limita el ancho del bloque derecho para que Mi perfil siempre quepa.
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        LottieAnimation(
                            composition = composition.value,
                            progress = { progress.value },
                            modifier = Modifier.size(72.dp) // Agranda la Lottie para acercarla visualmente al peso del nombre.
                        )

                        Text(
                            text = stringResource(R.string.main_profile),
                            color = colorResource(R.color.principal), // Usa el color principal como acento del bloque de perfil.
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            maxLines = 1 // Evita que Mi perfil se rompa en dos lineas dentro del bloque.
                        )

                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Mostrar opciones de perfil",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp) // Reduce el peso visual de la flecha para que no compita con la Lottie.
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(items) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (item.route.trim().equals("cerrar sesion", ignoreCase = true)) { // CAMBIO: COMPARA EL ROUTE REAL DEL ITEM DE LOGOUT SIN DEPENDER DE MAYUSCULAS.
                                    onLogout() // CAMBIO: EJECUTA EL CIERRE DE SESION CUANDO SE PULSA LA FILA CORRECTA.
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = item.title,
                            fontSize = 18.sp,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Ir",
                            tint = Color.Gray
                        )
                    }

                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))
                }
            }
        }
    }
}
