package com.example.mindstreak.data.repository

import com.example.mindstreak.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userFlow: Flow<User?>
    suspend fun getProfile(userId: String): User?
    suspend fun updateProfile(user: User)
    fun refresh()
}
