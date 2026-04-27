package com.example.mindstreak.feature.social

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.theme.*
import com.example.mindstreak.data.mock.MockData

// Colores del podio
@Composable
fun getRankColors() = listOf(HabitYellow, RankSilver, RankBronze)
val RANK_LABELS = listOf("🥇", "🥈", "🥉")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialScreen() {
    var selectedTab by remember { mutableStateOf("Leaderboard") }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- Header ---
        Row(
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Social",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge
            )

            Surface(
                color = HabitPurple.copy(alpha = 0.15f),
                shape = RoundedCornerShape(12.dp),
                onClick = { /* Invite logic */ }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.PersonAdd, null, tint = HabitPurple, modifier = Modifier.size(14.dp))
                    Text(
                        "Invite",
                        color = HabitPurple,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        // --- Search Bar ---
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search friends...", color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium) },
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(54.dp),
            leadingIcon = { Icon(Icons.Default.Search, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp)) },
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                // Color de fondo
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,

                // Color del texto
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,

                // Colores del borde (Indicator)
                focusedIndicatorColor = HabitPurple,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,

                // Cursor
                cursorColor = HabitPurple,

                // Opcional: Quitar la línea base si no la quieres
                disabledIndicatorColor = Color.Transparent,
            )
        )

        // --- Tab Picker ---
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
                .padding(4.dp)
        ) {
            listOf("Leaderboard", "Activity", "Groups").forEach { t ->
                val isSelected = selectedTab == t
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) HabitPurple else Color.Transparent)
                        .clickable { selectedTab = t }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        t,
                        color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        // --- Contenido Animado ---
        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = {
                (fadeIn() + slideInVertically { it / 2 }).togetherWith(fadeOut())
            }
        ) { currentTab ->
            when (currentTab) {
                "Leaderboard" -> LeaderboardContent()
                "Activity" -> ActivityContent()
                "Groups" -> GroupsContent()
            }
        }
    }
}

