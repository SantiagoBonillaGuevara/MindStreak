package com.example.mindstreak.data.remote.dto

import com.example.mindstreak.data.model.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("username") val username: String,
    @SerialName("university") val university: String,
    @SerialName("avatar_emoji") val avatarEmoji: String,
    @SerialName("level") val level: Int,
    @SerialName("xp") val xp: Int,
    @SerialName("next_level_xp") val nextLevelXp: Int,
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
        university = university,
        avatarEmoji = avatarEmoji,
        level = level,
        xp = xp,
        nextLevelXp = nextLevelXp,
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
        level = level,
        xp = xp,
        nextLevelXp = nextLevelXp,
        totalStreak = totalStreak,
        bestStreak = bestStreak,
        totalHabitsCompleted = totalHabitsCompleted,
        joinDate = joinDate
    )
}
