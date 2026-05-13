package com.example.mindstreak.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstreak.data.remote.SupabaseClientProvider
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    object LoggedOut : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel : ViewModel() {
    private val client = SupabaseClientProvider.client
    private val _state = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val state = _state.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            try {
                client.auth.signOut()
                _state.value = ProfileState.LoggedOut
            } catch (e: Exception) {
                _state.value = ProfileState.Error(e.localizedMessage ?: "Error al cerrar sesión")
            }
        }
    }
}
