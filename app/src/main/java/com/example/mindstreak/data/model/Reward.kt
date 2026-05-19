package com.example.mindstreak.data.model

import androidx.annotation.StringRes
import com.example.mindstreak.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

enum class RewardStatus { LOCKED, UNLOCKED, CLAIMED }

@Serializable
data class Reward(
    val id: String,
    @Transient @StringRes val titleRes: Int = 0,
    @Transient @StringRes val descRes: Int = 0,
    val title: String = "",
    val description: String = "",
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
            titleRes = R.string.reward_1_title,
            descRes = R.string.reward_1_desc,
            emoji = "🍦",
            requiredLevel = 3,
        ),
        Reward(
            id = "reward_2",
            titleRes = R.string.reward_2_title,
            descRes = R.string.reward_2_desc,
            emoji = "🍱",
            requiredLevel = 6,
        ),
        Reward(
            id = "reward_3",
            titleRes = R.string.reward_3_title,
            descRes = R.string.reward_3_desc,
            emoji = "☕",
            requiredLevel = 10,
        ),
        Reward(
            id = "reward_4",
            titleRes = R.string.reward_4_title,
            descRes = R.string.reward_4_desc,
            emoji = "🎬",
            requiredLevel = 15,
        ),
        Reward(
            id = "reward_5",
            titleRes = R.string.reward_5_title,
            descRes = R.string.reward_5_desc,
            emoji = "🎒",
            requiredLevel = 20,
        ),
    )
}
