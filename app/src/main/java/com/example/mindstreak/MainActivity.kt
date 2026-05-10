package com.example.mindstreak

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mindstreak.core.navigation.AppNavigation
import com.example.mindstreak.core.theme.HabitsAppTheme
import com.example.mindstreak.data.remote.SupabaseClientProvider
import com.example.mindstreak.feature.home.AppViewModel
import io.github.jan.supabase.auth.handleDeeplinks

class MainActivity : ComponentActivity() {
    private val TAG = "MainActivityAuth"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        SupabaseClientProvider.init(applicationContext)
        enableEdgeToEdge()
        
        handleIntent(intent)

        setContent {
            HabitsAppTheme {
                val appViewModel: AppViewModel = viewModel()
                AppNavigation(appViewModel = appViewModel)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val data = intent.data
        if (data != null) {
            Log.d(TAG, "URI recibida de Google: $data")
            try {
                SupabaseClientProvider.client.handleDeeplinks(intent) {
                    Log.d(TAG, "¡Sesión de Google importada con éxito!")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error procesando Deep Link: ${e.message}")
            }
        } else {
            Log.d(TAG, "No se recibió URI en el intent")
        }
    }
}
