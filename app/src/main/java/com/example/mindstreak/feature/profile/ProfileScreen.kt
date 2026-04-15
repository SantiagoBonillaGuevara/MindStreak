package com.example.mindstreak.feature.profile

import androidx.compose.animation.core.*
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
import com.example.mindstreak.data.mock.MockData

@Composable
fun ProfileScreen(onNavigate: (String) -> Unit) {
    val user = MockData.USER
    var darkMode by remember { mutableStateOf(true) }
    var notifications by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A14))
            .verticalScroll(rememberScrollState())
    ) {
        // --- Header ---
        Text(
            text = "Profile",
            color = Color(0xFFF0F0FF),
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
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
                color = Color(0xFF7C6EFF),
                value = darkMode,
                onValueChange = { darkMode = it }
            )
            SettingsToggleItem(
                icon = Icons.Default.Notifications,
                label = "Notifications",
                sub = if (notifications) "Enabled" else "Disabled",
                color = Color(0xFFFF6B35),
                value = notifications,
                onValueChange = { notifications = it }
            )
        }

        SettingsSection(title = "Account") {
            SettingsClickItem(Icons.Default.Shield, "Privacy & Data", Color(0xFF4ECDC4))
            SettingsClickItem(Icons.Default.Star, "MindStreak Pro", Color(0xFFFFD166), badge = "Upgrade")
            SettingsClickItem(Icons.Default.Group, "Refer a Friend", Color(0xFFFF8FAB), badge = "+50 XP")
        }

        // --- Logout ---
        Button(
            onClick = { onNavigate("auth") },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4757).copy(alpha = 0.08f)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFFF4757).copy(alpha = 0.2f))
        ) {
            Icon(Icons.Default.Logout, null, tint = Color(0xFFFF4757), modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Log Out", color = Color(0xFFFF4757), fontWeight = FontWeight.Bold)
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
            .background(Brush.linearGradient(listOf(Color(0xFF1A1330), Color(0xFF0F0F1E))))
            .border(1.dp, Color(0xFF2A2A45), RoundedCornerShape(28.dp))
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
                            .background(Brush.linearGradient(listOf(Color(0xFF7C6EFF), Color(0xFF4ECDC4)))),
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
                            .background(Color(0xFFFFD166), CircleShape)
                            .border(2.dp, Color(0xFF0A0A14), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(user.level.toString(), color = Color.Black, fontSize = 10.sp, fontWeight = FontWeight.Black)
                    }
                }

                Column(Modifier.padding(start = 16.dp).weight(1f)) {
                    Text(user.name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    Text("@${user.username}", color = Color(0xFF7C6EFF), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    Text(user.university, color = Color(0xFF5A5A7A), fontSize = 11.sp)
                }

                Surface(
                    color = Color(0xFF7C6EFF).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF7C6EFF).copy(alpha = 0.3f))
                ) {
                    Text("Edit", color = Color(0xFF7C6EFF), modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            // XP Bar
            Column(Modifier.padding(top = 20.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Level ${user.level} • Habit Architect", color = Color(0xFF6B6B8A), fontSize = 11.sp)
                    Text("${user.xp} / ${user.nextLevelXp} XP", color = Color(0xFFFFD166), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { xpPercent },
                    modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                    color = Color(0xFFFFD166), // Podrías usar un brush aquí para el degradado
                    trackColor = Color(0xFF2A2A45)
                )
            }

            // Member Since
            Row(Modifier.padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("Member since ${user.joinDate}", color = Color(0xFF4A4A6A), fontSize = 11.sp)
                Spacer(Modifier.width(8.dp))
                Surface(
                    color = Color(0xFF4ECDC4).copy(alpha = 0.15f),
                    shape = CircleShape,
                    border = BorderStroke(1.dp, Color(0xFF4ECDC4).copy(alpha = 0.3f))
                ) {
                    Text("✓ Verified Student", color = Color(0xFF4ECDC4), fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                }
            }
        }
    }
}

@Composable
fun QuickNavButton(emoji: String, label: String, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF13131F))
            .border(1.dp, Color(0xFF2A2A45), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 20.sp)
        Text(label, color = Color(0xFF8888A8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFF2A2A45), RoundedCornerShape(24.dp))
    ) {
        Box(Modifier.fillMaxWidth().background(Color(0xFF0F0F1A)).padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(title.uppercase(), color = Color(0xFF8888A8), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }
        content()
    }
}

@Composable
fun SettingsToggleItem(icon: ImageVector, label: String, sub: String, color: Color, value: Boolean, onValueChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color(0xFF13131F)).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(36.dp).background(color.copy(0.15f), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
        }
        Column(Modifier.padding(start = 12.dp).weight(1f)) {
            Text(label, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(sub, color = Color(0xFF5A5A7A), fontSize = 11.sp)
        }
        Switch(
            checked = value,
            onCheckedChange = onValueChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF7C6EFF),
                uncheckedThumbColor = Color(0xFF8888A8),
                uncheckedTrackColor = Color(0xFF2A2A45)
            )
        )
    }
}

@Composable
fun SettingsClickItem(icon: ImageVector, label: String, color: Color, badge: String? = null) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color(0xFF13131F)).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(36.dp).background(color.copy(0.15f), RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
        }
        Text(label, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 12.dp).weight(1f))

        if (badge != null) {
            Text(
                badge,
                modifier = Modifier.padding(end = 8.dp).background(color.copy(0.15f), CircleShape).padding(horizontal = 8.dp, vertical = 2.dp),
                color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold
            )
        }
        Icon(Icons.Default.ChevronRight, null, tint = Color(0xFF4A4A6A), modifier = Modifier.size(16.dp))
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
    val colors = listOf(Color(0xFFFF6B35), Color(0xFFFFD166), Color(0xFF7C6EFF), Color(0xFF4ECDC4))

    Row(Modifier.padding(horizontal = 20.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        stats.forEachIndexed { i, stat ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF13131F), RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFF2A2A45), RoundedCornerShape(16.dp))
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stat.second, color = colors[i], fontSize = 14.sp, fontWeight = FontWeight.Black)
                Text(stat.first.uppercase(), color = Color(0xFF4A4A6A), fontSize = 8.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}