package com.example.mindstreak.feature.onboarding.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun GlowingBlob(
    emoji: String,
    blobColor: Color,
    accentColor: Color,
) {
    val orbit1Rotation by rememberInfiniteTransition(label = "orbit1").animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing)), label = "orbit1rot",
    )
    val orbit2Rotation by rememberInfiniteTransition(label = "orbit2").animateFloat(
        initialValue = 0f,
        targetValue = -360f,
        animationSpec = infiniteRepeatable(tween(12000, easing = LinearEasing)),
        label = "orbit2rot",
    )
    val emojiScale by rememberInfiniteTransition(label = "emojiPulse").animateFloat(
        initialValue = 1f, targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emojiScale",
    )

    Box(contentAlignment = Alignment.Center) {
        Box(modifier = Modifier
            .size(236.dp)
            .rotate(orbit2Rotation)) {
            CircleBorderDashed(
                color = accentColor.copy(alpha = 0.2f),
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(modifier = Modifier
            .size(226.dp)
            .rotate(orbit1Rotation)) {
            CircleBorderDashed(
                color = accentColor.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(modifier = Modifier
            .size(220.dp)
            .clip(CircleShape)
            .background(blobColor)
            .blur(2.dp))
        Text(text = emoji, fontSize = (80 * emojiScale).sp, textAlign = TextAlign.Center)
    }
}

@Composable
private fun CircleBorderDashed(color: Color, modifier: Modifier = Modifier) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val strokeWidth = 1.5.dp.toPx()
        val radius = (size.minDimension - strokeWidth) / 2
        drawCircle(
            color = color, radius = radius,
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = strokeWidth,
                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                    floatArrayOf(
                        8f,
                        8f
                    ), 0f
                )
            ),
        )
    }
}

@Composable
fun HabitListIllustration(accentColor: Color, habits: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        habits.forEachIndexed { i, habit ->
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { delay(300L + i * 100L); visible = true }
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(300)) + slideInHorizontally(tween(300)) { -it / 2 }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = habit,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "✓",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                }
            }
        }
    }
}

@Composable
fun BarChartIllustration(accentColor: Color) {
    val heights = listOf(0.40f, 0.70f, 0.55f, 0.90f, 0.75f, 0.95f, 0.85f)
    Row(
        modifier = Modifier.height(80.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        heights.forEachIndexed { i, h ->
            val isLast = i == heights.size - 1
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { delay(200L + i * 80L); visible = true }
            val animatedH by animateFloatAsState(
                targetValue = if (visible) h else 0f,
                animationSpec = tween(500, easing = FastOutSlowInEasing),
                label = "bar_$i"
            )
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .fillMaxHeight(animatedH)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(accentColor.copy(alpha = if (isLast) 1f else 0.6f))
            )
        }
    }
}

@Composable
fun FriendsIllustration(accentColor: Color, friends: List<FriendData>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        friends.forEachIndexed { i, f ->
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { delay(300L + i * 120L); visible = true }
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 2 },
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(text = f.emoji, fontSize = 24.sp)
                    Text(
                        text = f.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "🔥 ${f.streak}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = accentColor
                    )
                }
            }
        }
    }
}

data class FriendData(val emoji: String, val name: String, val streak: Int)
