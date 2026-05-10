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
        val json = prefs.getString("session", null)
        return json?.let { Json.decodeFromString<UserSession>(it) }
            ?: throw IllegalStateException("No session found")
    }

    override suspend fun deleteSession() {
        prefs.edit { remove("session") }
    }
}