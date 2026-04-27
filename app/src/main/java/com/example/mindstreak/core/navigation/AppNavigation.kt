package com.example.mindstreak.core.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.mindstreak.core.components.navigation.NavBottom
import com.example.mindstreak.feature.home.AppViewModel

@Composable
fun AppNavigation(appViewModel: AppViewModel) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visible = currentRoute in BOTTOM_NAV_ROUTES,
                    enter = fadeIn(tween(200)) + slideInVertically(tween(200)) { it },
                    exit = fadeOut(tween(200)) + slideOutVertically(tween(200)) { it }
                ) {
                    NavBottom(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            if (currentRoute != route) {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Onboarding.route,
                modifier = Modifier.padding(innerPadding), // Evita que el contenido quede detrás de la NavBottom
                enterTransition = { defaultEnter() },
                exitTransition = { defaultExit() },
                popEnterTransition = { defaultPopEnter() },
                popExitTransition = { defaultPopExit() }
            ) {
                // Inyectamos el grafo
                mainGraph(
                    navController = navController,
                    appViewModel = appViewModel
                )
            }
        }

    }
}

// Helper functions para mantener limpio el NavHost
private fun defaultEnter() = fadeIn(tween(200)) + slideInHorizontally(tween(200)) { it / 10 }
private fun defaultExit() = fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { -it / 10 }
private fun defaultPopEnter() = fadeIn(tween(200)) + slideInHorizontally(tween(200)) { -it / 10 }
private fun defaultPopExit() = fadeOut(tween(200)) + slideOutHorizontally(tween(200)) { it / 10 }