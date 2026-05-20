package com.example.mindstreak.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mindstreak.feature.achievements.AchievementsScreen
import com.example.mindstreak.feature.auth.AuthScreen
import com.example.mindstreak.feature.auth.AuthViewModel
import com.example.mindstreak.feature.auth.ForgotPasswordScreen
import com.example.mindstreak.feature.create_habit.CreateHabitScreen
import com.example.mindstreak.feature.habit_detail.HabitDetailScreen
import com.example.mindstreak.feature.home.AppViewModel
import com.example.mindstreak.feature.home.HomeScreen
import com.example.mindstreak.feature.notifications.NotificationsScreen
import com.example.mindstreak.feature.onboarding.OnboardingScreen
import com.example.mindstreak.feature.profile.PrivacyScreen
import com.example.mindstreak.feature.profile.ProfileScreen
import com.example.mindstreak.feature.profile.EditProfileScreen
import com.example.mindstreak.feature.rewards.RewardsScreen
import com.example.mindstreak.feature.statistics.StatisticsScreen
import com.example.mindstreak.feature.streak.StreakScreen

@RequiresApi(Build.VERSION_CODES.S)
fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
    appViewModel: AppViewModel
) {
    composable(Screen.Onboarding.route) {
        OnboardingScreen(onFinish = {
            navController.navigate(Screen.Auth.route) {
                popUpTo(Screen.Onboarding.route) { inclusive = true }
            }
        })
    }

    composable(Screen.Auth.route) {
        val authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
        AuthScreen(
            viewModel = authViewModel,
            onLogin = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Auth.route) { inclusive = true }
                }
            },
            onForgotPassword = {
                navController.navigate(Screen.ForgotPassword.route)
            }
        )
    }

    composable(Screen.ForgotPassword.route) {
        val authViewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
        ForgotPasswordScreen(
            onBack = { navController.popBackStack() },
            viewModel = authViewModel
        )
    }

    composable(Screen.Home.route) {
        HomeScreen(appViewModel = appViewModel, navController = navController)
    }

    composable(Screen.CreateHabit.route) {
        CreateHabitScreen(
            appViewModel = appViewModel,
            onBack = { navController.popBackStack() },
            onCreated = { navController.popBackStack() },
        )
    }

    composable(
        route = Screen.HabitDetail.route,
        arguments = listOf(navArgument("habitId") { type = NavType.StringType }),
    ) { backStackEntry ->
        val habitId = backStackEntry.arguments?.getString("habitId") ?: return@composable
        HabitDetailScreen(
            habitId = habitId,
            appViewModel = appViewModel,
            onBack = { navController.popBackStack() },
        )
    }

    composable(Screen.Streak.route) {
        StreakScreen(appViewModel = appViewModel, navController = navController)
    }

    composable(Screen.Statistics.route) {
        StatisticsScreen(appViewModel = appViewModel)
    }

    composable(Screen.Notifications.route) {
        NotificationsScreen(onBack = { navController.popBackStack() }, appViewModel = appViewModel)
    }

    composable(Screen.Achievements.route) { AchievementsScreen(appViewModel = appViewModel) }

    composable(Screen.Rewards.route) { RewardsScreen(appViewModel = appViewModel) }

    composable(Screen.Privacy.route) {
        PrivacyScreen(onBack = { navController.popBackStack() })
    }

    composable(Screen.EditProfile.route) {
        EditProfileScreen(onBack = { navController.popBackStack() })
    }

    composable(Screen.Profile.route) {
        ProfileScreen(
            onNavigate = { route ->
                navController.navigate(route) {
                    // Solo limpiamos si vamos a otra pestaña principal
                    if (route in BOTTOM_NAV_ROUTES) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            onNavigateToNotifications = {
                // Navegación estándar que añade Notifications al stack encima de Profile
                navController.navigate(Screen.Notifications.route)
            },
            appViewModel = appViewModel
        )
    }
}
