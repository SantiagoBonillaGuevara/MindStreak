package com.example.mindstreak.feature.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstreak.data.model.User
import com.example.mindstreak.data.remote.SupabaseClientProvider
import com.example.mindstreak.data.repository.RepositoryProvider
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    object LoggedOut : ProfileState()
    data class Error(val message: String) : ProfileState()
}

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val client = SupabaseClientProvider.client
    private val userRepository = RepositoryProvider.getUserRepository()
    
    private val _state = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val state = _state.asStateFlow()

    val user: StateFlow<User?> = userRepository.userFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

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
