package com.example.mindstreak.feature.statistics.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mindstreak.core.components.StatSmallCard
import com.example.mindstreak.core.theme.*

@Composable
fun StatsCardsRow(
    weekStats: String,
    weekLabel: String,
    bestStreak: String,
    bestStreakLabel: String,
    avgDay: String,
    avgDayLabel: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatSmallCard("📅", weekStats, weekLabel, HabitPurple, Modifier.weight(1f))
        StatSmallCard("🔥", bestStreak, bestStreakLabel, HabitOrange, Modifier.weight(1f))
        StatSmallCard("⚡", avgDay, avgDayLabel, HabitTeal, Modifier.weight(1f))
    }
}
