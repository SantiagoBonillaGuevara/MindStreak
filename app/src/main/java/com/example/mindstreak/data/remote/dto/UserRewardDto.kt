package com.example.mindstreak.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Representa una fila en la tabla `user_rewards` de Supabase.
 * Se almacena una fila por recompensa reclamada por usuario.
 */
@Serializable
data class UserRewardDto(
    @SerialName("id")          val id: String = "",
    @SerialName("user_id")     val userId: String = "",
    @SerialName("reward_id")   val rewardId: String = "",
    @SerialName("coupon_code") val couponCode: String = "",
    @SerialName("claimed_at")  val claimedAt: String = "",
)
