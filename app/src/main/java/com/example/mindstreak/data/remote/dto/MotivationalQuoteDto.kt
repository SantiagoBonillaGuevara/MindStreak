package com.example.mindstreak.data.remote.dto

import com.example.mindstreak.data.model.MotivationalQuote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MotivationalQuoteDto(
    @SerialName("id") val id: String,
    @SerialName("text") val text: String,
    @SerialName("author") val author: String? = null,
    @SerialName("is_active") val isActive: Boolean = true
)

fun MotivationalQuoteDto.toDomain() = MotivationalQuote(
    id = id,
    text = text,
    author = author,
    isActive = isActive
)