@Composable
fun LeaderboardContent() {
    val friends = MockData.FRIENDS // Asumiendo que tienes una lista de amigos

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
    ) {
        // Banner Challenge
        item {
            WeeklyChallengeBanner()
            Spacer(Modifier.height(20.dp))
        }

        // Podium (Top 3)
        item {
            PodiumRow(friends)
            Spacer(Modifier.height(24.dp))
        }

        // Lista completa
        itemsIndexed(friends) { index, friend ->
            FriendRankItem(index, friend)
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun PodiumRow(friends: List<com.example.mindstreak.data.model.Friend>) {
    // Reordenar para podio: [2nd, 1st, 3rd]
    val podiumOrder = listOf(friends[1], friends[0], friends[2])
    val heights = listOf(70.dp, 100.dp, 60.dp)
    val actualRanks = listOf(1, 0, 2) // Indices de RANK_COLORS
    val rankColors = getRankColors()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        podiumOrder.forEachIndexed { index, friend ->
            val rankIndex = actualRanks[index]
            val isFirst = rankIndex == 0

            Column(
                modifier = Modifier.weight(if (isFirst) 1.2f else 1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(RANK_LABELS[rankIndex], fontSize = if (isFirst) 20.sp else 16.sp)
                Text(friend.avatarEmoji, fontSize = if (isFirst) 36.sp else 28.sp)
                Text(
                    friend.name.split(" ")[0],
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = if (isFirst) 12.sp else 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("🔥${friend.streak}", color = HabitOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(8.dp))

                // Barra del podio
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(heights[index])
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(rankColors[rankIndex].copy(0.3f), rankColors[rankIndex].copy(0.1f))
                            )
                        )
                        .border(1.dp, rankColors[rankIndex].copy(0.4f), RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text((rankIndex + 1).toString(), color = rankColors[rankIndex], fontWeight = FontWeight.Black, fontSize = 20.sp)
                }
            }
        }
    }
}

@Composable
fun FriendRankItem(index: Int, friend: com.example.mindstreak.data.model.Friend) {
    val isTop3 = index < 3
    val isYou = friend.isYou
    val rankColors = getRankColors()

    Surface(
        color = if (isYou) HabitPurple.copy(0.1f) else MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, if (isYou) HabitPurple.copy(0.4f) else MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank Circle
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        if (isTop3) rankColors[index].copy(0.2f) else MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (isTop3) RANK_LABELS[index] else (index + 1).toString(),
                    color = if (isTop3) rankColors[index] else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Text(friend.avatarEmoji, fontSize = 24.sp, modifier = Modifier.padding(horizontal = 12.dp))

            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(friend.name, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    if (isYou) {
                        Text(
                            "YOU",
                            modifier = Modifier.padding(start = 6.dp).background(HabitPurple.copy(0.2f), CircleShape).padding(horizontal = 6.dp, vertical = 2.dp),
                            color = HabitPurple, fontSize = 9.sp, fontWeight = FontWeight.Black
                        )
                    }
                }
                Text("Lvl ${friend.level} · ${friend.weeklyHabits} habits/wk", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("🔥 ${friend.streak}", color = HabitOrange, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("${friend.points} pts", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp)
            }
        }
    }
}

@Composable
fun WeeklyChallengeBanner() {
    Surface(
        color = Color.Transparent,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, HabitPurple.copy(0.25f))
    ) {
        Row(
            modifier = Modifier
                .background(
                    Brush.linearGradient(listOf(HabitPurple.copy(0.15f), HabitTeal.copy(0.15f)))
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("⚔️", fontSize = 24.sp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("Weekly Challenge", color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("Most habits logged wins!", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            }
            Text("3 days left", color = HabitPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}
// Data Classes para los items
data class ActivityItem(val userName: String, val userEmoji: String, val action: String, val target: String, val time: String, val targetEmoji: String)
data class GroupItem(val name: String, val members: Int, val streak: Int, val emoji: String, val color: Color)
// --- PESTAÑA DE ACTIVIDAD ---
@Composable
fun ActivityContent() {
    val activityItems = listOf(
        ActivityItem("Emma Wilson", "👩‍🎨", "completed", "Morning Run", "8 min ago", "🏃"),
        ActivityItem("Sarah Kim", "👩‍💻", "hit streak", "18 days!", "23 min ago", "🔥"),
        ActivityItem("Mike Torres", "👨‍🎓", "unlocked", "Iron Will badge", "1h ago", "💪"),
        ActivityItem("Emma Wilson", "👩‍🎨", "completed", "Meditate", "2h ago", "🧘")
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(activityItems) { item ->
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(item.userEmoji, fontSize = 26.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append(item.userName)
                                }
                                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                                    append(" ${item.action} ")
                                }
                                withStyle(SpanStyle(color = HabitPurple, fontWeight = FontWeight.Bold)) {
                                    append(item.target)
                                }
                                append(" ${item.targetEmoji}")
                            },
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                        Text(
                            text = item.time,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

// --- PESTAÑA DE GRUPOS ---
@Composable
fun GroupsContent() {
    val groups = listOf(
        GroupItem("MIT Wellness Club", 24, 14, "🎓", HabitPurple),
        GroupItem("Morning Hustlers", 12, 7, "☀️", HabitOrange),
        GroupItem("Sleep Scientists", 8, 21, "💤", HabitBlue)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(groups) { group ->
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icono de grupo con fondo coloreado
                    Surface(
                        modifier = Modifier.size(44.dp),
                        color = group.color.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(group.emoji, fontSize = 22.sp)
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(group.name, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = "${group.members} members · 🔥 ${group.streak} day group streak",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Botón "+ Create a Group"
        item {
            OutlinedButton(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant), // Borde dashed no es nativo fácil, usamos sólido
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
            ) {
                Text("+ Create a Group", fontSize = 14.sp)
            }
        }
    }

}