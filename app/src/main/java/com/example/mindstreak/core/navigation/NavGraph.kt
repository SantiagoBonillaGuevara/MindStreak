package com.example.mindstreak.core.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
        AuthScreen(onLogin = {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Auth.route) { inclusive = true }
            }
        })
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
    // path: 'habit/:id' → "habit_detail/{habitId}"
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
        StatisticsScreen(appViewModel = appViewModel, navController = navController)
    }

    composable(Screen.Notifications.route) {
        NotificationsScreen(onBack = { navController.popBackStack() }, appViewModel = appViewModel)
    }

    composable(Screen.Achievements.route) { AchievementsScreen(appViewModel = appViewModel) }

    composable(Screen.Social.route) { SocialScreen(/*appViewModel = appViewModel*/) }

    composable(Screen.Profile.route) {
        ProfileScreen(onNavigate = { route ->
            navController.navigate(route)
        })
    }
}