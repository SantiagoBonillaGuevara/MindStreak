package com.example.mindstreak.feature.onboarding.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OnboardingHero(
    step: Int,
    emoji: String,
    blobColor: Color,
    accentColor: Color,
    habitList: List<String>,
    friendsList: List<FriendData>,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = step,
        transitionSpec = {
            (fadeIn(tween(400)) + scaleIn(tween(400), initialScale = 0.8f))
                .togetherWith(fadeOut(tween(300)) + scaleOut(tween(300), targetScale = 1.1f))
        },
        modifier = modifier.fillMaxWidth(),
        label = "heroContent",
    ) { currentStep ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            GlowingBlob(emoji = emoji, blobColor = blobColor, accentColor = accentColor)
            Spacer(Modifier.height(32.dp))
            when (currentStep) {
                0 -> HabitListIllustration(accentColor = accentColor, habits = habitList)
                1 -> BarChartIllustration(accentColor = accentColor)
                2 -> FriendsIllustration(accentColor = accentColor, friends = friendsList)
            }
        }
    }
}
