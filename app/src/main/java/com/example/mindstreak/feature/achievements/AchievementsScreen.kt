package com.example.mindstreak.feature.achievements

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.components.ProgressRing
import com.example.mindstreak.core.theme.*
import com.example.mindstreak.data.model.Achievement
import com.example.mindstreak.data.model.Rarity
import com.example.mindstreak.feature.home.AppViewModel

// Configuración de UI basada en tu Enum Rarity
data class RarityUi(val color: Color, val label: String, val bg: Color)

@Composable
fun getRarityConfig(): Map<Rarity, RarityUi> {
    return mapOf(
        Rarity.COMMON to RarityUi(MaterialTheme.colorScheme.onSurfaceVariant, "Common", MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)),
        Rarity.RARE to RarityUi(HabitTeal, "Rare", HabitTeal.copy(alpha = 0.12f)),
        Rarity.EPIC to RarityUi(HabitPurple, "Epic", HabitPurple.copy(alpha = 0.12f)),
        Rarity.LEGENDARY to RarityUi(HabitYellow, "Legendary", HabitYellow.copy(alpha = 0.12f))
    )
}

@Composable
fun AchievementsScreen(appViewModel: AppViewModel) {
    val uiState by appViewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf("All") }
    var highlightedId by remember { mutableStateOf<String?>(null) }

    val earned = remember(uiState.achievements) { uiState.achievements.filter { it.earned } }
    val locked = remember(uiState.achievements) { uiState.achievements.filter { !it.earned } }
    val displayed = when (selectedTab) {
        "Earned" -> earned
        "Locked" -> locked
        else -> uiState.achievements
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // --- Header ---
            item {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        "Achievements",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        "Level up your habits",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
            }

            // --- Level Card ---
            item {
                LevelCard() // Implementado abajo
            }

            // --- Recently Earned ---
            if (earned.isNotEmpty()) {
                item {
                    Text(
                        "Recently Earned",
                        modifier = Modifier.padding(20.dp, 10.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(earned.takeLast(3).reversed()) { ach ->
                            RecentlyEarnedItem(ach)
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                }
            }

            // --- Tab Picker ---
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
                        .padding(4.dp)
                ) {
                    listOf("All", "Earned", "Locked").forEach { t ->
                        val isSelected = selectedTab == t
                        val count = if (t == "Earned") " (${earned.size})" else if (t == "Locked") " (${locked.size})" else ""
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                                .clickable { selectedTab = t }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "$t$count",
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))
            }

            // --- Grid de Logros ---
            item {
                AchievementGrid(displayed, highlightedId) { id ->
                    highlightedId = if (highlightedId == id) null else id
                }
            }
        }

        // --- Detail Tooltip (Framer Motion style) ---
        AnimatedVisibility(
            visible = highlightedId != null,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 20.dp)
        ) {
            val achievement = uiState.achievements.find { it.id == highlightedId }
            achievement?.let { AchievementTooltip(it) { highlightedId = null } }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AchievementGrid(achievements: List<Achievement>, highlightedId: String?, onSelect: (String) -> Unit) {
    val rarityConfig = getRarityConfig()
    FlowRow(
        modifier = Modifier.padding(horizontal = 20.dp),
        maxItemsInEachRow = 3,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        achievements.forEach { ach ->
            val config = rarityConfig[ach.rarity]!!
            val isHighlighted = highlightedId == ach.id

            Column(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(0.85f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (ach.earned) config.bg else MaterialTheme.colorScheme.surface)
                    .border(
                        if (isHighlighted) 2.dp else 1.dp,
                        if (isHighlighted) config.color else if (ach.earned) config.color.copy(0.4f) else MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { onSelect(ach.id) }
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(ach.emoji, fontSize = 30.sp)
                    if (!ach.earned) {
                        Box(
                            Modifier
                                .matchParentSize()
                                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Lock,
                                null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }
                Text(
                    ach.name,
                    color = if (ach.earned) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                if (!ach.earned && ach.progress != null && ach.total != null) {
                    Spacer(Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { ach.progress.toFloat() / ach.total.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(3.dp)
                            .clip(CircleShape),
                        color = config.color,
                        trackColor = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
    }
}

@Composable
fun AchievementTooltip(ach: Achievement, onClose: () -> Unit) {
    val config = getRarityConfig()[ach.rarity]!!
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clickable { onClose() },
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.5.dp, config.color.copy(0.5f))
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(ach.emoji, fontSize = 40.sp)
            Column {
                Text(
                    ach.name,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    ach.description,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                    Text(config.label, color = config.color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    if (ach.earned) {
                        Text(
                            " • Earned ${ach.earnedDate}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecentlyEarnedItem(ach: Achievement) {
    val config = getRarityConfig()[ach.rarity]!!
    Column(
        modifier = Modifier
            .width(100.dp)
            .background(config.bg, RoundedCornerShape(20.dp))
            .border(1.5.dp, config.color.copy(0.4f), RoundedCornerShape(20.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(ach.emoji, fontSize = 32.sp)
        Text(
            ach.name,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
        Box(
            Modifier
                .padding(top = 4.dp)
                .background(config.color.copy(0.2f), CircleShape)
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(config.label, color = config.color, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LevelStatCard(emoji: String, value: String, label: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(emoji, fontSize = 18.sp)
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 2.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LevelCard() {
    // Datos simulados basados en tu captura (XP: 2,840 de 3,000)
    val userLevel = 12
    val currentXp = 2840
    val nextLevelXp = 3000
    val xpPercent = currentXp.toFloat() / nextLevelXp.toFloat()

    // Estadísticas simuladas
    val earned = 5
    val locked = 5
    val totalXpK = "2.8k"

    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp)) // Esquinas pronunciadas
            .background(MaterialTheme.colorScheme.surface)
            .border(1.5.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(28.dp))
            .padding(16.dp)
    ) {
        // --- SECCIÓN SUPERIOR: Nivel y Barra de XP ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Anillo de progreso amarillo (Igual al diseño)
            Box(contentAlignment = Alignment.Center) {
                ProgressRing(
                    progress = 65f, // Valor simulado para que se vea igual que en la captura
                    size = 76.dp,
                    strokeWidth = 7.dp,
                    color = HabitYellow,
                    trackColor = MaterialTheme.colorScheme.outlineVariant
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = userLevel.toString(),
                            color = HabitYellow,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "LVL",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.width(16.dp))

            // 2. Información del nivel y Barra de progreso
            Column(modifier = Modifier.weight(1f)) {
                // Título con Corona
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("👑", fontSize = 20.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Level $userLevel",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Text(
                    text = "Habit Architect",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(Modifier.height(16.dp))

                // Texto de XP (Actual vs Siguiente)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "%,d XP".format(currentXp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "%,d XP".format(nextLevelXp),
                        color = HabitYellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(4.dp))

                // Barra de XP con degradado (Igual al diseño)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(MaterialTheme.colorScheme.outlineVariant, CircleShape)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(xpPercent) // El % de XP actual
                            .fillMaxHeight()
                            .background(
                                brush = Brush.horizontalGradient(listOf(HabitYellow, HabitOrange)),
                                shape = CircleShape
                            )
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // --- SECCIÓN INFERIOR: Tarjetas de estadísticas oscuras ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LevelStatCard("🏅", earned.toString(), "Earned", Modifier.weight(1f))
            LevelStatCard("🔒", locked.toString(), "Locked", Modifier.weight(1f))
            LevelStatCard("⚡", totalXpK, "Total XP", Modifier.weight(1f))
        }
    }
}