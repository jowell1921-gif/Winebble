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
            user = UserData(uid = currentUserId)
        }
    }

    private fun validateAuthFields(
        name: String = "",
        email: String,
        password: String,
        isRegisterMode: Boolean
    ): Boolean {
        if (isRegisterMode && name.trim().isEmpty()) {
            errorMessage = "Debes introducir tu nombre"
            return false
        }

        if (email.trim().isEmpty()) {
            errorMessage = "Debes introducir tu correo"
            return false
        }

        if (password.isBlank()) {
            errorMessage = "Debes introducir tu contraseña"
            return false
        }

        if (password.length < 6) {
            errorMessage = "La contraseña debe tener al menos 6 caracteres"
            return false
        }

        return true
    }

    fun register(
        name: String,
        email: String,
        password: String
    ) {

        viewModelScope.launch {
            if (!validateAuthFields(name = name, email = email, password = password, isRegisterMode = true)) {
                return@launch
            }
            isLoading = true
            errorMessage = null
            successMessage = null

            try {
                val result = repository.registerUser(name, email, password)

                result.onSuccess { userData ->
                    user = userData
                    successMessage = "Usuario registrado correctamente"
                }.onFailure { exception ->
                    errorMessage = exception.message
                }
            } finally {
                isLoading = false
            }
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            if (!validateAuthFields(email = email, password = password, isRegisterMode = false)) {
                return@launch
            }
            isLoading = true
            errorMessage = null
            successMessage = null

            try {
                val result = repository.loginUser(email, password)

                result.onSuccess { userData ->
                    user = userData
                    successMessage = "Inicio de sesión correcto"
                }.onFailure { exception ->
                    errorMessage = exception.message
                }
            } finally {
                isLoading = false
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
        user = null
        errorMessage = null
        successMessage = null
    }

    fun getCurrentUserId(): String? {
        return repository.getCurrentUserId()
    }
}
