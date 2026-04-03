package com.example.winebble.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.winebble.R
import com.example.winebble.ui.theme.WinebbleTheme



/**
 * Project: Winebble
 * From: com.example.winebble.components
 * Created by: Joel Arturo Osorio
 * On: 16/03/2026 at 19: 57
 * All rights reserved 2026.
 */

@Preview(showBackground = true)
@Composable
fun PreviewItemCard() {
    WinebbleTheme {
        ItemCard(
            imgUrl = "https://s2.elespanol.com/2019/05/18/cocinillas/vinos/vinos_399470851_123155360_864x486.jpg",
            mainText = "Vinoteca El Sumiller",
            secondaryText = "Esta vinoteca de Aluche ofrece una seleccion cuidada y una experiencia cercana para descubrir nuevas botellas.",
            numberPhone = 910419582,
            direction = "Calle de Camarena, 88",
            isFavorite = false, // CAMBIO: EL PREVIEW DEFINE EXPLICITAMENTE EL ESTADO DEL CORAZON.
            onFavoriteClick = {}, // CAMBIO: EL PREVIEW USA UNA ACCION VACIA PARA LA PULSACION DEL FAVORITO.
            compactMode = false // CAMBIO: EL PREVIEW SIGUE MOSTRANDO LA VERSION NORMAL DE LA CARD.
        )
    }
}

@Composable
fun ItemCard(
    modifier: Modifier = Modifier,
    imgUrl: String,
    mainText: String,
    secondaryText: String,
    numberPhone: Int,
    direction: String,
    isFavorite: Boolean, // CAMBIO: RECIBE SI LA CARD YA ESTA EN FAVORITOS.
    onFavoriteClick: () -> Unit, // CAMBIO: RECIBE LA ACCION QUE SE EJECUTA AL PULSAR EL CORAZON.
    compactMode: Boolean = false // CAMBIO: PERMITE REUTILIZAR LA MISMA CARD EN UN TAMANO MAS PEQUENO PARA FAVORITOS.
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.item_card_corner_radius)),
        colors = CardDefaults.elevatedCardColors(containerColor = colorResource(R.color.item_card_white)),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = dimensionResource(R.dimen.item_card_elevation)
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            ImageSection(imgUrl, compactMode) // CAMBIO: AJUSTA LA ALTURA VISUAL DE LA IMAGEN SEGUN EL MODO COMPACTO.
            TextSection(mainText, secondaryText, numberPhone, direction, isFavorite, onFavoriteClick, compactMode) // CAMBIO: AJUSTA EL CONTENIDO TEXTUAL SEGUN EL MODO COMPACTO.
        }
    }
}

@Composable
fun ImageSection (imgUrl: String, compactMode: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (compactMode) 110.dp else dimensionResource(R.dimen.img_conver_height)) // CAMBIO: REDUCE MUCHO LA ALTURA DE LA IMAGEN EN FAVORITOS.
    ) {
        //IMAGE URL
        AsyncImage(
            model = imgUrl,
            contentDescription = "imgUrl",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        topStart = dimensionResource(R.dimen.item_card_corner_radius),
                        topEnd = dimensionResource(R.dimen.item_card_corner_radius)
                    )
                )
        )
        //ICON_STAR - CIRCLE
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(dimensionResource(R.dimen.common_padding_xl))
                .clip(RoundedCornerShape(dimensionResource(R.dimen.common_padding_middle)))
                .background(colorResource(R.color.item_card_white))
                .padding(
                    horizontal = dimensionResource(R.dimen.item_card_text_horizontal_padding),
                    vertical = dimensionResource(R.dimen.item_card_text_vertical_padding)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.item_card_rating_badge_spacing))
        ) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = "Valoracion",
                tint = colorResource(R.color.item_card_rating_star),
                modifier = Modifier.size(dimensionResource(R.dimen.item_card_rating_icon_size))
            )
            Text(
                text = stringResource(R.string.valoration_number),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
        }
        //CIRCLE WINEBBLE
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    start = dimensionResource(R.dimen.common_padding_long),
                    bottom = dimensionResource(R.dimen.common_padding_long)
                )
                .size(dimensionResource(R.dimen.item_card_logo_size))
                .clip(CircleShape)
                .background(colorResource(R.color.item_card_white))
                .border(
                    dimensionResource(R.dimen.item_card_logo_border_width),
                    colorResource(R.color.item_card_logo_border),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.main_name),
                color = colorResource(R.color.item_card_brand_wine),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun TextSection(mainText: String,
                secondaryText: String,
                numberPhone: Int,
                direction: String,
                isFavorite: Boolean,
                onFavoriteClick: () -> Unit,
                compactMode: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen.item_card_text_horizontal_padding),
                vertical = dimensionResource(R.dimen.item_card_text_vertical_padding)
            ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.item_card_text_vertical_padding))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            //MAIN TEXT
            Text(
                text = mainText,
                modifier = Modifier.weight(1f),
                style = if (compactMode) MaterialTheme.typography.titleLarge else MaterialTheme.typography.headlineSmall, // CAMBIO: USA UN TITULO MAS PEQUENO EN LA VERSION DE FAVORITOS.
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder, // CAMBIO: CAMBIA EL ICONO SEGUN SI LA CARD YA ESTA GUARDADA.
                contentDescription = "Favorite",
                tint = if (isFavorite) colorResource(R.color.item_card_brand_wine) else colorResource(R.color.item_card_brand_wine), // CAMBIO: RESALTA EL CORAZON CUANDO EL ELEMENTO YA ES FAVORITO.
                modifier = Modifier
                    .padding(start = dimensionResource(R.dimen.item_card_text_horizontal_padding))
                    .size(dimensionResource(R.dimen.item_card_favorite_icon_size))
                    .clickable { onFavoriteClick() } // CAMBIO: HACE CLICKABLE EL CORAZON PARA GUARDAR O QUITAR FAVORITOS.
            )
        }

        //SUBTITLE
        Text(
            text = stringResource(R.string.sub_title),
            style = if (compactMode) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.titleMedium, // CAMBIO: COMPRIME EL SUBTITULO EN LA CARD COMPACTA.
        )

        //TEXT_DIRECTION
        Text(
            text = direction,
            style = if (compactMode) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodyLarge, // CAMBIO: REDUCE EL PESO VISUAL DE LA DIRECCION EN FAVORITOS.
            color = colorResource(R.color.item_card_secondary_info),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //NUMBER TEXT
        Text(
            text = "Telefono: $numberPhone",
            style = if (compactMode) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyMedium, // CAMBIO: HACE MAS LIGERO EL TELEFONO EN LA CARD COMPACTA.
            color = colorResource(R.color.item_card_secondary_info),
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = secondaryText,
            style = if (compactMode) MaterialTheme.typography.bodySmall else MaterialTheme.typography.bodyMedium, // CAMBIO: REDUCE EL CUERPO DEL TEXTO EN FAVORITOS.
            color = colorResource(R.color.item_card_description_text),
            maxLines = if (compactMode) 2 else 3, // CAMBIO: RECORTA MAS LA DESCRIPCION EN FAVORITOS PARA HACER LA CARD MAS BAJA.
            overflow = TextOverflow.Ellipsis
        )
    }
}
