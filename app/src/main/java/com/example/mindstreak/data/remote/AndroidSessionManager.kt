package com.example.mindstreak.data.remote

import android.content.Context
import io.github.jan.supabase.auth.SessionManager
import io.github.jan.supabase.auth.user.UserSession
import kotlinx.serialization.json.Json
import androidx.core.content.edit

class AndroidSessionManager(context: Context) : SessionManager {
    private val prefs = context.getSharedPreferences("mindstreak_auth", Context.MODE_PRIVATE)

    override suspend fun saveSession(session: UserSession) {
        prefs.edit { putString("session", Json.encodeToString(session)) }
    }

    override suspend fun loadSession(): UserSession {
        val json = prefs.getString("session", null) ?: throw IllegalStateException("No session found")
        return try {
            Json.decodeFromString<UserSession>(json)
        } catch (e: Exception) {
            throw IllegalStateException("Invalid session format", e)
        }
    }

    override suspend fun deleteSession() {
        prefs.edit { remove("session") }
    }
}