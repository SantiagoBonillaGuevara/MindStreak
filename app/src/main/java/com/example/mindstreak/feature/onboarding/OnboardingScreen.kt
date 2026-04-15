package com.example.mindstreak.feature.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
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

// ─── Datos de cada slide ────────────────────────────────────────────────────
// Equivalente al array SLIDES de React
private data class SlideData(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val accentColor: Color,
    val blobColor: Color,
)

private val SLIDES = listOf(
    SlideData(
        emoji = "🔥",
        title = "Build Habits\nThat Stick",
        subtitle = "Track your daily habits with one tap. Stay consistent and watch your life transform.",
        accentColor = Color(0xFF030213),
        blobColor   = Color(0xFF030213).copy(alpha = 0.15f),
    ),
    SlideData(
        emoji = "📈",
        title = "Streaks Keep\nYou Going",
        subtitle = "Never break the chain. Watch your streak grow and unlock achievements as you level up.",
        accentColor = Color(0xFFF97316),   // orange-500
        blobColor   = Color(0xFFF97316).copy(alpha = 0.15f),
    ),
    SlideData(
        emoji = "👥",
        title = "Better Together\nWith Friends",
        subtitle = "Compete on leaderboards, share milestones, and motivate each other every single day.",
        accentColor = Color(0xFF14B8A6),   // teal-500
        blobColor   = Color(0xFF14B8A6).copy(alpha = 0.15f),
    ),
)

// ─── Screen ─────────────────────────────────────────────────────────────────
@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    val slide = SLIDES[step]

    val next = {
        if (step < SLIDES.size - 1) step++ else onFinish()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // ── Skip button ───────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(onClick = onFinish) {
                Text(
                    text = "Skip",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                )
            }
        }

        // ── Hero illustration (AnimatePresence mode="wait") ───
        AnimatedContent(
            targetState = step,
            transitionSpec = {
                // initial: opacity 0, scale 0.8, y 20 → animate: opacity 1, scale 1, y 0
                // exit: opacity 0, scale 1.1, y -20
                (fadeIn(tween(400)) + scaleIn(tween(400), initialScale = 0.8f))
                    .togetherWith(fadeOut(tween(300)) + scaleOut(tween(300), targetScale = 1.1f))
            },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            label = "heroContent",
        ) { currentStep ->
            val currentSlide = SLIDES[currentStep]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                // Glowing blob con orbiting rings
                GlowingBlob(
                    emoji = currentSlide.emoji,
                    blobColor = currentSlide.blobColor,
                    accentColor = currentSlide.accentColor,
                )

                Spacer(Modifier.height(32.dp))

                // Ilustraciones por step
                when (currentStep) {
                    0 -> HabitListIllustration(accentColor = currentSlide.accentColor)
                    1 -> BarChartIllustration(accentColor = currentSlide.accentColor)
                    2 -> FriendsIllustration(accentColor = currentSlide.accentColor)
                }
            }
        }

        // ── Texto (AnimatePresence mode="wait") ───────────────
        AnimatedContent(
            targetState = step,
            transitionSpec = {
                // initial: y 30, opacity 0 → exit: y -20, opacity 0
                (fadeIn(tween(400, delayMillis = 100)) + slideInVertically(tween(400, delayMillis = 100)) { it / 3 })
                    .togetherWith(fadeOut(tween(300)) + slideOutVertically(tween(300)) { -it / 3 })
            },
            modifier = Modifier.padding(horizontal = 32.dp),
            label = "slideText",
        ) { currentStep ->
            val currentSlide = SLIDES[currentStep]
            Column {
                Text(
                    text = currentSlide.title,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 36.sp,
                    letterSpacing = (-0.5).sp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = currentSlide.subtitle,
                    fontSize = 15.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            }
        }

        // ── Progress dots + CTA ───────────────────────────────
        Column(
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Dots — equivalente al animate width: 24 vs 8
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SLIDES.forEachIndexed { index, s ->
                    val width by animateDpAsState(
                        targetValue = if (index == step) 24.dp else 8.dp,
                        animationSpec = tween(300),
                        label = "dot_$index",
                    )
                    Box(
                        modifier = Modifier
                            .height(8.dp)
                            .width(width)
                            .clip(CircleShape)
                            .background(
                                if (index == step) slide.accentColor
                                else MaterialTheme.colorScheme.secondary
                            ),
                    )
                }
            }

            // CTA Button
            Button(
                onClick = { next() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = slide.accentColor,
                    contentColor = Color.White,
                ),
            ) {
                Text(
                    text = if (step < SLIDES.size - 1) "Continue" else "Get Started",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = if (step < SLIDES.size - 1)
                        Icons.Default.ChevronRight
                    else
                        Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
    }
}

// ─── Glowing blob con orbiting rings ────────────────────────────────────────
@Composable
private fun GlowingBlob(
    emoji: String,
    blobColor: Color,
    accentColor: Color,
) {
    // Rotación infinita — equivalente a animate={{ rotate: 360 }} repeat: Infinity
    val orbit1Rotation by rememberInfiniteTransition(label = "orbit1")
        .animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing)),
            label = "orbit1rot",
        )
    val orbit2Rotation by rememberInfiniteTransition(label = "orbit2")
        .animateFloat(
            initialValue = 0f,
            targetValue = -360f,
            animationSpec = infiniteRepeatable(tween(12000, easing = LinearEasing)),
            label = "orbit2rot",
        )

    // Pulsing emoji — equivalente a animate scale: [1, 1.08, 1] repeat: Infinity
    val emojiScale by rememberInfiniteTransition(label = "emojiPulse")
        .animateFloat(
            initialValue = 1f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                tween(2000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "emojiScale",
        )

    Box(contentAlignment = Alignment.Center) {
        // Orbit 2 (exterior)
        Box(
            modifier = Modifier
                .size(236.dp)
                .rotate(orbit2Rotation)
                .clip(CircleShape)
                .background(Color.Transparent),
        ) {
            // Borde punteado simulado con un Box con border
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.Transparent),
            )
        }
        // Borde orbit2
        Box(
            modifier = Modifier
                .size(236.dp)
                .rotate(orbit2Rotation),
        ) {
            CircleBorderDashed(
                color = accentColor.copy(alpha = 0.2f),
                modifier = Modifier.fillMaxSize(),
            )
        }

        // Orbit 1 (interior)
        Box(
            modifier = Modifier
                .size(226.dp)
                .rotate(orbit1Rotation),
        ) {
            CircleBorderDashed(
                color = accentColor.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxSize(),
            )
        }

        // Blob central
        Box(
            modifier = Modifier
                .size(220.dp)
                .clip(CircleShape)
                .background(blobColor)
                .blur(2.dp),
        )

        // Emoji pulsante
        Text(
            text = emoji,
            fontSize = (80 * emojiScale).sp,
            textAlign = TextAlign.Center,
        )
    }
}

