package com.example.mindstreak.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.mindstreak.data.local.SettingsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class HabitAlarmReceiver : BroadcastReceiver() {
    private val TAG = "HabitAlarmReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        val habitName = intent.getStringExtra("HABIT_NAME") ?: "Hábito"
        val settingsManager = SettingsManager(context)

        // Usamos un Scope para leer del DataStore de forma asíncrona
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // REGLA DE NEGOCIO: Validar Master Switch antes de mostrar
                val notificationsEnabled = settingsManager.notificationsEnabledFlow.first()
                Log.d(TAG, "Alarma recibida para $habitName. Master Switch: $notificationsEnabled")

                if (notificationsEnabled) {
                    val notificationHelper = NotificationHelper(context)
                    notificationHelper.showNotification(
                        title = context.getString(com.example.mindstreak.R.string.notif_habit_title),
                        message = context.getString(com.example.mindstreak.R.string.notif_habit_message, habitName)
                    )
                } else {
                    Log.d(TAG, "Notificación omitida porque el Master Switch está desactivado")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error procesando alarma: ${e.message}")
            }
        }
    }
}
