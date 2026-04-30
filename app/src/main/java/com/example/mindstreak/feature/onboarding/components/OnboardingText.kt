package com.example.mindstreak.feature.onboarding.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingText(
    step: Int,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = step,
        transitionSpec = {
            (fadeIn(tween(400, delayMillis = 100)) + slideInVertically(
                tween(
                    400,
                    delayMillis = 100
                )
            ) { it / 3 })
                .togetherWith(fadeOut(tween(300)) + slideOutVertically(tween(300)) { -it / 3 })
        },
        modifier = modifier.padding(horizontal = 32.dp),
        label = "slideText",
    ) { _ ->
        Column {
            Text(
                text = title,
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 36.sp,
                letterSpacing = (-0.5).sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = subtitle, fontSize = 15.sp, lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }
    }
}
