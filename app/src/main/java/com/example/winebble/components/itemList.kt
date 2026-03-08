package com.example.winebble.components


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.winebble.R
import com.example.winebble.ui.theme.WinebbleTheme





/**
 * Project: Winebble
 * From: com.example.winebble.ui.theme
 * Created by: Joel Arturo Osorio
 * On: 06/03/2026 at 11: 38
 * All rights reserved 2026.
 */

@Composable
@Preview(showBackground = true)
fun AdvancePreview() {
    WinebbleTheme {
        ItemList(
            mainText = "Título",
            secondaryText = "Texto secundario",
            imgUrl = "",
            icon = null,
            showDivider = true
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemList(modifier: Modifier = Modifier,
                     mainText: String,
                     secondaryText: String,
                     imgUrl: String = "",
                     icon: ImageVector? = null,
                     overlineText: String = "",
                     showDivider: Boolean = false){

    Column  {
        val imageShape = RoundedCornerShape(16.dp)
        val borderShape = RoundedCornerShape((-16).dp)

        ListItem(modifier = Modifier,
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            headlineContent = {
                Text(
                    mainText,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    color = Color.White
                )
            },
            supportingContent ={
                Text(secondaryText,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White)
            },
            leadingContent = {
                GlideImage(//<------en rojo
                    model = imgUrl,
                    contentDescription = "Image_url",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(72.dp)
                        .clip(imageShape)
                        .border( width = 2.dp,
                            color = colorResource(R.color.gold))
                ) { glide ->//<------en rojo
                    glide.diskCacheStrategy(DiskCacheStrategy.ALL) //<------en rojo

                }
            },
            trailingContent = {
                icon?.let {
                    Icon(icon, contentDescription = "Icon_image")
                }
            },
            overlineContent ={
                if(overlineText.isNotEmpty()) Text(overlineText)
            }
        )
    }

    if(showDivider) HorizontalDivider()
}