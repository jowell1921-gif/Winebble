package com.example.winebble.components


import android.R.attr.text
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.example.winebble.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winebble.ui.theme.WinebbleTheme
import coil.compose.AsyncImage
import com.example.winebble.ui.theme.Typography


/**
 * Project: Winebble
 * From: com.example.winebble.ui.theme
 * Created by: Joel Arturo Osorio
 * On: 06/03/2026 at 11: 38
 * All rights reserved 2026.
 */

@Preview(showBackground = true)
@Composable
private fun BasicPreview(){
    WinebbleTheme {
        ProfileList(
            text = "Algún texto",
            icon = painterResource(R.drawable.icon_person),
            modifier = Modifier)
    }
}

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

@Composable
fun ProfileList(text: String,
                icon: Painter,
                modifier: Modifier = Modifier) {
    Column {
        Row(modifier = Modifier.fillMaxWidth()
            .padding(dimensionResource(R.dimen.common_padding_default)),
            verticalAlignment = Alignment.CenterVertically) {
            Image(painter = icon,
                contentDescription = text,
                modifier = Modifier
                    .size(36.dp)
                    .padding(end = 16.dp))
            Text(text, Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.common_padding_default)),
                style = Typography.bodyLarge
            )
        }
        HorizontalDivider()
    }
}

@Composable
fun ItemList(modifier: Modifier = Modifier,
                     mainText: String,
                     secondaryText: String,
                     imgUrl: String = "",
                     icon: ImageVector? = null,
                     overlineText: String = "",
                     showDivider: Boolean = false,
                     price: Double = 0.0){

    Column  {
        val imageShape = RoundedCornerShape(16.dp)

        ListItem(modifier = modifier
            .fillMaxWidth(),
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
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White)
            },
            leadingContent = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 8.dp)
                ) {

                    AsyncImage(
                        model = imgUrl,
                        contentDescription = "Image_url",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(72.dp)
                            .clip(imageShape)
                            .border(
                                width = 2.dp,
                                color = colorResource(R.color.gold),
                                shape = imageShape
                            )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "€${"%.2f".format(price)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            trailingContent = {
                icon?.let {
                    Icon(icon, contentDescription = "Icon_image", tint = Color.White)

                }
            },
            overlineContent ={
                if(overlineText.isNotEmpty())
                Text(
                    overlineText,
                    color = colorResource(R.color.gold),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        )
    }

    if(showDivider) HorizontalDivider()
}

