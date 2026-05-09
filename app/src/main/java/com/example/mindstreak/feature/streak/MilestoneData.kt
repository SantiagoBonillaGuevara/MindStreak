package com.example.mindstreak.feature.streak

data class Milestone(
    val days: Int,
    val emoji: String,
    val label: String,
    val achieved: Boolean,
    val current: Boolean = false
)

val MILESTONES = listOf(
    Milestone(7, "🔥", "First Flame", true),
    Milestone(14, "💪", "Iron Will", true),
    Milestone(21, "⚡", "Current", true, current = true),
    Milestone(30, "🌟", "Habit Hero", false),
    Milestone(50, "💎", "Diamond Mind", false),
    Milestone(100, "👑", "Legend", false)
)
