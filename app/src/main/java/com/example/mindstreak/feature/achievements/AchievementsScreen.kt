package com.example.mindstreak.feature.achievements

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mindstreak.data.model.Rarity
import com.example.mindstreak.feature.achievements.components.AchievementGrid
import com.example.mindstreak.feature.achievements.components.Header
import com.example.mindstreak.feature.achievements.components.LevelCard
import com.example.mindstreak.feature.achievements.components.RecentlyEarned
import com.example.mindstreak.feature.achievements.components.TabPicker
import com.example.mindstreak.feature.achievements.components.Tooltip
import com.example.mindstreak.feature.home.AppViewModel

@Composable
fun AchievementsScreen(appViewModel: AppViewModel) {
    val uiState by appViewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf("All") }
    var highlightedId by remember { mutableStateOf<String?>(null) }

    val achievement = uiState.achievements.find { it.id == highlightedId }
    val earned = remember(uiState.achievements) { uiState.achievements.filter { it.earned } }
    val locked = remember(uiState.achievements) { uiState.achievements.filter { !it.earned } }
    val displayed = when (selectedTab) {
        "Earned" -> earned
        "Locked" -> locked
        else -> uiState.achievements
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item {
                Header(title = "Achievements", subtitle = "Level up your habits")
            }

            item {
                LevelCard(
                    userLevel = 12,
                    currentXp = 2840,
                    nextLevelXp = 3000,
                    earned = 5,
                    locked = 5,
                    totalXpK = "2.8k",
                    earnedText = "Earned",
                    lockedText = "Locked",
                    xpText = "Total XP",
                    levelText = "Level",
                    emoji = "👑",
                    lvlName = "Habit Architect"
                )
            }

            if (earned.isNotEmpty()) {
                item {
                    RecentlyEarned(
                        earned = earned,
                        onGetConfig = { ach -> ach.rarity.toUi() },
                        title = "Recently Earned"
                    )
                }
            }

            item {
                TabPicker(
                    earned = earned,
                    locked = locked,
                    selectedTab = selectedTab,
                    onClick = { selectedTab = it },
                    allText = "All",
                    earnedText = "Earned",
                    lockedText = "Locked"
                )
            }

            item {
                AchievementGrid(
                    achievements = displayed,
                    highlightedId = highlightedId,
                    onSelect = { id -> highlightedId = if (highlightedId == id) null else id },
                    onGetConfig = { ach -> ach.rarity.toUi() }
                )
            }
        }

        Tooltip(
            highlightedId = highlightedId,
            achievement = achievement,
            onClose = { highlightedId = null },
            text = "Earned",
            config = achievement?.rarity?.toUi() ?: Rarity.COMMON.toUi()
        )
    }
}