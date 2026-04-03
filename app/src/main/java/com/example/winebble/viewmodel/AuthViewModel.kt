package com.example.winebble.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.winebble.data.model.UserData
import com.example.winebble.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    var user by mutableStateOf<UserData?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var successMessage by mutableStateOf<String?>(null)
        private set

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        val currentUserId = repository.getCurrentUserId()
        if (currentUserId != null) {
            user = UserData(uid = currentUserId) // CAMBIO: RECUPERA LA SESION ABIERTA AL ENTRAR EN LA APP.
        }
    }

    fun register(
        name: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            isLoading = true // CAMBIO: ACTIVA EL ESTADO DE CARGA MIENTRAS FIREBASE RESPONDE.
            errorMessage = null // CAMBIO: LIMPIA EL ERROR ANTERIOR ANTES DE UN NUEVO REGISTRO.
            successMessage = null // CAMBIO: LIMPIA EL MENSAJE DE EXITO ANTERIOR ANTES DE UN NUEVO REGISTRO.

            try {
                val result = repository.registerUser(name, email, password)

                result.onSuccess { userData ->
                    user = userData // CAMBIO: AL TENER USUARIO, MAINACTIVITY DEJA DE MOSTRAR LOGIN Y ENTRA EN LA APP.
                    successMessage = "Usuario registrado correctamente" // CAMBIO: MUESTRA CONFIRMACION DE REGISTRO.
                }.onFailure { exception ->
                    errorMessage = exception.message
                }
            } finally {
                isLoading = false // CAMBIO: GARANTIZA QUE EL BOTON Y EL INDICADOR DEJEN DE CARGAR INCLUSO SI ALGO FALLA.
            }
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            isLoading = true // CAMBIO: ACTIVA EL ESTADO DE CARGA MIENTRAS FIREBASE RESPONDE.
            errorMessage = null // CAMBIO: LIMPIA EL ERROR ANTERIOR ANTES DE UN NUEVO LOGIN.
            successMessage = null // CAMBIO: LIMPIA EL MENSAJE DE EXITO ANTERIOR ANTES DE UN NUEVO LOGIN.

            try {
                val result = repository.loginUser(email, password)

                result.onSuccess { userData ->
                    user = userData // CAMBIO: AL TENER USUARIO, MAINACTIVITY ENTRA EN LA APP PRINCIPAL.
                    successMessage = "Inicio de sesion correcto" // CAMBIO: MUESTRA CONFIRMACION DE LOGIN.
                }.onFailure { exception ->
                    errorMessage = exception.message
                }
            } finally {
                isLoading = false // CAMBIO: GARANTIZA QUE EL ESTADO DE CARGA SIEMPRE TERMINE.
            }
        }
    }

    fun clearError() {
        errorMessage = null
    }

    fun clearSuccessMessage() {
        successMessage = null
    }

    fun logout() {
        repository.logout()
        user = null // CAMBIO: ELIMINA EL USUARIO ACTUAL AL CERRAR SESION.
        errorMessage = null // CAMBIO: LIMPIA ERRORES AL SALIR.
        successMessage = null // CAMBIO: LIMPIA MENSAJES DE EXITO AL SALIR.
    }

    fun getCurrentUserId(): String? {
        return repository.getCurrentUserId()
    }
}
