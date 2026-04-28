package com.example.mindstreak.feature.notifications
// UI y Layout
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import android.graphics.Color.parseColor as parseHexColor

// Animación y Estado
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Iconos (Asegúrate de tener la dependencia material-icons-extended)
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

// Componentes Material 3
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text

// ViewModel y Navegación
import com.example.mindstreak.core.components.CustomToggle
import com.example.mindstreak.core.components.NotificationTypeRow
import com.example.mindstreak.core.theme.*
import androidx.compose.material3.MaterialTheme
import com.example.mindstreak.feature.home.AppViewModel

@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    appViewModel: AppViewModel
) {
    val uiState by appViewModel.uiState.collectAsState()

    // Estados locales para los Toggles (Equivalente a los useState de React)
    var isMasterOn by remember { mutableStateOf(true) }
    var streakAlert by remember { mutableStateOf(true) }
    var dailySummary by remember { mutableStateOf(true) }
    var achievements by remember { mutableStateOf(true) }
    var social by remember { mutableStateOf(false) }
    var motivational by remember { mutableStateOf(true) }

    // Estado para los hábitos (Map de ID a Booleano)
    val habitReminders = remember { mutableStateMapOf<String, Boolean>() }

    // Inicializar el estado de los hábitos si está vacío
    LaunchedEffect(uiState.habits) {
        if (habitReminders.isEmpty()) {
            uiState.habits.forEach { habitReminders[it.id] = true }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // --- Header ---
        item {
            Row(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(36.dp).background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                }
                Text("Notifications", color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp))
            }
        }

        // --- Master Toggle ---
        item {
            val masterBg = if (isMasterOn) HabitPurple.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface
            val masterBorder = if (isMasterOn) HabitPurple.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outlineVariant

            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(masterBg)
                    .border(1.5.dp, masterBorder, RoundedCornerShape(24.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier.size(40.dp).background(if(isMasterOn) HabitPurple.copy(alpha = 0.2f) else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if(isMasterOn) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                        contentDescription = null,
                        tint = if(isMasterOn) HabitPurple else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column(Modifier.weight(1f)) {
                    Text("All Notifications", color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text(if(isMasterOn) "Reminders are active" else "All muted", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                }
                CustomToggle(checked = isMasterOn, onCheckedChange = { isMasterOn = it })
            }
            Spacer(Modifier.height(16.dp))
        }

        // --- Alert Types Section ---
        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .alpha(if (isMasterOn) 1f else 0.4f) // Efecto de master disable
            ) {
                Box(Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceVariant).padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text("ALERT TYPES", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }

                NotificationTypeRow(Icons.Default.Whatshot, "Streak Alerts", "When streak is at risk", HabitOrange, streakAlert, { streakAlert = it }, isMasterOn)
                NotificationTypeRow(Icons.Default.Notifications, "Daily Summary", "End of day report", HabitPurple, dailySummary, { dailySummary = it }, isMasterOn)
                NotificationTypeRow(Icons.Default.EmojiEvents, "Achievements", "Unlock notifications", HabitYellow, achievements, { achievements = it }, isMasterOn)
                NotificationTypeRow(Icons.Default.People, "Social Activity", "Friends & leaderboard", HabitTeal, social, { social = it }, isMasterOn)
            }
            Spacer(Modifier.height(24.dp))
        }

        // --- Habit Reminders ---
        item {
            Text("Habit Reminders", modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp), color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        items(uiState.habits) { habit ->
            val isEnabled = habitReminders[habit.id] ?: false
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 4.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                    .alpha(if (isMasterOn) 1f else 0.4f)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(habit.emoji, fontSize = 20.sp)
                Column(Modifier.weight(1f)) {
                    Text(habit.name, color = MaterialTheme.colorScheme.onSurface, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.AccessTime, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(10.dp))
                        Text("09:00 · Daily", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                    }
                }
                CustomToggle(
                    checked = isEnabled,
                    onCheckedChange = { if(isMasterOn) habitReminders[habit.id] = it },
                    activeColor = Color(parseHexColor(habit.color))
                )
            }
        }
    }
}

