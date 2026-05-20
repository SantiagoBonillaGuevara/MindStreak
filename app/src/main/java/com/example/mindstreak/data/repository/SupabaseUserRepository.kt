package com.example.mindstreak.data.repository

import com.example.mindstreak.data.model.User
import com.example.mindstreak.data.remote.SupabaseClientProvider
import com.example.mindstreak.data.remote.dto.UserDto
import com.example.mindstreak.data.remote.dto.toDomain
import com.example.mindstreak.data.remote.dto.toDto
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.onStart

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.combine

class SupabaseUserRepository : UserRepository {
    private val client by lazy { SupabaseClientProvider.client }
    private val tag = "SupabaseUserRepo"
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1).apply { tryEmit(Unit) }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val userFlow: Flow<User?> = combine(
        client.auth.sessionStatus,
        refreshTrigger
    ) { status, _ -> status }
        .onStart { Log.d(tag, "userFlow collection started") }
        .flatMapLatest { status ->
            Log.d(tag, "Auth status changed or refresh triggered: $status")
            if (status is SessionStatus.Authenticated) {
                flow {
                    val userId = status.session.user?.id
                    if (userId != null) {
                        Log.d(tag, "Fetching profile for $userId")
                        val profile = getProfile(userId)
                        Log.d(tag, "Profile result: ${profile?.name}")
                        emit(profile)
                    } else {
                        Log.e(tag, "User ID is null in authenticated session")
                        emit(null)
                    }
                }
            } else {
                Log.d(tag, "Not authenticated, emitting null user")
                flowOf(null)
            }
        }

    override fun refresh() {
        Log.d(tag, "Refreshing user profile manually")
        refreshTrigger.tryEmit(Unit)
    }

    override suspend fun getProfile(userId: String): User? {
        Log.d(tag, "Calling getProfile for $userId")
        return try {
            val result = client.postgrest["profiles"]
                .select(io.github.jan.supabase.postgrest.query.Columns.raw("*, levels(title)")) {
                    filter {
                        eq("id", userId)
                    }
                }
                .decodeSingleOrNull<UserDto>()
            Log.d(tag, "Raw profile result: $result")
            result?.toDomain()
        } catch (e: Exception) {
            Log.e(tag, "Error fetching profile: ${e.message}", e)
            null
        }
    }

    override suspend fun updateProfile(user: User) {
        try {
            client.postgrest["profiles"].update(user.toDto()) {
                filter {
                    eq("id", user.id)
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "Error updating profile: ${e.message}")
        }
    }
}
