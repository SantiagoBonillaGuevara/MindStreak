package com.example.mindstreak.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LevelDto(
    @SerialName("title") val title: String
)
