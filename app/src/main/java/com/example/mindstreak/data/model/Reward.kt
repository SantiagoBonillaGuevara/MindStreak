package com.example.mindstreak.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class RewardStatus { LOCKED, UNLOCKED, CLAIMED }

@Serializable
data class Reward(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    @SerialName("required_level") val requiredLevel: Int,
    val status: RewardStatus = RewardStatus.LOCKED,
    @SerialName("claimed_at") val claimedAt: String? = null,
    @SerialName("coupon_code") val couponCode: String? = null,
)

/** Catálogo fijo de recompensas disponibles en la app */
object RewardCatalog {
    val ALL: List<Reward> = listOf(
        Reward(
            id = "reward_1",
            title = "Cupón Helado 🍦",
            description = "Un helado gratis en la cafetería universitaria. ¡Te lo ganaste!",
            emoji = "🍦",
            requiredLevel = 3,
        ),
        Reward(
            id = "reward_2",
            title = "Almuerzo Gratis 🍱",
            description = "Un almuerzo completo en el comedor universitario sin costo.",
            emoji = "🍱",
            requiredLevel = 6,
        ),
        Reward(
            id = "reward_3",
            title = "Café & Snack ☕",
            description = "Un café y un snack en la librería. Perfecto para estudiar.",
            emoji = "☕",
            requiredLevel = 10,
        ),
        Reward(
            id = "reward_4",
            title = "Día de Streaming 🎬",
            description = "1 mes gratis de acceso a la plataforma de cursos online de la universidad.",
            emoji = "🎬",
            requiredLevel = 15,
        ),
        Reward(
            id = "reward_5",
            title = "Kit Universitario 🎒",
            description = "Pack de artículos de papelería premium con el logo de tu universidad.",
            emoji = "🎒",
            requiredLevel = 20,
        ),
    )
}
