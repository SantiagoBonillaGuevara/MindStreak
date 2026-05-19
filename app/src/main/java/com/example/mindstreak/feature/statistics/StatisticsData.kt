package com.example.mindstreak.feature.statistics

import com.example.mindstreak.data.model.Habit
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

class StatisticsProcessor(private val habits: List<Habit>) {

    // 1. Estadísticas de la semana (ej: "33/42")
    fun getWeekStats(): String {
        val totalExpected = habits.size * 7
        val totalDone = habits.sumOf { h ->
            h.weekHistory.count { it }
        }
        return "$totalDone/$totalExpected"
    }

    // 2. Mejor racha (Fuente de verdad: user o el max de los hábitos)
    fun getBestStreak(bestUserStreak: Int): String {
        val maxHabitStreak = habits.maxOfOrNull { it.streak } ?: 0
        return maxOf(bestUserStreak, maxHabitStreak).toString()
    }

    // 3. Promedio por día (basado en la última semana)
    fun getAvgPerDay(): String {
        if (habits.isEmpty()) return "0.0"
        val totalDone = habits.sumOf { h ->
            h.weekHistory.count { it }
        }
        val avg = totalDone.toFloat() / 7f
        return "%.1f".format(avg)
    }

    // 4. Cambio de tendencia (entre ayer y hoy)
    fun getTrendChange(): String {
        val today = LocalDate.now().toString()
        val yesterday = LocalDate.now().minusDays(1).toString()

        val doneToday = habits.count { h -> h.completionLog[today] == true }
        val doneYesterday = habits.count { h -> h.completionLog[yesterday] == true }

        if (doneYesterday == 0) {
            return if (doneToday > 0) "+100%" else "0%"
        }

        val change = ((doneToday - doneYesterday).toFloat() / doneYesterday.toFloat()) * 100
        val sign = if (change >= 0) "+" else ""
        return "$sign${change.toInt()}%"
    }

    // 5. Datos para el gráfico de barras (Semana)
    fun getWeeklyBarData(): List<Pair<String, Float>> {
        val today = LocalDate.now()
        val mondayOffset = today.dayOfWeek.value - 1
        val monday = today.minusDays(mondayOffset.toLong())

        return (0..6).map { i ->
            val date = monday.plusDays(i.toLong())
            val dateStr = date.toString()
            val done = habits.count { it.completionLog[dateStr] == true }
            val total = habits.size
            val label = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            val percent = if (total > 0) done.toFloat() / total.toFloat() else 0f
            label to percent
        }
    }

    // 5b. Datos para el gráfico de barras (Mes - Agrupado por semanas)
    fun getMonthlyBarData(): List<Pair<String, Float>> {
        val today = LocalDate.now()
        val firstDayOfMonth = today.withDayOfMonth(1)
        
        // Vamos a mostrar 4 semanas del mes actual
        return (0..3).map { i ->
            val startOfWeek = firstDayOfMonth.plusWeeks(i.toLong())
            val endOfWeek = startOfWeek.plusDays(6)
            
            var totalDone = 0
            var totalExpected = 0
            
            var current = startOfWeek
            while (!current.isAfter(endOfWeek)) {
                val dateStr = current.toString()
                totalDone += habits.count { it.completionLog[dateStr] == true }
                totalExpected += habits.size
                current = current.plusDays(1)
            }
            
            val label = "Wk ${i + 1}"
            val percent = if (totalExpected > 0) totalDone.toFloat() / totalExpected.toFloat() else 0f
            label to percent
        }
    }

    // 5c. Datos para el gráfico de barras (Año - Agrupado por meses)
    fun getYearlyBarData(): List<Pair<String, Float>> {
        val currentYear = LocalDate.now().year
        
        return (1..12).map { month ->
            val firstDayOfMonth = LocalDate.of(currentYear, month, 1)
            val lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1)
            
            var totalDone = 0
            var totalExpected = 0
            
            var current = firstDayOfMonth
            while (!current.isAfter(lastDayOfMonth)) {
                val dateStr = current.toString()
                totalDone += habits.count { it.completionLog[dateStr] == true }
                totalExpected += habits.size
                current = current.plusDays(1)
            }
            
            val label = firstDayOfMonth.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
            val percent = if (totalExpected > 0) totalDone.toFloat() / totalExpected.toFloat() else 0f
            label to percent
        }
    }

    // 6. Datos para la tendencia (últimos 7 días)
    fun getTrendData(): List<Float> {
        val today = LocalDate.now()
        return (6 downTo 0).map { i ->
            val date = today.minusDays(i.toLong()).toString()
            val done = habits.count { it.completionLog[date] == true }
            val total = habits.size
            if (total > 0) (done.toFloat() / total.toFloat()) * 100f else 0f
        }
    }
}
