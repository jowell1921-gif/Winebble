package com.example.winebble.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.winebble.data.model.PaymentMethodData
import com.example.winebble.data.repository.PaymentRepository
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID

class PaymentViewModel(
    private val repository: PaymentRepository = PaymentRepository()
) : ViewModel() {

    var paymentMethods by mutableStateOf<List<PaymentMethodData>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var successMessage by mutableStateOf<String?>(null)
        private set

    fun loadPaymentMethods(userId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val result = repository.getPaymentMethods(userId)

            result.onSuccess { methods ->
                paymentMethods = methods
            }.onFailure { exception ->
                errorMessage = exception.message
            }

            isLoading = false
        }
    }

    private fun validateCardForm(
        cardholderName: String,
        cardNumber: String,
        expMonth: String,
        expYear: String,
        cvv: String
    ): Boolean {
        val cleanName = cardholderName.trim()
        val cleanNumber = cardNumber.filter { it.isDigit() }
        val cleanMonth = expMonth.trim()
        val cleanYear = expYear.trim()
        val cleanCvv = cvv.filter { it.isDigit() }

        if (cleanName.isBlank() || cleanNumber.isBlank() || cleanMonth.isBlank() || cleanYear.isBlank() || cleanCvv.isBlank()) {
            errorMessage = "Debes rellenar todos los campos"
            return false
        }

        if (cleanNumber.length < 13 || cleanNumber.length > 19) {
            errorMessage = "El número de tarjeta debe tener entre 13 y 19 dígitos"
            return false
        }

        val month = cleanMonth.toIntOrNull()
        val year = cleanYear.toIntOrNull()

        if (month == null || month !in 1..12) {
            errorMessage = "El mes de caducidad no es válido"
            return false
        }

        if (year == null || cleanYear.length != 4) {
            errorMessage = "El año de caducidad no es válido"
            return false
        }

        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1

        if (year < currentYear || (year == currentYear && month < currentMonth)) {
            errorMessage = "La tarjeta está caducada"
            return false
        }

        if (cleanCvv.length < 3) {
            errorMessage = "El CVV debe tener al menos 3 dígitos"
            return false
        }

        return true
    }

    private fun getCardBrand(cardNumber: String): String {
        return when {
            cardNumber.startsWith("4") -> "Visa"
            cardNumber.startsWith("5") -> "Mastercard"
            else -> "Tarjeta"
        }
    }

    fun addPaymentMethod(
        userId: String,
        cardholderName: String,
        cardNumber: String,
        expMonth: String,
        expYear: String,
        cvv: String
    ) {
        if (!validateCardForm(cardholderName, cardNumber, expMonth, expYear, cvv)) {
            return
        }

        val cleanNumber = cardNumber.filter { it.isDigit() }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            successMessage = null

            val optimisticMethod = PaymentMethodData(
                id = UUID.randomUUID().toString(),
                userId = userId,
                cardholderName = cardholderName.trim(),
                brand = getCardBrand(cleanNumber),
                last4 = cleanNumber.takeLast(4),
                expMonth = expMonth.toInt(),
                expYear = expYear.toInt(),
                isDefault = paymentMethods.isEmpty()
            )

            val previousMethods = paymentMethods
            paymentMethods = previousMethods + optimisticMethod

            val result = repository.addPaymentMethod(optimisticMethod)

            result.onSuccess { savedMethod ->
                paymentMethods = paymentMethods.map { method ->
                    if (method.id == optimisticMethod.id) savedMethod else method
                }
                successMessage = "Tarjeta añadida correctamente"
            }.onFailure { exception ->
                paymentMethods = previousMethods
                errorMessage = exception.message
            }

            isLoading = false
        }
    }

    fun updatePaymentMethod(
        methodId: String,
        userId: String,
        cardholderName: String,
        cardNumber: String,
        expMonth: String,
        expYear: String,
        cvv: String,
        isDefault: Boolean
    ) {
        if (!validateCardForm(cardholderName, cardNumber, expMonth, expYear, cvv)) {
            return
        }

        val cleanNumber = cardNumber.filter { it.isDigit() }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            successMessage = null

            val updatedMethod = PaymentMethodData(
                id = methodId,
                userId = userId,
                cardholderName = cardholderName.trim(),
                brand = getCardBrand(cleanNumber),
                last4 = cleanNumber.takeLast(4),
                expMonth = expMonth.toInt(),
                expYear = expYear.toInt(),
                isDefault = isDefault
            )

            val previousMethods = paymentMethods
            paymentMethods = paymentMethods.map { method ->
                if (method.id == updatedMethod.id) updatedMethod else method
            }

            val result = repository.updatePaymentMethod(updatedMethod)

            result.onSuccess { savedMethod ->
                paymentMethods = paymentMethods.map { method ->
                    if (method.id == savedMethod.id) savedMethod else method
                }
                successMessage = "Tarjeta actualizada correctamente"
            }.onFailure { exception ->
                paymentMethods = previousMethods
                errorMessage = exception.message
            }

            isLoading = false
        }
    }

    fun deletePaymentMethod(userId: String, methodId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            successMessage = null

            val previousMethods = paymentMethods
            paymentMethods = paymentMethods.filterNot { method -> method.id == methodId }

            val result = repository.deletePaymentMethod(methodId)

            result.onSuccess {
                successMessage = "Método de pago eliminado correctamente"
            }.onFailure { exception ->
                paymentMethods = previousMethods
                errorMessage = exception.message
            }

            isLoading = false
        }
    }

    fun setDefaultPaymentMethod(userId: String, methodId: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            successMessage = null

            val result = repository.setDefaultPaymentMethod(userId, methodId)

            result.onSuccess {
                paymentMethods = paymentMethods.map { method ->
                    method.copy(isDefault = method.id == methodId)
                }
                successMessage = "Método predeterminado actualizado"
            }.onFailure { exception ->
                errorMessage = exception.message
            }

            isLoading = false
        }
    }

    fun clearMessages() {
        errorMessage = null
        successMessage = null
    }
}
