package com.example.mindstreak.feature.rewards.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.theme.HabitOrange
import com.example.mindstreak.core.theme.HabitYellow

@Composable
fun RewardsHeader(
    userLevel: Int,
    nextRewardLevel: Int?,
) {
    Column(Modifier.padding(20.dp)) {
        Text(
            "🎁 Recompensas",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            "Sube de nivel y canjea premios reales",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(16.dp))

        // Banner de nivel actual
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.horizontalGradient(listOf(HabitYellow.copy(alpha = 0.15f), HabitOrange.copy(alpha = 0.15f)))
                )
                .border(1.5.dp, HabitOrange.copy(0.4f), RoundedCornerShape(20.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                Text("Nivel actual", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("⭐ Nivel $userLevel", fontSize = 20.sp, fontWeight = FontWeight.Black, color = HabitOrange)
            }
            if (nextRewardLevel != null) {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                    Text("Próxima recompensa", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Nivel $nextRewardLevel 🎁", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = HabitYellow)
                }
            } else {
                Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                    Text("¡Todas desbloqueadas!", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = HabitOrange)
                }
            }
        }
    }
}
