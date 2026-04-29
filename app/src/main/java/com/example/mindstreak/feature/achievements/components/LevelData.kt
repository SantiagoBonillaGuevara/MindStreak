package com.example.mindstreak.feature.achievements.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.theme.HabitOrange
import com.example.mindstreak.core.theme.HabitYellow

@Composable
fun LevelData(
    userLevel: Int,
    levelText: String,
    emoji: String,
    lvlName: String,
    currentXp: Int,
    nextLevelXp: Int
) {
    val xpPercent = currentXp.toFloat() / nextLevelXp.toFloat()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Información del nivel y Barra de progreso
        Column(modifier = Modifier.weight(1f)) {
            // Título
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(emoji, fontSize = 20.sp)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "$levelText $userLevel",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Text(
                text = lvlName,
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
            // Barra de XP con degradado
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
}