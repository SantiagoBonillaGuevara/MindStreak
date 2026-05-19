package com.example.mindstreak.feature.rewards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mindstreak.R
import com.example.mindstreak.data.model.Reward
import com.example.mindstreak.data.model.RewardStatus
import com.example.mindstreak.feature.home.AppViewModel
import com.example.mindstreak.feature.rewards.components.RewardCard
import com.example.mindstreak.feature.rewards.components.RewardCelebrationDialog
import com.example.mindstreak.feature.rewards.components.RewardsHeader

@Composable
fun RewardsScreen(
    appViewModel: AppViewModel,
    rewardsViewModel: RewardsViewModel = viewModel(),
) {
    val appState  by appViewModel.uiState.collectAsState()
    val uiState   by rewardsViewModel.uiState.collectAsState()
    val userLevel = appState.user?.levelId ?: 1

    // Cargar recompensas cuando cambia el nivel
    LaunchedEffect(userLevel) {
        rewardsViewModel.loadRewards(userLevel)
    }

    // Diálogo de celebración al reclamar
    uiState.justClaimed?.let { reward ->
        RewardCelebrationDialog(
            reward  = reward,
            onDismiss = rewardsViewModel::dismissCelebration,
        )
    }

    // Snackbar de error
    uiState.errorMessage?.let { msg ->
        LaunchedEffect(msg) {
            rewardsViewModel.clearError()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp),
    ) {
        item {
            RewardsHeader(
                userLevel  = userLevel,
                nextRewardLevel = uiState.rewards
                    .firstOrNull { it.status == RewardStatus.LOCKED }
                    ?.requiredLevel,
            )
        }

        if (uiState.isLoading) {
            item {
                Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        } else {
            // Recompensas desbloqueadas / reclamadas primero
            val sorted = uiState.rewards.sortedWith(
                compareBy({ it.status == RewardStatus.LOCKED }, { it.requiredLevel })
            )
            items(sorted) { reward ->
                RewardCard(
                    reward     = reward,
                    userLevel  = userLevel,
                    isClaiming = uiState.claimingId == reward.id,
                    onClaim    = { rewardsViewModel.claimReward(reward, userLevel) },
                )
            }
        }
    }
}