// Borde circular punteado con Canvas
@Composable
private fun CircleBorderDashed(
    color: Color,
    modifier: Modifier = Modifier,
) {
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val strokeWidth = 1.5.dp.toPx()
        val radius = (size.minDimension - strokeWidth) / 2
        drawCircle(
            color = color,
            radius = radius,
            style = androidx.compose.ui.graphics.drawscope.Stroke(
                width = strokeWidth,
                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                    floatArrayOf(8f, 8f), 0f   // Equivalente a border-dashed
                ),
            ),
        )
    }
}

// ─── Ilustraciones por step ──────────────────────────────────────────────────

// Step 0 — lista de hábitos
@Composable
private fun HabitListIllustration(accentColor: Color) {
    val habits = listOf("Morning Run 🏃", "Meditate 🧘", "Drink Water 💧")
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        habits.forEachIndexed { i, habit ->
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(300L + i * 100L)
                visible = true
            }
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(300)) + slideInHorizontally(tween(300)) { -it / 2 },
            ) {
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
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "✓",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                    )
                }
            }
        }
    }
}

// Step 1 — bar chart
@Composable
private fun BarChartIllustration(accentColor: Color) {
    val heights = listOf(0.40f, 0.70f, 0.55f, 0.90f, 0.75f, 0.95f, 0.85f)
    Row(
        modifier = Modifier.height(80.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        heights.forEachIndexed { i, h ->
            val isLast = i == heights.size - 1
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(200L + i * 80L)
                visible = true
            }
            val animatedH by animateFloatAsState(
                targetValue = if (visible) h else 0f,
                animationSpec = tween(500, easing = FastOutSlowInEasing),
                label = "bar_$i",
            )
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .fillMaxHeight(animatedH)
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                    .background(
                        accentColor.copy(alpha = if (isLast) 1f else 0.6f)
                    ),
            )
        }
    }
}

// Step 2 — friends cards
@Composable
private fun FriendsIllustration(accentColor: Color) {
    data class Friend(val emoji: String, val name: String, val streak: Int)
    val friends = listOf(
        Friend("👩‍🎨", "Emma", 23),
        Friend("👨‍🎓", "Mike", 15),
        Friend("👩‍💻", "Sarah", 18),
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        friends.forEachIndexed { i, f ->
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(300L + i * 120L)
                visible = true
            }
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 2 },
                modifier = Modifier.weight(1f),
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
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "🔥 ${f.streak}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = accentColor,
                    )
                }
            }
        }
    }
}