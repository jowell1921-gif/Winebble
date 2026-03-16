package com.example.winebble

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import com.example.winebble.R
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
import com.example.winebble.Wine
import com.example.winebble.components.ItemList
import com.example.winebble.getAllWine
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
        MainScreen(Modifier.padding(16.dp)) {}
    }
}

@Composable
fun MainScreen ( modifier: Modifier, onSelectedItem: (Wine) -> Unit) {
    val wines =  getAllWine()

val buttons =listOf(
    stringResource((R.string.licor_list)),
    stringResource((R.string.wines_list))
)

var selectedIndex by remember { mutableIntStateOf(1) }

Column (modifier = modifier
    .fillMaxSize()
    .background(colorResource(R.color.principal))
    .padding(dimensionResource(R.dimen.common_padding_default)),
    verticalArrangement = Arrangement.Top) {
    Box(Modifier
        .fillMaxWidth(),
        contentAlignment = Alignment.Center) {
        SingleChoiceSegmentedButtonRow (Modifier
            .padding(vertical = dimensionResource(R.dimen.common_padding_min))
        ) {
            buttons.forEachIndexed { index, label ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults
                        .itemShape( index = index,
                            count = buttons.size
                        ),
                            onClick = { selectedIndex = index },
                                    selected = index == selectedIndex,
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = colorResource(R.color.gold),
                        inactiveContainerColor = Color.White
                    ) ,
                        label = {
                            Text(label) }
                        )
                    }
                }
            }

        if (selectedIndex == 1) {
            LazyColumn(modifier = Modifier
                .weight(1f)) {
                items(wines.size) { index ->
                    val wine = wines[index]
                    ItemList (modifier = Modifier.clickable{ onSelectedItem(wine)},
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

