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

fun HabitDto.toDomain(): Habit {
    return Habit(
        id = id,
        name = name,
        emoji = emoji,
        category = categoryId ?: "Uncategorized", // Simplification for now
        color = color ?: "#FF6B35",
        streak = currentStreak,
        completedToday = false, // This usually depends on lastCompletedDate logic
        lastCompletedDate = lastCompletedDate,
        frequency = frequency,
        completionRate = completionRate,
        reminderTime = reminderTime ?: "08:00",
        weekHistory = emptyList(), // This needs logs
        completionLog = emptyMap() // This needs logs
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
