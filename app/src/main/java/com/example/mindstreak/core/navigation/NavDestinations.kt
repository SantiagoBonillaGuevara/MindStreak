package com.example.mindstreak.core.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Auth : Screen("auth")
    object Home : Screen("home")
    object CreateHabit : Screen("create_habit")
    object HabitDetail : Screen("habit_detail/{habitId}") {
        fun createRoute(habitId: String) = "habit_detail/$habitId"
    }

    object Streak : Screen("streak")
    object Statistics : Screen("statistics")
    object Notifications : Screen("notifications")
    object Achievements : Screen("achievements")
    object Social : Screen("social")
    object Profile : Screen("profile")
}

// Rutas que muestran BottomNav — equivalente a BOTTOM_NAV_PATHS
val BOTTOM_NAV_ROUTES = setOf(
    Screen.Home.route,
    Screen.Statistics.route,
    Screen.Streak.route,
    Screen.Achievements.route,
    Screen.Social.route,
    Screen.Profile.route,
)