package com.example.mindstreak.feature.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mindstreak.R

import com.example.mindstreak.feature.onboarding.components.FriendData

data class OnboardingTexts(
    val skip: String, val continueBtn: String, val finishBtn: String,
    val habits: List<String>, val friends: List<FriendData>, val slides: List<SlideData>
)

data class SlideData(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val accentColor: androidx.compose.ui.graphics.Color,
    val blobColor: androidx.compose.ui.graphics.Color
)

@Composable
fun rememberOnboardingTexts() = OnboardingTexts(
    skip = stringResource(R.string.onboarding_skip),
    continueBtn = stringResource(R.string.onboarding_continue),
    finishBtn = stringResource(R.string.onboarding_finish),
    habits = listOf("Morning Run 🏃", "Meditate 🧘", "Drink Water 💧"),
    friends = listOf(
        FriendData("👩‍🎨", "Emma", 23),
        FriendData("👨‍🎓", "Mike", 15),
        FriendData("👩‍💻", "Sarah", 18)
    ),
    slides = listOf(
        SlideData(
            "🔥",
            stringResource(R.string.onboarding_slide1_title),
            stringResource(R.string.onboarding_slide1_subtitle),
            com.example.mindstreak.core.theme.HabitPurple,
            com.example.mindstreak.core.theme.HabitPurple.copy(0.15f)
        ),
        SlideData(
            "📈",
            stringResource(R.string.onboarding_slide2_title),
            stringResource(R.string.onboarding_slide2_subtitle),
            com.example.mindstreak.core.theme.HabitOrange,
            com.example.mindstreak.core.theme.HabitOrange.copy(0.15f)
        ),
        SlideData(
            "👥",
            stringResource(R.string.onboarding_slide3_title),
            stringResource(R.string.onboarding_slide3_subtitle),
            com.example.mindstreak.core.theme.HabitTeal,
            com.example.mindstreak.core.theme.HabitTeal.copy(0.15f)
        )
    )
)
