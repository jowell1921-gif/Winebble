package com.example.winebble.data.repository

import com.example.winebble.data.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException // CAMBIO: PERMITE DETECTAR EMAIL MAL FORMADO O PASSWORD INCORRECTA.
import com.google.firebase.auth.FirebaseAuthInvalidUserException // CAMBIO: PERMITE DETECTAR USUARIO NO REGISTRADO O DESHABILITADO.
import com.google.firebase.auth.FirebaseAuthUserCollisionException // CAMBIO: PERMITE DETECTAR SI EL CORREO YA ESTA REGISTRADO.
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
        val cleanName = name.trim() // CAMBIO: LIMPIA ESPACIOS SOBRANTES EN EL NOMBRE.
        val cleanEmail = email.trim() // CAMBIO: LIMPIA ESPACIOS SOBRANTES EN EL CORREO.

        return try {
            val authResult = withTimeout(10_000) { auth.createUserWithEmailAndPassword(cleanEmail, password).await() } // CAMBIO: USA EL CORREO LIMPIO PARA REGISTRAR
            val firebaseUser = authResult.user ?: throw Exception("No se pudo crear el usuario") // CAMBIO: VALIDA QUE FIREBASE HAYA DEVUELTO EL USUARIO.

            val userData = UserData(
                uid = firebaseUser.uid,
                name = cleanName,
                email = cleanEmail
            )

            try {
                withTimeout(10_000) { // CAMBIO: LIMITA EL TIEMPO DE GUARDADO EN FIRESTORE PARA QUE LA UI NO SE QUEDE CARGANDO.
                    firestore.collection("users")
                        .document(firebaseUser.uid)
                        .set(userData)
                        .await()
                }
            } catch (_: Exception) {
                // CAMBIO: SI FALLA FIRESTORE, EL USUARIO YA EXISTE EN AUTH Y PUEDE ENTRAR EN LA APLICACION.
            }

            Result.success(userData)
        }
        catch (e: FirebaseAuthUserCollisionException) {
        Result.failure(Exception("Este correo ya está registrado")) // CAMBIO: MUESTRA UN MENSAJE AMIGABLE SI EL EMAIL YA EXISTE.
    }   catch (e: FirebaseAuthInvalidCredentialsException) {
            val message = when (e.errorCode) { // CAMBIO: DISTINGUE MEJOR EL MOTIVO DEL FALLO EN EL REGISTRO.
                "ERROR_INVALID_EMAIL" -> "El correo no tiene un formato válido" // CAMBIO: MENSAJE ESPECIFICO PARA EMAIL INCORRECTO.
                "ERROR_WEAK_PASSWORD" -> "La contraseña debe tener al menos 6 caracteres" // CAMBIO: MENSAJE ESPECIFICO SI FIREBASE CONSIDERA LA PASSWORD DEMASIADO DEBIL.
                else -> "No se pudo validar el registro" // CAMBIO: MENSAJE GENERAL LIMPIO PARA OTROS CASOS.
            }
            Result.failure(Exception(message))
    }   catch (e: TimeoutCancellationException) {
        Result.failure(Exception("La conexión ha tardado demasiado. Inténtalo otra vez")) // CAMBIO: EVITA MOSTRAR UN ERROR TECNICO SI FIREBASE TARDA DEMASIADO.
    }   catch (e: Exception) {
        Result.failure(Exception("No se pudo completar el registro")) } // CAMBIO: SUSTITUYE EL ERROR TECNICO POR UNO MAS LIMPIO.
    }

    suspend fun loginUser(
        email: String,
        password: String
    ): Result<UserData> {
        val cleanEmail = email.trim()

        return try {
            val authResult = withTimeout(10_000) { auth.signInWithEmailAndPassword(cleanEmail, password).await() } // CAMBIO: LIMITA EL TIEMPO DE ESPERA DEL LOGIN PARA EVITAR BLOQUEOS.
            val firebaseUser = authResult.user ?: throw Exception("No se pudo iniciar sesion") // CAMBIO: VALIDA QUE FIREBASE HAYA DEVUELTO EL USUARIO.

            val document = withTimeout(10_000) { // CAMBIO: LIMITA EL TIEMPO DE LECTURA EN FIRESTORE.
                firestore.collection("users")
                    .document(firebaseUser.uid)
                    .get()
                    .await()
            }

            val userData = document.toObject(UserData::class.java)
                ?: UserData(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: cleanEmail
                ) // CAMBIO: SI NO EXISTE PERFIL EN FIRESTORE, CREA UN USUARIO BASICO CON LOS DATOS DE AUTH.

            Result.success(userData)
        }
        catch (e: FirebaseAuthInvalidUserException) {
        Result.failure(Exception("No existe una cuenta con este correo")) // CAMBIO: INDICA QUE EL USUARIO NO ESTA REGISTRADO.

    }   catch (e: FirebaseAuthInvalidCredentialsException) {
        val message = when (e.errorCode) { // CAMBIO: REVISA EL CODIGO INTERNO DE FIREBASE PARA DAR UN MENSAJE MAS PRECISO.
            "ERROR_INVALID_EMAIL" -> "El correo no tiene un formato válido" // CAMBIO: MENSAJE ESPECIFICO SI EL EMAIL ESTA MAL ESCRITO.
            "ERROR_WRONG_PASSWORD" -> "La contraseña es incorrecta" // CAMBIO: MENSAJE ESPECIFICO SI LA PASSWORD NO COINCIDE.
            else -> "Las credenciales no son válidas" // CAMBIO: MENSAJE GENERAL SI FIREBASE DEVUELVE OTRO CASO PARECIDO.
        }
        Result.failure(Exception(message)) // CAMBIO: DEVUELVE EL MENSAJE LIMPIO A LA UI.

    }   catch (e: TimeoutCancellationException) {
        Result.failure(Exception("La conexión ha tardado demasiado. Inténtalo otra vez")) // CAMBIO: EVITA MOSTRAR UN ERROR TECNICO SI FIREBASE TARDA DEMASIADO.

    }   catch (e: Exception) {
        Result.failure(Exception("No se pudo iniciar sesión")) } // CAMBIO: MENSAJE GENERAL LIMPIO PARA ERRORES NO CONTROLADOS.
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}
