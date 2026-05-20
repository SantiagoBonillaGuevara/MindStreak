package com.example.mindstreak.feature.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstreak.data.model.User
import com.example.mindstreak.data.repository.RepositoryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class EditProfileState {
    object Idle : EditProfileState()
    object Loading : EditProfileState()
    object Success : EditProfileState()
    data class Error(val message: String) : EditProfileState()
}

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = RepositoryProvider.getUserRepository()
    
    private val _state = MutableStateFlow<EditProfileState>(EditProfileState.Idle)
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.userFlow.collect {
                _user.value = it
            }
        }
    }

    fun updateProfile(name: String, avatarEmoji: String) {
        val currentUser = _user.value ?: return
        _state.value = EditProfileState.Loading
        
        viewModelScope.launch {
            try {
                val updatedUser = currentUser.copy(name = name, avatarEmoji = avatarEmoji)
                userRepository.updateProfile(updatedUser)
                userRepository.refresh()
                _state.value = EditProfileState.Success
            } catch (e: Exception) {
                _state.value = EditProfileState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun resetState() {
        _state.value = EditProfileState.Idle
    }
}
