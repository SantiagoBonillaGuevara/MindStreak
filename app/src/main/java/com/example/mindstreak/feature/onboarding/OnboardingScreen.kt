package com.example.mindstreak.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindstreak.core.theme.*
import com.example.mindstreak.feature.onboarding.components.*

private data class SlideData(
    val emoji: String,
    val title: String,
    val subtitle: String,
    val accentColor: Color,
    val blobColor: Color,
)

@Composable
fun OnboardingScreen(onFinish: () -> Unit) {
    var step by remember { mutableIntStateOf(0) }

    val texts = object {
        val skip = "Skip"
        val continueBtn = "Continue"
        val finishBtn = "Get Started"
        val habits = listOf("Morning Run 🏃", "Meditate 🧘", "Drink Water 💧")
        val friends = listOf(
            FriendData("👩‍🎨", "Emma", 23),
            FriendData("👨‍🎓", "Mike", 15),
            FriendData("👩‍💻", "Sarah", 18)
        )
        val slides = listOf(
            SlideData(
                "🔥",
                "Build Habits\nThat Stick",
                "Track your daily habits with one tap. Stay consistent and watch your life transform.",
                HabitPurple,
                HabitPurple.copy(alpha = 0.15f)
            ),
            SlideData(
                "📈",
                "Streaks Keep\nYou Going",
                "Never break the chain. Watch your streak grow and unlock achievements as you level up.",
                HabitOrange,
                HabitOrange.copy(alpha = 0.15f)
            ),
            SlideData(
                "👥",
                "Better Together\nWith Friends",
                "Compete on leaderboards, share milestones, and motivate each other every single day.",
                HabitTeal,
                HabitTeal.copy(alpha = 0.15f)
            )
        )
    }

    val slide = texts.slides[step]
    val isLastStep = step == texts.slides.size - 1

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = onFinish) {
                Text(
                    text = texts.skip,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }

        OnboardingHero(
            step = step,
            emoji = slide.emoji,
            blobColor = slide.blobColor,
            accentColor = slide.accentColor,
            habitList = texts.habits,
            friendsList = texts.friends,
            modifier = Modifier.weight(1f)
        )

        OnboardingText(step = step, title = slide.title, subtitle = slide.subtitle)

        OnboardingProgress(
            step = step, totalSteps = texts.slides.size, accentColor = slide.accentColor,
            onNext = { if (!isLastStep) step++ else onFinish() },
            buttonText = if (!isLastStep) texts.continueBtn else texts.finishBtn,
            isLastStep = isLastStep
        )
    }
}
