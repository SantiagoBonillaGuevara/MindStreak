package com.example.mindstreak.data.repository

import com.example.mindstreak.data.model.User
import com.example.mindstreak.data.remote.SupabaseClientProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import io.github.jan.supabase.auth.auth

class SupabaseUserRepository : UserRepository {
    private val client = SupabaseClientProvider.client

    override val userFlow: Flow<User?> = flow {
        val userId = client.auth.currentSessionOrNull()?.user?.id
        if (userId != null) {
            emit(getProfile(userId))
        } else {
            emit(null)
        }
    }

    override suspend fun getProfile(userId: String): User? {
        return null 
    }

    override suspend fun updateProfile(user: User) {
    }
}
