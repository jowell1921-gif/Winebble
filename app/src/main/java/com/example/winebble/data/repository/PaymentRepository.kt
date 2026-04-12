package com.example.winebble.data.repository

import com.example.winebble.data.model.PaymentMethodData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class PaymentRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun getPaymentMethods(userId: String): Result<List<PaymentMethodData>> {
        return try {
            val snapshot = withTimeout(10_000) {
                firestore.collection("payment_methods")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()
            }

            val methods = snapshot.documents.mapNotNull { document ->
                document.toObject(PaymentMethodData::class.java)?.copy(id = document.id)
            }

            Result.success(methods)
        } catch (e: Exception) {
            Result.failure(Exception("No se pudieron cargar los métodos de pago"))
        }
    }

    suspend fun addPaymentMethod(method: PaymentMethodData): Result<PaymentMethodData> {
        return try {
            val documentRef = if (method.id.isBlank()) {
                firestore.collection("payment_methods").document()
            } else {
                firestore.collection("payment_methods").document(method.id)
            }
            val methodWithId = method.copy(id = documentRef.id)

            documentRef.set(methodWithId).await()

            Result.success(methodWithId)
        } catch (e: Exception) {
            Result.failure(Exception("No se pudo guardar el método de pago"))
        }
    }

    suspend fun updatePaymentMethod(method: PaymentMethodData): Result<PaymentMethodData> {
        return try {
            firestore.collection("payment_methods")
                .document(method.id)
                .set(method)
                .await()

            Result.success(method)
        } catch (e: Exception) {
            Result.failure(Exception("No se pudo actualizar el método de pago"))
        }
    }

    suspend fun deletePaymentMethod(methodId: String): Result<Unit> {
        return try {
            firestore.collection("payment_methods")
                .document(methodId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("No se pudo eliminar el método de pago"))
        }
    }

    suspend fun setDefaultPaymentMethod(userId: String, selectedMethodId: String): Result<Unit> {
        return try {
            val snapshot = withTimeout(10_000) {
                firestore.collection("payment_methods")
                    .whereEqualTo("userId", userId)
                    .get()
                    .await()
            }

            snapshot.documents.forEach { document ->
                val isSelected = document.id == selectedMethodId

                firestore.collection("payment_methods")
                    .document(document.id)
                    .update("isDefault", isSelected)
                    .await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("No se pudo actualizar el método predeterminado"))
        }
    }
}
