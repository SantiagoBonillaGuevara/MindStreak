package com.example.mindstreak.feature.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstreak.data.remote.SupabaseClientProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.util.TimeZone

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String, val isSignUp: Boolean = false) : AuthState() // Añadimos isSignUp para diferenciar si navegamos o solo cambiamos de pestaña
    data class Error(val error: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val TAG = "AuthViewModel"
    private val client = SupabaseClientProvider.client
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()
    private var isManualAction = false

    init {
        viewModelScope.launch {
            client.auth.sessionStatus.collect { status ->
                if (status is SessionStatus.Authenticated && !isManualAction) {
                    // Login vía Google (o sesión persistida) -> Al Home
                    _authState.value = AuthState.Success("¡Bienvenido!")
                }
            }
        }
    }

    fun signInWithGoogle() {
        isManualAction = false
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                client.auth.signInWith(Google, "mindstreak://login-callback")
            } catch (e: Exception) {
                Log.e(TAG, "Error Google: ${e.message}")
                _authState.value = AuthState.Error("No se pudo conectar con Google")
            }
        }
    }

    fun signUp(email: String, password: String, name: String) {
        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            _authState.value = AuthState.Error("Todos los campos son obligatorios")
            return
        }
        val timeZone = TimeZone.getDefault().id

        isManualAction = true
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                client.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                    data = buildJsonObject {
                        put("name", name)
                        put("timeZone", timeZone)
                    }
                }
                // Registro exitoso -> Cambiamos a Success con flag isSignUp = true
                _authState.value = AuthState.Success(
                    message = "Registro exitoso. Por favor, confirma tu email para poder entrar.",
                    isSignUp = true
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error en signUp: ${e.message}")
                _authState.value = AuthState.Error(e.localizedMessage ?: "Error en registro")
            }
        }
    }

    fun signIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Campos obligatorios")
            return
        }

        isManualAction = true
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                client.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                // Login exitoso -> Al Home
                _authState.value = AuthState.Success("Sesión iniciada")
            } catch (e: Exception) {
                Log.e(TAG, "Error en signIn: ${e.message}")
                _authState.value = AuthState.Error("Credenciales inválidas o email no verificado")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
        isManualAction = false
    }
}
