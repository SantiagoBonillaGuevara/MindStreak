package com.example.mindstreak.feature.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindstreak.data.model.Reward
import com.example.mindstreak.data.model.RewardStatus
import com.example.mindstreak.data.repository.RewardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RewardsUiState(
    val rewards: List<Reward> = emptyList(),
    val isLoading: Boolean = false,
    val claimingId: String? = null,
    val errorMessage: String? = null,
    /** Recompensa recién reclamada para mostrar el diálogo de celebración */
    val justClaimed: Reward? = null,
)

class RewardsViewModel(
    private val repository: RewardRepository = RewardRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RewardsUiState())
    val uiState: StateFlow<RewardsUiState> = _uiState.asStateFlow()

    fun loadRewards(userLevel: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            runCatching { repository.getRewardsForUser(userLevel) }
                .onSuccess { list -> _uiState.update { it.copy(rewards = list, isLoading = false) } }
                .onFailure { e  -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
        }
    }

    fun claimReward(reward: Reward, userLevel: Int) {
        if (reward.status != RewardStatus.UNLOCKED) return
        viewModelScope.launch {
            _uiState.update { it.copy(claimingId = reward.id, errorMessage = null) }
            repository.claimReward(reward.id)
                .onSuccess { coupon ->
                    val updated = reward.copy(
                        status     = RewardStatus.CLAIMED,
                        couponCode = coupon,
                        claimedAt  = java.time.Instant.now().toString(),
                    )
                    _uiState.update { state ->
                        state.copy(
                            claimingId = null,
                            justClaimed = updated,
                            rewards = state.rewards.map { if (it.id == reward.id) updated else it },
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(claimingId = null, errorMessage = e.message) }
                }
        }
    }

    fun dismissCelebration() = _uiState.update { it.copy(justClaimed = null) }
    fun clearError()         = _uiState.update { it.copy(errorMessage = null) }
}
