package com.example.mindstreak.feature.create_habit

val EMOJIS = listOf(
    "🏃", "🧘", "💧", "📚", "😴", "📖", "🥗", "🏋️",
    "🚴", "🎯", "💊", "🧠", "☀️", "🌙", "🎵",
)

val FREQUENCIES = listOf("Daily", "Weekdays", "3× a week", "Weekly")

val TIMES = listOf(
    "06:00", "07:00", "08:00", "09:00",
    "12:00", "18:00", "20:00", "21:00", "22:00",
)

enum class CreateStep { DETAILS, SCHEDULE }
