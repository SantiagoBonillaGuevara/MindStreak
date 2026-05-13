package com.example.mindstreak.data.model

data class User(
    val id: String,
    val name: String,
    val username: String,
    val university: String,
    val avatarEmoji: String,
    val level: Int,
    val xp: Int,
    val next_level_xp: Int,
    val totalStreak: Int,
    val bestStreak: Int,
    val totalHabitsCompleted: Int,
    val joinDate: String,
)