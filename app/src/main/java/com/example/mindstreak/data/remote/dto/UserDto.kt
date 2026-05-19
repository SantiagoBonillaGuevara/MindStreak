package com.example.mindstreak.data.remote.dto

import com.example.mindstreak.data.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("username") val username: String,
    @SerialName("university") val university: String? = null,
    @SerialName("avatar_emoji") val avatarEmoji: String,
    @SerialName("level_id") val levelId: Int, // Mapeado directo a la nueva columna de Supabase
    @SerialName("levels") val level: LevelDto? = null, // NUEVO: Join con tabla levels
    @SerialName("xp") val xp: Int,
    @SerialName("total_streak") val totalStreak: Int,
    @SerialName("best_streak") val bestStreak: Int,
    @SerialName("total_habits_completed") val totalHabitsCompleted: Int,
    @SerialName("join_date") val joinDate: String
)

fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        username = username,
        university = university ?: "",
        avatarEmoji = avatarEmoji,
        levelId = levelId,
        levelTitle = level?.title ?: "Habit Architect",
        xp = xp,
        totalStreak = totalStreak,
        bestStreak = bestStreak,
        totalHabitsCompleted = totalHabitsCompleted,
        joinDate = joinDate
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        id = id,
        name = name,
        username = username,
        university = university,
        avatarEmoji = avatarEmoji,
        levelId = levelId,
        xp = xp,
        totalStreak = totalStreak,
        bestStreak = bestStreak,
        totalHabitsCompleted = totalHabitsCompleted,
        joinDate = joinDate
    )
}
