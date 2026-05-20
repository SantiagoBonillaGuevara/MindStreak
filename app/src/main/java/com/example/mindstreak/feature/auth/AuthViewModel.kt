package com.example.mindstreak.feature.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstreak.R
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
    data class Success(val message: String, val isSignUp: Boolean = false) : AuthState()
    data class Error(val error: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "AuthViewModel"
    private val client = SupabaseClientProvider.client
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()
    private var isManualAction = false

    private fun getString(resId: Int): String =
        getApplication<Application>().getString(resId)

    init {
        viewModelScope.launch {
            client.auth.sessionStatus.collect { status ->
                if (status is SessionStatus.Authenticated && !isManualAction) {
                    _authState.value = AuthState.Success(getString(R.string.auth_success_welcome))
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
                _authState.value = AuthState.Error(getString(R.string.auth_error_google))
            }
        }
    }

    fun signUp(email: String, password: String, name: String) {
        if (email.isBlank() || password.isBlank() || name.isBlank()) {
            _authState.value = AuthState.Error(getString(R.string.auth_error_fields_empty))
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
                _authState.value = AuthState.Success(
                    message = getString(R.string.auth_success_signup),
                    isSignUp = true
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error en signUp: ${e.message}")
                _authState.value = AuthState.Error(e.localizedMessage ?: getString(R.string.auth_error_generic))
            }
        }
    }

    fun signIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error(getString(R.string.auth_error_fields_empty))
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
                _authState.value = AuthState.Success(getString(R.string.auth_success_signin))
            } catch (e: Exception) {
                Log.e(TAG, "Error en signIn: ${e.message}")
                _authState.value = AuthState.Error(getString(R.string.auth_error_invalid_credentials))
            }
        }
    }

    fun sendPasswordResetEmail(email: String) {
        if (email.isBlank()) {
            _authState.value = AuthState.Error(getString(R.string.auth_error_fields_empty))
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                client.auth.resetPasswordForEmail(email)
                _authState.value = AuthState.Success(getString(R.string.auth_reset_password_sent))
            } catch (e: Exception) {
                Log.e(TAG, "Error en resetPassword: ${e.message}")
                _authState.value = AuthState.Error(e.localizedMessage ?: getString(R.string.auth_error_generic))
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
        isManualAction = false
    }
}
