package com.example.mindstreak.data.model

data class Habit(
    val id: String,
    val name: String,
    val emoji: String,
    val category: String,
    val color: String,           // hex string → lo convertimos con Color(android.graphics.Color.parseColor(...))
    val streak: Int,
    val completedToday: Boolean,
    val lastCompletedDate: String? = null,  // "YYYY-MM-DD"
    val frequency: String,
    val completionRate: Float,
    val reminderTime: String,
    val weekHistory: List<Boolean>,          // últimos 7 días
    val completionLog: Map<String, Boolean> = emptyMap(), // "YYYY-MM-DD" → completed
)