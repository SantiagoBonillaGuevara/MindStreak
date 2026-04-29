package com.example.mindstreak.feature.achievements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun LevelCard(
    userLevel: Int,
    currentXp: Int,
    nextLevelXp: Int,
    earned: Int,
    locked: Int,
    totalXpK: String,
    earnedText: String,
    lockedText: String,
    xpText: String,
    levelText: String,
    emoji: String,
    lvlName: String
) {

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
        LevelData(
            userLevel = userLevel,
            levelText = levelText,
            emoji = emoji,
            lvlName = lvlName,
            currentXp = currentXp,
            nextLevelXp = nextLevelXp
        )

        Spacer(Modifier.height(20.dp))

        // --- SECCIÓN INFERIOR: Tarjetas de estadísticas oscuras ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            LevelStatCard("🏅", earned.toString(), earnedText, Modifier.weight(1f))
            LevelStatCard("🔒", locked.toString(), lockedText, Modifier.weight(1f))
            LevelStatCard("⚡", totalXpK, xpText, Modifier.weight(1f))
        }
    }
}