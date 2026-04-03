package com.example.winebble.Views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.winebble.R
import com.example.winebble.ui.theme.WinebbleTheme
import com.example.winebble.viewmodel.AuthViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.Image // CAMBIO: PERMITE MOSTRAR EL LOGO DESDE DRAWABLE.
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource


/**
 * Project: Winebble
 * From: com.example.winebble.Views
 * Created by: Joel Arturo Osorio
 * On: 01/04/2026 at 12: 18
 * All rights reserved 2026.
 */
@Preview(showBackground = true)
@Composable
fun PreviewUserLogin() {
    WinebbleTheme {
        UserLogin(modifier = Modifier)
    }
}

@Composable
fun UserLogin(modifier: Modifier,
              authViewModel: AuthViewModel = viewModel()) {

    var nameValue by remember { mutableStateOf("") }
    var emailValue by remember{ mutableStateOf("") }
    var passwordValue by remember{ mutableStateOf("") }
    var isRegisterMode by remember { mutableStateOf(false) }

    val emptyComposition =
        rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.wine_grapes))
    val emptyProgress = animateLottieCompositionAsState(
        composition = emptyComposition.value,
        iterations = LottieConstants.IterateForever
    )

    Box(modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            LottieAnimation(
                composition = emptyComposition.value,
                progress = { emptyProgress.value },
                modifier = Modifier.size(220.dp)
            )

            Text(
                text = if (isRegisterMode) stringResource(R.string.create_user)
                else stringResource(R.string.login_sesion),
                style = MaterialTheme.typography.headlineSmall
            )

            if (isRegisterMode) {
                OutlinedTextField(
                    value = nameValue,
                    onValueChange = { nameValue = it },
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.common_padding_min)),
                    label = { Text(stringResource(R.string.name_user)) }
                )
            }

            OutlinedTextField(
                value = emailValue,
                onValueChange = { emailValue = it },
                modifier = Modifier.padding(top = dimensionResource(R.dimen.common_padding_min)),
                label = { Text(stringResource(R.string.email)) }
            )

            OutlinedTextField(
                value = passwordValue,
                onValueChange = { passwordValue = it },
                modifier = Modifier.padding(top = dimensionResource(R.dimen.common_padding_min)),
                label = { Text(stringResource(R.string.password)) },
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.common_padding_default)))

            Button(
                onClick = {
                    if (isRegisterMode) {
                        authViewModel.register(
                            name = nameValue,
                            email = emailValue,
                            password = passwordValue
                        )
                    } else {
                        authViewModel.login(
                            email = emailValue,
                            password = passwordValue
                        )
                    }
                },
                enabled = !authViewModel.isLoading
            )
            {
                Icon(
                    Icons.AutoMirrored.Filled.Login,
                    contentDescription = null,
                    modifier = Modifier.padding(end = dimensionResource(R.dimen.common_padding_min))
                )

                Text(
                    text = if (authViewModel.isLoading) stringResource(R.string.load_user)
                    else {
                        if (isRegisterMode) stringResource(R.string.register_user)
                        else stringResource(R.string.enter_user)
                    }
                )
            }
            if (authViewModel.isLoading) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.common_padding_min)))
                CircularProgressIndicator()
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.common_padding_default)))

            Text(
                text = if (isRegisterMode) stringResource(R.string.answer_account)
                else stringResource(R.string.not_account)
            )

            Text(
                text = if (isRegisterMode) stringResource(R.string.login_sesion)
                else stringResource(R.string.new_register),
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    isRegisterMode = !isRegisterMode
                    authViewModel.clearError()

                }
            )

            authViewModel.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.common_padding_min)))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error, // CAMBIO: MANTIENE EL MENSAJE COMO ERROR VISUAL.
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.common_padding_default)), // CAMBIO: HACE QUE EL TEXTO OCUPE ANCHO Y NO QUEDE PEGADO A UN LADO.
                    textAlign = TextAlign.Center
                )
            }

            authViewModel.successMessage?.let { success ->
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.common_padding_min)))
                Text(
                    text = success,
                    color = MaterialTheme.colorScheme.primary, // CAMBIO: MUESTRA EL EXITO CON EL COLOR PRINCIPAL.
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dimensionResource(R.dimen.common_padding_default)), // CAMBIO: DA ANCHO AL MENSAJE PARA PODER CENTRARLO BIEN.
                    textAlign = TextAlign.Center // CAMBIO: CENTRA EL MENSAJE DE EXITO.
                )
            }

            authViewModel.user?.let { user ->
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.common_padding_min)))
                Text(
                    text = "Bienvenido ${user.name.ifEmpty { user.email }}",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
