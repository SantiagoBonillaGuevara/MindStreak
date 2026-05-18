package com.example.mindstreak.feature.rewards.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.theme.HabitOrange
import com.example.mindstreak.data.model.Reward

/**
 * Banner compacto que aparece en la pantalla de Achievements cuando el usuario
 * sube al nivel exacto que desbloquea una nueva recompensa.
 * Colócalo justo encima de la sección "Recently Earned".
 */
@Composable
fun RewardUnlockBanner(
    reward: Reward,
    visible: Boolean,
    onGoToRewards: () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter   = slideInVertically { -it } + fadeIn(),
        exit    = slideOutVertically { -it } + fadeOut(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(HabitOrange.copy(alpha = 0.12f))
                .border(1.5.dp, HabitOrange.copy(0.5f), RoundedCornerShape(18.dp))
                .clickable(onClick = onGoToRewards)
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(reward.emoji, fontSize = 28.sp)
            Column(Modifier.weight(1f)) {
                Text(
                    "¡Nueva recompensa desbloqueada!",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = HabitOrange,
                )
                Text(
                    reward.title,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text("→", fontSize = 18.sp, color = HabitOrange, fontWeight = FontWeight.Bold)
        }
    }
}
