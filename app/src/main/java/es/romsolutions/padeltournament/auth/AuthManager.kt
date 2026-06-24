package es.romsolutions.padeltournament.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.analytics.ktx.analytics
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID

class AuthManager(private val context: Context) {
    private val auth: FirebaseAuth = Firebase.auth
    private val credentialManager = CredentialManager.create(context)
    private val WEB_CLIENT_ID = "416205523640-1i8fb36l4vodf56q0nuolt7hbqn4b11u.apps.googleusercontent.com"
    private val TAG = "AuthManager"

    suspend fun signInWithGoogle(): Boolean {
        return try {
            Log.d(TAG, "Iniciando proceso de login con Google (SignInOption)...")
            
            val signInWithGoogleOption = GetSignInWithGoogleOption.Builder(WEB_CLIENT_ID)
                .setNonce(generateNonce())
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()

            val result = credentialManager.getCredential(context, request)
            handleSignIn(result)
        } catch (e: GetCredentialException) {
            Log.e(TAG, "Error de CredentialManager: ${e.message} (Tipo: ${e.type})")
            false
        } catch (e: Exception) {
            Log.e(TAG, "Error desconocido en login: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    private suspend fun handleSignIn(result: androidx.credentials.GetCredentialResponse): Boolean {
        val credential = result.credential
        
        return when (credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    val authCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    val authResult = auth.signInWithCredential(authCredential).await()
                    
                    authResult.user?.let { user ->
                        Log.d(TAG, "Usuario autenticado: ${user.displayName}, ${user.email}")
                        Firebase.analytics.setUserId(user.uid)
                    }
                    true
                } else {
                    Log.e(TAG, "Tipo de credencial no esperada: ${credential.type}")
                    false
                }
            }
            else -> {
                Log.e(TAG, "Credencial no reconocida")
                false
            }
        }
    }

    private fun generateNonce(): String {
        val rawNonce = UUID.randomUUID().toString()
        val bytes = rawNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun isUserLoggedIn(): Boolean = auth.currentUser != null

    fun getCurrentUserName(): String? {
        val user = auth.currentUser
        Log.d(TAG, "Obteniendo nombre: ${user?.displayName}")
        return user?.displayName
    }

    fun getCurrentUserPhotoUrl(): String? {
        val user = auth.currentUser
        Log.d(TAG, "Obteniendo foto: ${user?.photoUrl}")
        return user?.photoUrl?.toString()
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid
}