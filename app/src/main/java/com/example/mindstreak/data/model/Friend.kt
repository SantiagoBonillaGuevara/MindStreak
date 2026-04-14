package com.example.mindstreak.data.model

data class Friend(
    val id: String,
    val name: String,
    val username: String,
    val avatarEmoji: String,
    val streak: Int,
    val level: Int,
    val points: Int,
    val weeklyHabits: Int,
    val isYou: Boolean = false,
)