package com.example.mindstreak.data.remote.dto

import com.example.mindstreak.data.model.Habit
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HabitDto(
    @SerialName("id") val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("name") val name: String,
    @SerialName("emoji") val emoji: String,
    @SerialName("category_id") val categoryId: String?,
    @SerialName("color") val color: String?,
    @SerialName("frequency") val frequency: String,
    @SerialName("reminder_time") val reminderTime: String?,
    @SerialName("is_active") val isActive: Boolean,
    @SerialName("current_streak") val currentStreak: Int,
    @SerialName("best_streak") val bestStreak: Int,
    @SerialName("completion_rate") val completionRate: Float,
    @SerialName("last_completed_date") val lastCompletedDate: String?
)

fun HabitDto.toDomain(completionLog: Map<String, Boolean> = emptyMap()): Habit {
    val today = java.time.LocalDate.now()
    val todayStr = today.toString()
    
    // Derive week history (last 7 days starting from Monday)
    val mondayOffset = today.dayOfWeek.value - 1
    val weekHistory = (0..6).map { i ->
        val date = today.minusDays(mondayOffset.toLong()).plusDays(i.toLong())
        completionLog[date.toString()] == true
    }

    return Habit(
        id = id,
        name = name,
        emoji = emoji,
        category = categoryId ?: "Uncategorized",
        color = color ?: "#7C6EFF",
        streak = currentStreak,
        completedToday = lastCompletedDate == todayStr,
        lastCompletedDate = lastCompletedDate,
        frequency = frequency,
        completionRate = completionRate,
        reminderTime = reminderTime ?: "08:00",
        weekHistory = weekHistory,
        completionLog = completionLog
    )
}

// Map from Domain to DTO for saving
fun Habit.toDto(userId: String): HabitDto {
    return HabitDto(
        id = id,
        userId = userId,
        name = name,
        emoji = emoji,
        categoryId = null, // Needs mapping
        color = color,
        frequency = frequency,
        reminderTime = reminderTime,
        isActive = true,
        currentStreak = streak,
        bestStreak = streak, // Simplification
        completionRate = completionRate,
        lastCompletedDate = lastCompletedDate
    )
}
