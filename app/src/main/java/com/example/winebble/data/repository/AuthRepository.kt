package com.example.winebble.data.repository

import com.example.winebble.data.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.TimeoutCancellationException

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): Result<UserData> {
        val cleanName = name.trim()
        val cleanEmail = email.trim()

        return try {
            val authResult = withTimeout(10_000) { auth.createUserWithEmailAndPassword(cleanEmail, password).await() }
            val firebaseUser = authResult.user ?: throw Exception("No se pudo crear el usuario")

            val userData = UserData(
                uid = firebaseUser.uid,
                name = cleanName,
                email = cleanEmail
            )

            try {
                withTimeout(10_000) {
                    firestore.collection("users")
                        .document(firebaseUser.uid)
                        .set(userData)
                        .await()
                }
            } catch (_: Exception) {
            }

            Result.success(userData)
        }
        catch (e: FirebaseAuthUserCollisionException) {
        Result.failure(Exception("Este correo ya está registrado"))
    }   catch (e: FirebaseAuthInvalidCredentialsException) {
            val message = when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> "El correo no tiene un formato válido"
                "ERROR_WEAK_PASSWORD" -> "La contraseña debe tener al menos 6 caracteres"
                else -> "No se pudo validar el registro"
            }
            Result.failure(Exception(message))
    }   catch (e: TimeoutCancellationException) {
        Result.failure(Exception("La conexión ha tardado demasiado. Inténtalo otra vez"))
    }   catch (e: Exception) {
        Result.failure(Exception("No se pudo completar el registro")) }
    }

    suspend fun loginUser(
        email: String,
        password: String
    ): Result<UserData> {
        val cleanEmail = email.trim()

        return try {
            val authResult = withTimeout(10_000) { auth.signInWithEmailAndPassword(cleanEmail, password).await() }
            val firebaseUser = authResult.user ?: throw Exception("No se pudo iniciar sesión")

            val document = withTimeout(10_000) {
                firestore.collection("users")
                    .document(firebaseUser.uid)
                    .get()
                    .await()
            }

            val userData = document.toObject(UserData::class.java)
                ?: UserData(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: cleanEmail
                )

            Result.success(userData)
        }
        catch (e: FirebaseAuthInvalidUserException) {
        Result.failure(Exception("No existe una cuenta con este correo"))

    }   catch (e: FirebaseAuthInvalidCredentialsException) {
        val message = when (e.errorCode) {
            "ERROR_INVALID_EMAIL" -> "El correo no tiene un formato válido"
            "ERROR_WRONG_PASSWORD" -> "La contraseña es incorrecta"
            else -> "Las credenciales no son válidas"
        }
        Result.failure(Exception(message))

    }   catch (e: TimeoutCancellationException) {
        Result.failure(Exception("La conexión ha tardado demasiado. Inténtalo otra vez"))

    }   catch (e: Exception) {
        Result.failure(Exception("No se pudo iniciar sesión")) }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
