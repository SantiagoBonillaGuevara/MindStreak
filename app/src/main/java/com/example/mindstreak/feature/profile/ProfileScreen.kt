package com.example.mindstreak.feature.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.components.QuickNavButton
import com.example.mindstreak.core.components.SettingsClickItem
import com.example.mindstreak.core.components.SettingsSection
import com.example.mindstreak.core.components.SettingsToggleItem
import com.example.mindstreak.data.mock.MockData
import com.example.mindstreak.core.theme.*

@Composable
fun ProfileScreen(onNavigate: (String) -> Unit) {
    val user = MockData.USER
    var darkMode by remember { mutableStateOf(true) }
    var notifications by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // --- Header ---
        Text(
            text = "Profile",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        )

        // --- Profile Hero Card ---
        ProfileHeroCard(user)

        // --- Stats Grid ---
        StatsGrid(user)

        // --- Quick Nav ---
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickNavButton("🏆", "Achievements", Modifier.weight(1f)) { onNavigate("achievements") }
            QuickNavButton("👥", "Social", Modifier.weight(1f)) { onNavigate("social") }
            QuickNavButton("🔔", "Reminders", Modifier.weight(1f)) { onNavigate("notifications") }
        }

        // --- Settings Sections ---
        SettingsSection(title = "Preferences") {
            SettingsToggleItem(
                icon = Icons.Default.DarkMode,
                label = "Dark Mode",
                sub = "Always on",
                color = HabitPurple,
                value = darkMode,
                onValueChange = { darkMode = it }
            )
            SettingsToggleItem(
                icon = Icons.Default.Notifications,
                label = "Notifications",
                sub = if (notifications) "Enabled" else "Disabled",
                color = HabitOrange,
                value = notifications,
                onValueChange = { notifications = it }
            )
        }

        SettingsSection(title = "Account") {
            SettingsClickItem(Icons.Default.Shield, "Privacy & Data", HabitTeal)
            SettingsClickItem(Icons.Default.Star, "MindStreak Pro", HabitYellow, badge = "Upgrade")
            SettingsClickItem(Icons.Default.Group, "Refer a Friend", HabitPink, badge = "+50 XP")
        }

        // --- Logout ---
        Button(
            onClick = { onNavigate("auth") },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.08f)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
        ) {
            Icon(Icons.Default.Logout, null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Log Out", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ProfileHeroCard(user: com.example.mindstreak.data.model.User) {
    val xpPercent = user.xp.toFloat() / user.nextLevelXp.toFloat()

    Box(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.linearGradient(listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surface)))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(28.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar con nivel
                Box {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .background(Brush.linearGradient(listOf(HabitPurple, HabitTeal))),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(user.avatarEmoji, fontSize = 32.sp)
                    }
                    // Badge de nivel
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 4.dp, y = 4.dp)
                            .size(24.dp)
                            .background(HabitYellow, CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.background, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(user.level.toString(), color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Black)
                    }
                }

                Column(Modifier.padding(start = 16.dp).weight(1f)) {
                    Text(user.name, color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp))
                    Text("@${user.username}", color = HabitPurple, style = MaterialTheme.typography.bodyMedium)
                    Text(user.university, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal))
                }

                Surface(
                    color = HabitPurple.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, HabitPurple.copy(alpha = 0.3f))
                ) {
                    Text("Edit", color = HabitPurple, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // XP Bar
            Column(Modifier.padding(top = 20.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Level ${user.level} • Habit Architect", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    Text("${user.xp} / ${user.nextLevelXp} XP", color = HabitYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { xpPercent },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                    color = HabitYellow, // Podrías usar un brush aquí para el degradado
                    trackColor = MaterialTheme.colorScheme.outlineVariant
                )
            }

            // Member Since
            Row(Modifier.padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Member since ${user.joinDate}", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                Spacer(Modifier.width(8.dp))
                Surface(
                    color = HabitTeal.copy(alpha = 0.15f),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, HabitTeal.copy(alpha = 0.3f))
                ) {
                    Text("✓ Verified Student", color = HabitTeal, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                }
            }
        }
    }
}


@Composable
fun StatsGrid(user: com.example.mindstreak.data.model.User) {
    val stats = listOf(
        "Streak" to "${user.totalStreak}🔥",
        "Best" to "${user.bestStreak}",
        "Habits" to "${user.totalHabitsCompleted}",
        "Level" to "${user.level}"
    )
    val colors = listOf(HabitOrange, HabitYellow, HabitPurple, HabitTeal)

    Row(Modifier.padding(horizontal = 20.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        stats.forEachIndexed { i, stat ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stat.second, color = colors[i], fontSize = 14.sp, fontWeight = FontWeight.Black)
                Text(stat.first.uppercase(), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}