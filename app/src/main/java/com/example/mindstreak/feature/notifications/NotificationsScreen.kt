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
            .background(Color(0xFF0A0A14)),
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
                    modifier = Modifier.size(36.dp).background(Color(0xFF1C1C2E), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFF8888A8), modifier = Modifier.size(16.dp))
                }
                Text("Notifications", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            }
        }

        // --- Master Toggle ---
        item {
            val masterBg = if (isMasterOn) Color(0xFF7C6EFF).copy(alpha = 0.1f) else Color(0xFF13131F)
            val masterBorder = if (isMasterOn) Color(0xFF7C6EFF).copy(alpha = 0.3f) else Color(0xFF2A2A45)

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
                    modifier = Modifier.size(40.dp).background(if(isMasterOn) Color(0xFF7C6EFF).copy(alpha = 0.2f) else Color(0xFF2A2A45), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if(isMasterOn) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                        contentDescription = null,
                        tint = if(isMasterOn) Color(0xFF7C6EFF) else Color(0xFF5A5A7A),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column(Modifier.weight(1f)) {
                    Text("All Notifications", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text(if(isMasterOn) "Reminders are active" else "All muted", color = Color(0xFF6B6B8A), fontSize = 12.sp)
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
                    .border(1.dp, Color(0xFF2A2A45), RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .alpha(if (isMasterOn) 1f else 0.4f) // Efecto de master disable
            ) {
                Box(Modifier.fillMaxWidth().background(Color(0xFF0F0F1A)).padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text("ALERT TYPES", color = Color(0xFF8888A8), fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                }

                NotificationTypeRow(Icons.Default.Whatshot, "Streak Alerts", "When streak is at risk", Color(0xFFFF6B35), streakAlert, { streakAlert = it }, isMasterOn)
                NotificationTypeRow(Icons.Default.Notifications, "Daily Summary", "End of day report", Color(0xFF7C6EFF), dailySummary, { dailySummary = it }, isMasterOn)
                NotificationTypeRow(Icons.Default.EmojiEvents, "Achievements", "Unlock notifications", Color(0xFFFFD166), achievements, { achievements = it }, isMasterOn)
                NotificationTypeRow(Icons.Default.People, "Social Activity", "Friends & leaderboard", Color(0xFF4ECDC4), social, { social = it }, isMasterOn)
            }
            Spacer(Modifier.height(24.dp))
        }

        // --- Habit Reminders ---
        item {
            Text("Habit Reminders", modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp), color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }

        items(uiState.habits) { habit ->
            val isEnabled = habitReminders[habit.id] ?: false
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 4.dp)
                    .fillMaxWidth()
                    .background(Color(0xFF13131F), RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFF2A2A45), RoundedCornerShape(16.dp))
                    .alpha(if (isMasterOn) 1f else 0.4f)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(habit.emoji, fontSize = 20.sp)
                Column(Modifier.weight(1f)) {
                    Text(habit.name, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.AccessTime, null, tint = Color(0xFF6B6B8A), modifier = Modifier.size(10.dp))
                        Text("09:00 · Daily", color = Color(0xFF6B6B8A), fontSize = 11.sp)
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

@Composable
fun NotificationTypeRow(
    icon: ImageVector,
    label: String,
    sub: String,
    color: Color,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    masterEnabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF13131F))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier.size(36.dp).background(color.copy(alpha = 0.15f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(16.dp))
        }
        Column(Modifier.weight(1f)) {
            Text(label, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(sub, color = Color(0xFF6B6B8A), fontSize = 11.sp)
        }
        CustomToggle(checked = value, onCheckedChange = { if(masterEnabled) onValueChange(it) }, activeColor = color)
    }
}

@Composable
fun CustomToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    activeColor: Color = Color(0xFF7C6EFF)
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 20.dp else 0.dp,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "toggle"
    )

    Box(
        modifier = Modifier
            .size(48.dp, 28.dp)
            .clip(CircleShape)
            .background(if (checked) activeColor else Color(0xFF2A2A45))
            .clickable { onCheckedChange(!checked) }
            .padding(3.dp)
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(22.dp)
                .background(Color.White, CircleShape)
                .shadow(elevation = 4.dp, shape = CircleShape)
        )
    }
}