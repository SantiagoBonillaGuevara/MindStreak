package com.example.mindstreak.data.remote

import android.content.Context
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

object SupabaseClientProvider {
    private var _client: io.github.jan.supabase.SupabaseClient? = null
    val client: io.github.jan.supabase.SupabaseClient
        get() = _client ?: throw IllegalStateException("SupabaseClient not initialized.")

    fun init(context: Context) {
        if (_client == null) {
            _client = createSupabaseClient(
                supabaseUrl = SupabaseConfig.URL,
                supabaseKey = SupabaseConfig.ANON_KEY
            ) {
                install(Postgrest)
                install(Auth) {
                    sessionManager = AndroidSessionManager(context)
                    alwaysAutoRefresh = true
                    // CONFIGURACIÓN CRÍTICA PARA DEEP LINKS
                    scheme = "mindstreak"
                    host = "login-callback"
                }
                install(Realtime)
            }
        }
    }
}
