package com.example.mindstreak.data.model

data class User(
    val name: String,
    val username: String,
    val university: String,
    val avatarEmoji: String,
    val level: Int,
    val xp: Int,
    val nextLevelXp: Int,
    val totalStreak: Int,
    val bestStreak: Int,
    val totalHabitsCompleted: Int,
    val joinDate: String,
)