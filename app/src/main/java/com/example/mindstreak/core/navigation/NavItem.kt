package com.example.mindstreak.core.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mindstreak.R

sealed class NavItem(
    val route: String,
    @param:StringRes val labelRes: Int,
    val icon: ImageVector?,
    val emoji: String? = null,
) {
    object Home : NavItem("home", R.string.nav_home, Icons.Default.Home)
    object Stats : NavItem("statistics", R.string.nav_stats, Icons.Default.BarChart)
    object Streak : NavItem("streak", R.string.nav_streak, icon = null, emoji = "🔥")
    object Rewards : NavItem("rewards", R.string.nav_rewards, Icons.Default.Redeem)
    object Profile : NavItem("profile", R.string.nav_profile, Icons.Default.Person)
}

val NAV_ITEMS = listOf(
    NavItem.Home,
    NavItem.Stats,
    NavItem.Streak,
    NavItem.Rewards,
    NavItem.Profile,
)
