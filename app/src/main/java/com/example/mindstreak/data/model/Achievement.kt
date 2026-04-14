package com.example.mindstreak.data.model

enum class Rarity { COMMON, RARE, EPIC, LEGENDARY }

data class Achievement(
    val id: String,
    val name: String,
    val emoji: String,
    val description: String,
    val earned: Boolean,
    val earnedDate: String? = null,
    val progress: Int? = null,
    val total: Int? = null,
    val rarity: Rarity,
)