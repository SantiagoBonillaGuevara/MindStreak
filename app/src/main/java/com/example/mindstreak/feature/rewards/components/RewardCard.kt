package com.example.mindstreak.feature.rewards.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.R
import com.example.mindstreak.core.theme.HabitOrange
import com.example.mindstreak.core.theme.HabitPurple
import com.example.mindstreak.core.theme.HabitTeal
import com.example.mindstreak.data.model.Reward
import com.example.mindstreak.data.model.RewardStatus

@Composable
fun RewardCard(
    reward: Reward,
    userLevel: Int,
    isClaiming: Boolean,
    onClaim: () -> Unit,
) {
    val isLocked     = reward.status == RewardStatus.LOCKED
    val isUnlocked   = reward.status == RewardStatus.UNLOCKED
    val isClaimed    = reward.status == RewardStatus.CLAIMED

    val accentColor = when {
        isClaimed  -> HabitTeal
        isUnlocked -> HabitOrange
        else       -> MaterialTheme.colorScheme.outlineVariant
    }

    val bgColor by animateColorAsState(
        targetValue = when {
            isClaimed  -> HabitTeal.copy(alpha = 0.08f)
            isUnlocked -> HabitOrange.copy(alpha = 0.08f)
            else       -> MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(300),
        label = "rewardBg",
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(1.5.dp, accentColor.copy(if (isLocked) 0.2f else 0.5f), RoundedCornerShape(20.dp))
            .alpha(if (isLocked) 0.55f else 1f)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Emoji
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(accentColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(if (isLocked) "🔒" else reward.emoji, fontSize = 24.sp)
        }

        Spacer(Modifier.width(14.dp))

        // Info
        Column(Modifier.weight(1f)) {
            Text(
                if (reward.titleRes != 0) stringResource(reward.titleRes) else reward.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                if (reward.descRes != 0) stringResource(reward.descRes) else reward.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(6.dp))

            // Badge de nivel requerido / código cupón
            if (isClaimed && reward.couponCode != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(HabitTeal.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text("✅ ", fontSize = 11.sp)
                    Text(
                        reward.couponCode,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = HabitTeal,
                        letterSpacing = 1.sp,
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(accentColor.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Text(
                        stringResource(R.string.rewards_level_required_template, reward.requiredLevel),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                    )
                }
            }
        }

        Spacer(Modifier.width(10.dp))

        // Botón de acción
        when {
            isClaimed -> {
                Text("🎉", fontSize = 28.sp)
            }
            isUnlocked -> {
                Button(
                    onClick = onClaim,
                    enabled = !isClaiming,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = HabitOrange),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                ) {
                    if (isClaiming) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = Color.White,
                        )
                    } else {
                        Text(
                            stringResource(R.string.rewards_claim_btn),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
            else -> {
                // Locked: mostrar cuántos niveles faltan
                val remaining = reward.requiredLevel - userLevel
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("🔒", fontSize = 20.sp)
                    Text(
                        stringResource(R.string.rewards_locked_levels_template, remaining),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}
