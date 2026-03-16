package com.example.winebble.Views

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItemDefaults
import com.example.winebble.R
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.remote.creation.first
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.winebble.components.ProfileList
import com.example.winebble.getProfileItems
import com.example.winebble.ui.theme.WinebbleTheme
import kotlin.time.Duration.Companion.seconds


/**
 * Project: Winebble
 * From: com.example.winebble.Views
 * Created by: Joel Arturo Osorio
 * On: 13/03/2026 at 12: 48
 * All rights reserved 2026.
 */
@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    WinebbleTheme {
        Profile()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Profile() {

    val backgroundColor = colorResource(R.color.principal)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    // Row principal para controlar todos los elementos
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween // título a la izquierda, perfil a la derecha
                    ) {
                        // Título / user_name
                        Text(
                            text = stringResource(R.string.user_name),
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Imagen + texto del perfil
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,

                        ) {
                            Image(
                                painter = painterResource(R.drawable.profile_male),
                                contentDescription = "profile",
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = 2.dp,
                                        color = Color.Black,
                                        shape = CircleShape
                                    ),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(4.dp)) // separación entre imagen y texto

                            Text(
                                text = stringResource(R.string.main_profile),
                                color = Color.White,
                                fontSize = 18.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    scrolledContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color.Gray.copy(alpha = 0.3f),
                        shape = RectangleShape
                    )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
                val items = getProfileItems()

                LazyColumn {
                    items(items) { item ->
                        ProfileList(
                            text = item.first,
                            icon = painterResource(id = item.second)
                        )
                    }
                }
            }
        }
    }