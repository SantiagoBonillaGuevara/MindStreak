package com.example.mindstreak.feature.rewards.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.mindstreak.core.theme.HabitOrange
import com.example.mindstreak.core.theme.HabitTeal
import com.example.mindstreak.data.model.Reward

@Composable
fun RewardCelebrationDialog(
    reward: Reward,
    onDismiss: () -> Unit,
) {
    var appeared by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (appeared) 1f else 0.7f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 300f),
        label = "dialogScale",
    )

    LaunchedEffect(Unit) { appeared = true }

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .scale(scale)
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Gran emoji con fondo
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(HabitTeal.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(reward.emoji, fontSize = 48.sp)
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "¡Recompensa canjeada!",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(Modifier.height(6.dp))

            Text(
                reward.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = HabitOrange,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                reward.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(18.dp))

            // Código cupón
            reward.couponCode?.let { code ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(HabitTeal.copy(alpha = 0.1f))
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Código de canje", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        code,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = HabitTeal,
                        letterSpacing = 2.sp,
                    )
                    Text(
                        "Muéstralo en caja",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = HabitOrange),
            ) {
                Text("¡Genial! 🎉", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
