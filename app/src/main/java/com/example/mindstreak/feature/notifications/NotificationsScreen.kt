package com.example.mindstreak.feature.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.mindstreak.core.theme.*
import com.example.mindstreak.feature.home.AppViewModel
import com.example.mindstreak.feature.notifications.components.*

@Composable
fun NotificationsScreen(onBack: () -> Unit, appViewModel: AppViewModel) {
    val uiState by appViewModel.uiState.collectAsState()
    val texts = rememberNotificationsTexts()
    val context = LocalContext.current
    val isMasterOn = uiState.notificationsEnabled

    // Lanzador para permisos de notificaciones (Android 13+)
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            appViewModel.setNotificationsEnabled(true)
        } else {
            Toast.makeText(context, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
        }
    }


    LazyColumn(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item { NotificationsHeader(texts.title, onBack, texts.backDesc) }
        item {
            MasterToggleCard(
                isMasterOn,
                { enabled ->
                    if (enabled && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val hasPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED
                        
                        if (hasPermission) {
                            appViewModel.setNotificationsEnabled(true)
                        } else {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    } else {
                        appViewModel.setNotificationsEnabled(enabled)
                    }
                },
                texts.masterTitle,
                texts.masterActive,
                texts.masterMuted
            )
        }
        item {
            Text(
                texts.habitRemindersTitle,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        items(uiState.allHabits) { h ->
            HabitReminderItem(
                h.emoji,
                h.name,
                h.reminderTime,
                h.frequency,
                h.reminderEnabled,
                { appViewModel.toggleHabitReminder(h.id, it) },
                h.color,
                isMasterOn
            )
        }
    }
}
