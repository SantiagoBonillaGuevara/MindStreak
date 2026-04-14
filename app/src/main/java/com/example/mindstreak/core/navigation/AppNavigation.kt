package com.example.mindstreak.core.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.mindstreak.core.components.BottomNav
import com.example.mindstreak.feature.achievements.AchievementsScreen
import com.example.mindstreak.feature.auth.AuthScreen
import com.example.mindstreak.feature.create_habit.CreateHabitScreen
import com.example.mindstreak.feature.habit_detail.HabitDetailScreen
import com.example.mindstreak.feature.home.AppViewModel
import com.example.mindstreak.feature.home.HomeScreen
import com.example.mindstreak.feature.notifications.NotificationsScreen
import com.example.mindstreak.feature.onboarding.OnboardingScreen
import com.example.mindstreak.feature.profile.ProfileScreen
import com.example.mindstreak.feature.social.SocialScreen
import com.example.mindstreak.feature.statistics.StatisticsScreen
import com.example.mindstreak.feature.streak.StreakScreen

// Equivalente a los path strings de React — sealed class evita typos
sealed class Screen(val route: String) {
    object Onboarding   : Screen("onboarding")
    object Auth         : Screen("auth")
    object Home         : Screen("home")
    object CreateHabit  : Screen("create_habit")
    object HabitDetail  : Screen("habit_detail/{habitId}") {
        fun createRoute(habitId: String) = "habit_detail/$habitId"
    }
    object Streak       : Screen("streak")
    object Statistics   : Screen("statistics")
    object Notifications: Screen("notifications")
    object Achievements : Screen("achievements")
    object Social       : Screen("social")
    object Profile      : Screen("profile")
}

// Rutas que muestran BottomNav — equivalente a BOTTOM_NAV_PATHS
private val BOTTOM_NAV_ROUTES = setOf(
    Screen.Home.route,
    Screen.Statistics.route,
    Screen.Streak.route,
    Screen.Achievements.route,
    Screen.Social.route,
    Screen.Profile.route,
)

@Composable
fun AppNavigation(appViewModel: AppViewModel) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    // Equivalente al Root layout — Surface reemplaza el phone frame
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Área de contenido — ocupa todo el espacio disponible
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.TopCenter,
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Onboarding.route,
                    // Equivalente a AnimatePresence mode="wait" con opacity+x
                    enterTransition = {
                        fadeIn(tween(200)) + slideInHorizontally(
                            tween(200)) { it / 10 }   // x: 24 → 0 (suavizado)
                    },
                    exitTransition = {
                        fadeOut(tween(200)) + slideOutHorizontally(
                            tween(200)) { -it / 10 }
                    },
                    popEnterTransition = {
                        fadeIn(tween(200)) + slideInHorizontally(
                            tween(200)) { -it / 10 }
                    },
                    popExitTransition = {
                        fadeOut(tween(200)) + slideOutHorizontally(
                            tween(200)) { it / 10 }
                    },
                ) {

                    // { index: true, element: <Navigate to="/onboarding" replace /> }
                    // → startDestination ya maneja esto arriba

                    composable(Screen.Onboarding.route) {
                        OnboardingScreen(
                            onFinish = {
                                navController.navigate(Screen.Auth.route) {
                                    popUpTo(Screen.Onboarding.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    /*composable(Screen.Auth.route) {
                        AuthScreen(
                            onLogin = {
                                navController.navigate(Screen.Home.route) {
                                    popUpTo(Screen.Auth.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    composable(Screen.Home.route) {
                        HomeScreen(
                            appViewModel = appViewModel,
                            navController = navController,
                        )
                    }

                    composable(Screen.CreateHabit.route) {
                        CreateHabitScreen(
                            appViewModel = appViewModel,
                            onBack = { navController.popBackStack() },
                            onCreated = { navController.popBackStack() },
                        )
                    }

                    // path: 'habit/:id' → "habit_detail/{habitId}"
                    composable(
                        route = Screen.HabitDetail.route,
                        arguments = listOf(
                            navArgument("habitId") { type = NavType.StringType }
                        ),
                    ) { backStackEntry ->
                        val habitId = backStackEntry.arguments?.getString("habitId") ?: return@composable
                        HabitDetailScreen(
                            habitId = habitId,
                            appViewModel = appViewModel,
                            onBack = { navController.popBackStack() },
                        )
                    }

                    composable(Screen.Streak.route) {
                        StreakScreen(appViewModel = appViewModel)
                    }

                    composable(Screen.Statistics.route) {
                        StatisticsScreen(appViewModel = appViewModel)
                    }

                    composable(Screen.Notifications.route) {
                        NotificationsScreen(
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.Achievements.route) {
                        AchievementsScreen(appViewModel = appViewModel)
                    }

                    composable(Screen.Social.route) {
                        SocialScreen(appViewModel = appViewModel)
                    }

                    composable(Screen.Profile.route) {
                        ProfileScreen(
                            appViewModel = appViewModel,
                            navController = navController,
                        )
                    }*/
                }
            }

            // BottomNav — solo en rutas principales
            // Equivalente a {showNav && <BottomNav />}
            AnimatedVisibility(
                visible = currentRoute in BOTTOM_NAV_ROUTES,
                enter = fadeIn(tween(200)) + slideInVertically(tween(200)) { it },
                exit  = fadeOut(tween(200)) + slideOutVertically(tween(200)) { it },
            ) {
                BottomNav(navController = navController)
            }

            // Home indicator — barra de sistema gestionada por Android
            // El equivalente es WindowCompat en Theme.kt (ya configurado)
        }
    }
}