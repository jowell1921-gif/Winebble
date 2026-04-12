package com.example.winebble.data.model

data class PaymentMethodData(
    val id: String = "",
    val userId: String = "",
    val brand: String = "",
    val cardholderName: String = "",
    val last4: String = "",
    val expMonth: Int = 0,
    val expYear: Int = 0,
    val isDefault: Boolean = false
)
