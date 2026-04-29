package com.example.mindstreak.feature.achievements

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.mindstreak.core.theme.*
import com.example.mindstreak.data.model.Rarity

data class RarityUi(
    val color: Color,
    val label: String,
    val bg: Color
)

@Composable
fun getRarityConfig(): Map<Rarity, RarityUi> {
    return mapOf(
        Rarity.COMMON to RarityUi(
            MaterialTheme.colorScheme.onSurfaceVariant,
            "Common",
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)
        ),
        Rarity.RARE to RarityUi(HabitTeal, "Rare", HabitTeal.copy(alpha = 0.12f)),
        Rarity.EPIC to RarityUi(HabitPurple, "Epic", HabitPurple.copy(alpha = 0.12f)),
        Rarity.LEGENDARY to RarityUi(HabitYellow, "Legendary", HabitYellow.copy(alpha = 0.12f))
    )
}

// Helper opcional para obtener uno solo
@Composable
fun Rarity.toUi(): RarityUi {
    return getRarityConfig()[this] ?: getRarityConfig()[Rarity.COMMON]!!
}