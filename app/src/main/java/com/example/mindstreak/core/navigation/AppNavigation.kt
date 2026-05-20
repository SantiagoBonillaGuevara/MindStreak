package com.example.mindstreak.core.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.mindstreak.R
import com.example.mindstreak.core.components.navigation.NavBottom
import com.example.mindstreak.feature.home.AppViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavigation(appViewModel: AppViewModel) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route
    val uiState by appViewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
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
                                if (route == Screen.Rewards.route && uiState.user?.isInstitutional != true) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            context.getString(R.string.institutional_restricted_msg)
                                        )
                                    }
                                } else {
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
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