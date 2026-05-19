package com.example.mindstreak.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.mindstreak.data.model.Habit
import java.util.*

class HabitNotificationScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val TAG = "HabitNotificationSched"

    fun schedule(habit: Habit) {
        // Si el hábito no tiene recordatorio o tiempo, cancelamos cualquier alarma previa
        if (!habit.reminderEnabled || habit.reminderTime.isEmpty()) {
            cancel(habit.id)
            return
        }

        val intent = Intent(context, HabitAlarmReceiver::class.java).apply {
            putExtra("HABIT_ID", habit.id)
            putExtra("HABIT_NAME", habit.name)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            habit.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            val (hour, minute) = habit.reminderTime.split(":").map { it.toInt() }
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                // Si la hora ya pasó hoy, programar para mañana
                if (before(Calendar.getInstance())) {
                    add(Calendar.DATE, 1)
                }
            }

            // Usamos setExactAndAllowWhileIdle para máxima precisión en Doze mode
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
            Log.d(TAG, "Alarma programada: ${habit.name} a las ${habit.reminderTime} (ID: ${habit.id.hashCode()})")
        } catch (e: Exception) {
            Log.e(TAG, "Error al programar alarma para ${habit.name}: ${e.message}")
        }
    }

    fun cancel(habitId: String) {
        val intent = Intent(context, HabitAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            habitId.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
            Log.d(TAG, "Alarma cancelada para el hábito con ID: $habitId")
        }
    }

    fun cancelAll(habits: List<Habit>) {
        habits.forEach { cancel(it.id) }
        Log.d(TAG, "Todas las alarmas han sido canceladas")
    }
}
