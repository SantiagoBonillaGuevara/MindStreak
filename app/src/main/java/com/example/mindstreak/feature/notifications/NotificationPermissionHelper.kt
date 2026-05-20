package com.example.mindstreak.feature.notifications

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

object NotificationPermissionHelper {
    @Composable
    fun rememberLauncher(onGranted: () -> Unit): ManagedActivityResultLauncher<String, Boolean> {
        val context = LocalContext.current
        return rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                onGranted()
            } else {
                Toast.makeText(context, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }
    @SuppressLint("InlinedApi", "ObsoleteSdkInt")
    @RequiresApi(Build.VERSION_CODES.S)
    fun checkAndRequestPermissions(
        context: Context,
        permissionLauncher: ManagedActivityResultLauncher<String, Boolean>,
        onSuccess: () -> Unit
    ) {
        val isApi33 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        val isApi31 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

        // 1. Verificar Permiso de Notificaciones (Android 13+)
        val hasNotificationPermission = if (isApi33) ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        else true

        // 2. Verificar Permiso de Alarma Exacta (Android 12+)
        val hasAlarmPermission = if (isApi31) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.canScheduleExactAlarms()
        } else true

        when {
            // Caso A: Falta permiso de alarma (API 31+)
            !hasAlarmPermission -> {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
                Toast.makeText(context, "Habilita alarmas exactas para los recordatorios", Toast.LENGTH_LONG).show()
            }
            // Caso B: Falta permiso de notificaciones (API 33+)
            !hasNotificationPermission -> permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            // Caso C: Todo OK o versión antigua
            else -> onSuccess()
        }
    }
}