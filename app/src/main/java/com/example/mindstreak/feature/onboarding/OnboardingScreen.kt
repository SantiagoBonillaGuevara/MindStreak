package com.example.mindstreak.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.feature.onboarding.components.*

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }
    val texts = rememberOnboardingTexts()
    val slide = texts.slides[step]
    val isLastStep = step == texts.slides.size - 1

    Column(Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onFinish) {
                Text(
                    texts.skip,
                    color = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
        OnboardingHero(
            step,
            slide.emoji,
            slide.blobColor,
            slide.accentColor,
            texts.habits,
            texts.friends,
            Modifier.weight(1f)
        )
        OnboardingText(step, slide.title, slide.subtitle)
        OnboardingProgress(
            step,
            texts.slides.size,
            slide.accentColor,
            { if (!isLastStep) step++ else onFinish() },
            if (!isLastStep) texts.continueBtn else texts.finishBtn,
            isLastStep
        )
    }
}
