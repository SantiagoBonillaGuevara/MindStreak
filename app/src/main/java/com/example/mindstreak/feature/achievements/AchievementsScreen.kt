package com.example.mindstreak.feature.achievements

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mindstreak.data.model.Rarity
import com.example.mindstreak.feature.achievements.components.*
import com.example.mindstreak.feature.home.AppViewModel

@Composable
fun AchievementsScreen(appViewModel: AppViewModel) {
    val uiState by appViewModel.uiState.collectAsState()
    val texts = rememberAchievementsTexts()
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var highlightedId by remember { mutableStateOf<String?>(null) }
    val achievement = uiState.achievements.find { it.id == highlightedId }
    val earned = remember(uiState.achievements) { uiState.achievements.filter { it.earned } }
    val locked = remember(uiState.achievements) { uiState.achievements.filter { !it.earned } }
    val displayed = when (selectedTabIndex) {
        1 -> earned; 2 -> locked; else -> uiState.achievements
    }
    val tabLabels = listOf(texts.allText, texts.earnedText, texts.lockedText)

    Box(Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        LazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 120.dp)) {
            item { Header(texts.title, texts.subtitle) }
            item {
                uiState.user?.let { user ->
                    LevelCard(
                        userLevel = user.levelId,
                        currentXp = user.xpInCurrentLevel,
                        nextLevelXp = user.nextLevelXpNeta,
                        earned = earned.size,
                        locked = locked.size,
                        totalXpK = if (user.xp >= 1000) "%.1fk".format(user.xp / 1000f) else user.xp.toString(),
                        earnedText = texts.earnedText,
                        lockedText = texts.lockedText,
                        xpText = texts.xpText,
                        levelText = texts.levelText,
                        emoji = "👑",
                        lvlName = texts.lvlName
                    )
                }
            }
            if (earned.isNotEmpty()) item {
                RecentlyEarned(
                    earned,
                    { it.rarity.toUi() },
                    texts.recentlyEarned
                )
            }
            item {
                TabPicker(
                    earned,
                    locked,
                    tabLabels[selectedTabIndex],
                    { selectedTabIndex = tabLabels.indexOf(it) },
                    texts.allText,
                    texts.earnedText,
                    texts.lockedText
                )
            }
            item {
                AchievementGrid(
                    displayed,
                    highlightedId,
                    { id -> highlightedId = if (highlightedId == id) null else id },
                    { it.rarity.toUi() })
            }
        }
        Tooltip(
            highlightedId,
            achievement,
            { highlightedId = null },
            texts.earnedText,
            achievement?.rarity?.toUi() ?: Rarity.COMMON.toUi()
        )
    }
}
