package com.example.mindstreak.core.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mindstreak.core.theme.HabitPurple

// sealed class para las rutas de navegación
sealed class NavItem(
    val route: String,
    val label: String,
    val icon: ImageVector?,
    val emoji: String? = null,
    val activeEmoji: String? = null,
) {
    object Home         : NavItem("home",         "Home",    Icons.Default.Home)
    object Stats        : NavItem("statistics",   "Stats",   Icons.Default.BarChart)
    object Streak       : NavItem("streak",       "Streak",  icon = null, emoji = "🔥", activeEmoji = "🔥")
    object Achievements : NavItem("achievements",  "Awards",  Icons.Default.EmojiEvents)
    object Profile      : NavItem("profile",      "Profile", Icons.Default.Person)
}

private val NAV_ITEMS = listOf(
    NavItem.Home,
    NavItem.Stats,
    NavItem.Streak,
    NavItem.Achievements,
    NavItem.Profile,
)

@Composable
fun BottomNav(navController: NavController) {
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    val navBackground = MaterialTheme.colorScheme.surface
    val navBorder = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)

    Column {
        HorizontalDivider(color = navBorder, thickness = 1.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(navBackground)
                .padding(top = 8.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NAV_ITEMS.forEach { item ->
                NavItemButton(
                    item = item,
                    active = currentRoute == item.route,
                    onClick = {
                        if (currentRoute != item.route) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun NavItemButton(
    item: NavItem,
    active: Boolean,
    onClick: () -> Unit,
) {
    val activeColor = HabitPurple
    val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant
    val activeBg = activeColor.copy(alpha = 0.12f)

    val contentColor by animateColorAsState(
        targetValue = if (active) activeColor else inactiveColor,
        animationSpec = tween(200),
        label = "navColor_${item.route}",
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (active) activeBg else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        if (item.emoji != null) {
            Text(
                text = item.emoji,
                fontSize = 20.sp,
                lineHeight = 20.sp,
            )
        } else if (item.icon != null) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = contentColor,
                modifier = Modifier.size(if (active) 20.dp else 18.dp),
            )
        }

        Text(
            text = item.label,
            color = contentColor,
            fontSize = 10.sp,
            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
            letterSpacing = 0.02.sp,
        )
    }
}