package com.example.mindstreak.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(
    val route: String,
    val label: String,
    val icon: ImageVector?,
    val emoji: String? = null,
    val activeEmoji: String? = null,
) {
    object Home : NavItem("home", "Home", Icons.Default.Home)
    object Stats : NavItem("statistics", "Stats", Icons.Default.BarChart)
    object Streak : NavItem("streak", "Streak", icon = null, emoji = "🔥", activeEmoji = "🔥")
    object Achievements : NavItem("achievements", "Awards", Icons.Default.EmojiEvents)
    object Profile : NavItem("profile", "Profile", Icons.Default.Person)
}

val NAV_ITEMS = listOf(
    NavItem.Home,
    NavItem.Stats,
    NavItem.Streak,
    NavItem.Achievements,
    NavItem.Profile,
)