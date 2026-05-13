package com.example.mindstreak.data.remote.dto

import com.example.mindstreak.data.model.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("emoji") val emoji: String,
    @SerialName("color") val color: String,
    @SerialName("is_default") val isDefault: Boolean = false
)

fun CategoryDto.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        emoji = emoji,
        color = color
    )
}
