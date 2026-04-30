package com.example.mindstreak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mindstreak.core.navigation.AppNavigation
import com.example.mindstreak.core.theme.HabitsAppTheme
import com.example.mindstreak.feature.home.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Equivalente a -webkit-tap-highlight-color en index.css
        setContent {
            HabitsAppTheme {
                // ViewModel compartido — equivalente al AppProvider que envuelve todo
                val appViewModel: AppViewModel = viewModel()
                AppNavigation(appViewModel = appViewModel)
            }
        }
    }
}