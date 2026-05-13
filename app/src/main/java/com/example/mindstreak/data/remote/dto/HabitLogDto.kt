package com.example.mindstreak.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HabitLogDto(
    @SerialName("id") val id: String? = null,
    @SerialName("habit_id") val habitId: String,
    @SerialName("user_id") val userId: String,
    @SerialName("completed_date") val completedDate: String,
    @SerialName("completed") val completed: Boolean
)
