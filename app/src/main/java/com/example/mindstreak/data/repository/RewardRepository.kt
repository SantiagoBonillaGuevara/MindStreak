package com.example.mindstreak.data.repository

import com.example.mindstreak.data.model.Reward
import com.example.mindstreak.data.model.RewardCatalog
import com.example.mindstreak.data.model.RewardStatus
import com.example.mindstreak.data.remote.SupabaseClientProvider
import com.example.mindstreak.data.remote.dto.UserRewardDto
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.UUID

class RewardRepository {

    private val client get() = SupabaseClientProvider.client

    /**
     * Devuelve la lista completa de recompensas del catálogo,
     * marcando cuáles ya fueron reclamadas por el usuario autenticado.
     */
    suspend fun getRewardsForUser(userLevel: Int): List<Reward> = withContext(Dispatchers.IO) {
        val userId = client.auth.currentUserOrNull()?.id ?: return@withContext buildCatalog(userLevel, emptyList())

        val claimed = runCatching {
            client.postgrest["user_rewards"]
                .select(columns = Columns.ALL) {
                    filter { eq("user_id", userId) }
                }
                .decodeList<UserRewardDto>()
        }.getOrElse { emptyList() }

        buildCatalog(userLevel, claimed)
    }

    /**
     * Reclama una recompensa: inserta en `user_rewards` y devuelve el código de cupón generado.
     */
    suspend fun claimReward(rewardId: String): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            val userId = client.auth.currentUserOrNull()?.id
                ?: error("Usuario no autenticado")

            val coupon = generateCoupon(rewardId)
            val dto = UserRewardDto(
                id         = UUID.randomUUID().toString(),
                userId     = userId,
                rewardId   = rewardId,
                couponCode = coupon,
                claimedAt  = Instant.now().toString(),
            )

            client.postgrest["user_rewards"].insert(dto)
            coupon
        }
    }

    // ─── Helpers ─────────────────────────────────────────────────────────────

    private fun buildCatalog(userLevel: Int, claimed: List<UserRewardDto>): List<Reward> {
        val claimedMap = claimed.associateBy { it.rewardId }
        return RewardCatalog.ALL.map { reward ->
            val dto = claimedMap[reward.id]
            val status = when {
                dto != null                          -> RewardStatus.CLAIMED
                userLevel >= reward.requiredLevel    -> RewardStatus.UNLOCKED
                else                                 -> RewardStatus.LOCKED
            }
            reward.copy(
                status      = status,
                claimedAt   = dto?.claimedAt,
                couponCode  = dto?.couponCode,
            )
        }
    }

    private fun generateCoupon(rewardId: String): String {
        val suffix = rewardId.uppercase().replace("_", "").takeLast(4)
        val rand   = UUID.randomUUID().toString().replace("-", "").take(6).uppercase()
        return "MS-$suffix-$rand"
    }
}
